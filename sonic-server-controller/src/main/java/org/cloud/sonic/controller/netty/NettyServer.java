package org.cloud.sonic.controller.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.cloud.sonic.controller.tools.PortTool;
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
        if (serverPort == 0) {
            serverPort = PortTool.getPort();
        }
        InetSocketAddress socketAddress = new InetSocketAddress(serverPort);
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup(200);
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024,0,2));
                        socketChannel.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
                        socketChannel.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
                        socketChannel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
//                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
//                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        socketChannel.pipeline().addLast(new SecurityHandler());
                        socketChannel.pipeline().addLast(new IdleStateHandler(8, 4, 4, TimeUnit.MINUTES));
                        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/test",null,true,65535));
                    }
                })
                .localAddress(socketAddress)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            logger.info("Netty Server launch: {}", socketAddress);
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            if (future.isSuccess()) {
                logger.info("Netty Server Start successful!");
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            logger.info("Netty server shutdown...");
        }
    }

    public static Map<Integer, Channel> getMap() {
        return map;
    }
}
