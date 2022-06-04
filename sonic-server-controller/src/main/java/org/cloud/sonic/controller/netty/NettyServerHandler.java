package org.cloud.sonic.controller.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.interfaces.AgentStatus;
import org.cloud.sonic.controller.services.*;
import org.cloud.sonic.controller.tools.SpringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private DevicesService devicesService = SpringTool.getBean(DevicesService.class);
    private AgentsService agentsService = SpringTool.getBean(AgentsService.class);
    private ResultsService resultsService = SpringTool.getBean(ResultsService.class);
    private ResultDetailService resultDetailService = SpringTool.getBean(ResultDetailService.class);
    private TestCasesService testCasesService = SpringTool.getBean(TestCasesService.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("Agent：{} 连接到服务器!", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject jsonMsg = JSON.parseObject((String) msg);
        logger.info("服务器收到Agent: {} 消息: {}", ctx.channel().remoteAddress(), jsonMsg);
        switch (jsonMsg.getString("msg")) {
            case "battery": {
                devicesService.refreshDevicesBattery(jsonMsg);
                break;
            }
            case "debugUser":
                devicesService.updateDevicesUser(jsonMsg);
                break;
            case "heartBeat":
                Agents agentsOnline = agentsService.findById(jsonMsg.getInteger("agentId"));
                if (agentsOnline.getStatus() != AgentStatus.ONLINE) {
                    agentsOnline.setStatus(AgentStatus.ONLINE);
                    agentsService.saveAgents(agentsOnline);
                }
                break;
            case "agentInfo":
                if (NettyServer.getMap().get(jsonMsg.getInteger("agentId")) != null) {
                    NettyServer.getMap().get(jsonMsg.getInteger("agentId")).close();
                    NettyServer.getMap().remove(jsonMsg.getInteger("agentId"));
                }
                NettyServer.getMap().put(jsonMsg.getInteger("agentId"), ctx.channel());
                jsonMsg.remove("msg");
                agentsService.saveAgents(jsonMsg);
                break;
            case "subResultCount":
                resultsService.subResultCount(jsonMsg.getInteger("rid"));
                break;
            case "deviceDetail":
                devicesService.deviceStatus(jsonMsg);
                break;
            case "step":
            case "perform":
            case "record":
            case "status":
                resultDetailService.saveByTransport(jsonMsg);
                break;
            case "findSteps":
                JSONObject steps = findSteps(jsonMsg, "runStep");
                if (NettyServer.getMap().get(jsonMsg.getInteger("agentId")) != null) {
                    NettyServer.getMap().get(jsonMsg.getInteger("agentId")).writeAndFlush(steps.toJSONString());
                }
                break;
        }
    }

    /**
     * 查找 & 封装步骤对象
     *
     * @param jsonMsg websocket消息
     * @return 步骤对象
     */
    private JSONObject findSteps(JSONObject jsonMsg, String msg) {
        JSONObject j = testCasesService.findSteps(jsonMsg.getInteger("caseId"));
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
                agentsService.offLine(agentId);
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
                        agentsService.offLine(agentId);
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