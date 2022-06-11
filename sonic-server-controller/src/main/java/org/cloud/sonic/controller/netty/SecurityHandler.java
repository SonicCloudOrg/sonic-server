package org.cloud.sonic.controller.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.cloud.sonic.controller.models.domain.Cabinet;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.CabinetService;
import org.cloud.sonic.controller.tools.SpringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(SecurityHandler.class);
    private AgentsService agentsService = SpringTool.getBean(AgentsService.class);
    private CabinetService cabinetService = SpringTool.getBean(CabinetService.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent：{} request connection to server.", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("test");
        JSONObject jsonMsg = JSON.parseObject((String) msg);
        logger.info("Agent: {} -> Server auth: {}", ctx.channel().remoteAddress(), jsonMsg);
        Integer i = agentsService.auth(jsonMsg.getString("agentKey"));
        if (i != null && i != 0) {
            logger.info("Agent: {} auth pass!", ctx.channel().remoteAddress());
            ctx.pipeline().remove(SecurityHandler.class);
            ctx.pipeline().addLast(new NettyServerHandler());
            JSONObject auth = new JSONObject();
            auth.put("msg", "auth");
            auth.put("result", "pass");
            auth.put("id", i);
            if (jsonMsg.getString("cabinetKey") != null) {
                Cabinet cabinet = cabinetService.getIdByKey(jsonMsg.getString("cabinetKey"));
                if (cabinet != null) {
                    auth.put("cabinetAuth", "pass");
                    auth.put("cabinet", JSON.toJSONString(cabinet));
                } else {
                    auth.put("cabinetAuth", "fail");
                }
            }
            ctx.channel().writeAndFlush(auth.toJSONString());
        } else {
            logger.info("Agent: {} auth failed!", ctx.channel().remoteAddress());
            JSONObject result = new JSONObject();
            result.put("msg", "auth");
            result.put("result", "fail");
            ctx.channel().writeAndFlush(result.toJSONString());
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("Agent: {} error,cause", ctx.channel().remoteAddress());
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent: {} disconnected.", ctx.channel().remoteAddress());
        ctx.close();
    }
}