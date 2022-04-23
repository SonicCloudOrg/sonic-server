package org.cloud.sonic.transport.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.transport.feign.ControllerFeignClient;
import org.cloud.sonic.transport.tools.SpringTool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private ControllerFeignClient controllerFeignClient = SpringTool.getBean(ControllerFeignClient.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent：{} 连接到服务器!", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject jsonMsg = JSON.parseObject((String) msg);
        logger.info("服务器收到Agent: {} 消息: {}", ctx.channel().remoteAddress(), jsonMsg);
        switch (jsonMsg.getString("msg")) {
            case "temperature": {
                controllerFeignClient.refreshDevicesTemper(jsonMsg);
                break;
            }
            case "debugUser":
                controllerFeignClient.updateUser(jsonMsg);
                break;
            case "heartBeat":
                RespModel agentsOnline = controllerFeignClient.findAgentById(jsonMsg.getInteger("agentId"));
                LinkedHashMap agent = (LinkedHashMap) agentsOnline.getData();
                if (Integer.parseInt(agent.get("status").toString()) != 1) {
                    agent.put("status", 1);
                    controllerFeignClient.saveAgent((JSONObject) JSON.parse(String.valueOf(agent)));
                }
                break;
            case "agentInfo":
                if (NettyServer.getMap().get(jsonMsg.getInteger("agentId")) != null) {
                    NettyServer.getMap().get(jsonMsg.getInteger("agentId")).close();
                    NettyServer.getMap().remove(jsonMsg.getInteger("agentId"));
                }
                NettyServer.getMap().put(jsonMsg.getInteger("agentId"), ctx.channel());
                jsonMsg.remove("msg");
                controllerFeignClient.saveAgent(jsonMsg);
                break;
            case "subResultCount":
                controllerFeignClient.subResultCount(jsonMsg.getInteger("rid"));
                break;
            case "deviceDetail":
                controllerFeignClient.deviceStatus(jsonMsg);
                break;
            case "step":
            case "perform":
            case "record":
            case "status":
                controllerFeignClient.saveResultDetail(jsonMsg);
                break;
            case "findSteps":
                JSONObject steps = findSteps(jsonMsg, "runStep");
                NettyServer.getMap().get(jsonMsg.getInteger("agentId")).writeAndFlush(steps.toJSONString());
                break;
        }
    }

    /**
     * 查找 & 封装步骤对象
     *
     * @param jsonMsg   websocket消息
     * @return          步骤对象
     */
    private JSONObject findSteps(JSONObject jsonMsg, String msg) {
        LinkedHashMap j = (LinkedHashMap) controllerFeignClient.findSteps(jsonMsg.getInteger("caseId")).getData();
        JSONObject steps = new JSONObject();
        steps.put("cid", jsonMsg.getInteger("caseId"));
        steps.put("msg", msg);
        steps.put("pf", j.get("pf"));
        steps.put("steps", j.get("steps"));
        steps.put("gp", j.get("gp"));
        steps.put("sessionId", jsonMsg.getString("sessionId"));
        steps.put("pwd", jsonMsg.getString("pwd"));
        steps.put("udId", jsonMsg.getString("udId"));
        return steps;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("Agent: {} 发生异常 {}", ctx.channel().remoteAddress(), cause.fillInStackTrace());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent: {} 连接断开", ctx.channel().remoteAddress());
        for (Map.Entry<Integer, Channel> entry : NettyServer.getMap().entrySet()) {
            if (entry.getValue().equals(ctx.channel())) {
                int agentId = entry.getKey();
                controllerFeignClient.offLine(agentId);
            }
        }
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                logger.info("Agent: {} 无心跳！关闭连接...", ctx.channel().remoteAddress());
                for (Map.Entry<Integer, Channel> entry : NettyServer.getMap().entrySet()) {
                    if (entry.getValue().equals(ctx.channel())) {
                        int agentId = entry.getKey();
                        controllerFeignClient.offLine(agentId);
                    }
                }
                ctx.close();
            }
            if (event.state().equals(IdleState.ALL_IDLE)) {
                logger.info("Agent: {} 读写空闲，发送心跳检测...", ctx.channel().remoteAddress());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "heartBeat");
                ctx.channel().writeAndFlush(jsonObject.toJSONString());
            }
        }
    }
}