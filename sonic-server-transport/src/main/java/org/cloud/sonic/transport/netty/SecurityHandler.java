package org.cloud.sonic.transport.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.transport.feign.ControllerFeignClient;
import org.cloud.sonic.transport.tools.SpringTool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(SecurityHandler.class);
    private ControllerFeignClient controllerFeignClient = SpringTool.getBean(ControllerFeignClient.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent：{} 请求连接到服务器!", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject jsonMsg = JSON.parseObject((String) msg);
        logger.info("服务器收到Agent: {} 认证消息: {}", ctx.channel().remoteAddress(), jsonMsg);
        RespModel i = controllerFeignClient.auth(jsonMsg.getString("key"));
        if (Integer.parseInt(i.getData().toString()) != 0) {
            logger.info("服务器收到Agent: {} 认证通过！", ctx.channel().remoteAddress());
            ctx.pipeline().remove(SecurityHandler.class);
            ctx.pipeline().addLast(new NettyServerHandler());
            JSONObject auth = new JSONObject();
            auth.put("msg", "auth");
            auth.put("result", "pass");
            auth.put("id", Integer.parseInt(i.getData().toString()));
            ctx.channel().writeAndFlush(auth.toJSONString());
        } else {
            logger.info("服务器收到Agent: {} 认证不通过！", ctx.channel().remoteAddress());
            JSONObject result = new JSONObject();
            result.put("msg", "auth");
            result.put("result", "fail");
            ctx.channel().writeAndFlush(result.toJSONString());
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("Agent: {} 发生异常 {}", ctx.channel().remoteAddress(), cause.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent: {} 连接断开", ctx.channel().remoteAddress());
        ctx.close();
    }
}