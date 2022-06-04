package org.cloud.sonic.controller.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.tools.SpringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(SecurityHandler.class);
    private AgentsService agentsService = SpringTool.getBean(AgentsService.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent：{} 请求连接到服务器!", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject jsonMsg = JSON.parseObject((String) msg);
        logger.info("服务器收到Agent: {} 认证消息: {}", ctx.channel().remoteAddress(), jsonMsg);
        Integer i = agentsService.auth(jsonMsg.getString("key"));
        if (i != null && i != 0) {
            logger.info("服务器收到Agent: {} 认证通过！", ctx.channel().remoteAddress());
            ctx.pipeline().remove(SecurityHandler.class);
            ctx.pipeline().addLast(new NettyServerHandler());
            JSONObject auth = new JSONObject();
            auth.put("msg", "auth");
            auth.put("result", "pass");
            auth.put("id", i);
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