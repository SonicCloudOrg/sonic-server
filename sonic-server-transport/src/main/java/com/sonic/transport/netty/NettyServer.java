package com.sonic.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class NettyServer implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private static Map<Integer, Channel> map = new ConcurrentHashMap<>();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    @Value("${sonic.netty.port}")
    private int serverPort;

    @Override
    public void run(ApplicationArguments args) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        InetSocketAddress socketAddress = new InetSocketAddress(inetAddress.getHostAddress(), serverPort);
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup(200);
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        socketChannel.pipeline().addLast(new SecurityHandler());
                        socketChannel.pipeline().addLast(new IdleStateHandler(8, 4, 4, TimeUnit.MINUTES));
                    }
                })
                .localAddress(socketAddress)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            logger.info("Netty服务启动: {}", socketAddress);
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            if (future.isSuccess()) {
                logger.info("Netty服务启动成功！");
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("Netty服务器关闭！");
        }
    }

    public static Map<Integer, Channel> getMap() {
        return map;
    }
}