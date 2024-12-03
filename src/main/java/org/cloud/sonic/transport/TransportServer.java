/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.transport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.config.WsEndpointConfigure;
import org.cloud.sonic.models.domain.Agents;
import org.cloud.sonic.models.dto.ElementsDTO;
import org.cloud.sonic.models.dto.StepsDTO;
import org.cloud.sonic.models.interfaces.AgentStatus;
import org.cloud.sonic.models.interfaces.ConfType;
import org.cloud.sonic.services.*;
import org.cloud.sonic.tools.BytesTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@ServerEndpoint(value = "/agent/{agentKey}", configurator = WsEndpointConfigure.class)
public class TransportServer {
    @Autowired
    private AgentsService agentsService;
    @Autowired
    private DevicesService devicesService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private ResultDetailService resultDetailService;
    @Autowired
    private TestCasesService testCasesService;

    @Autowired
    private ConfListService confListService;

    @OnOpen
    public void onOpen(Session session, @PathParam("agentKey") String agentKey) throws IOException {
        log.info("Session: {} is requesting auth server.", session.getId());
        if (agentKey == null || agentKey.length() == 0) {
            log.info("Session: {} missing key.", session.getId());
            session.close();
            return;
        }
        Agents authResult = agentsService.auth(agentKey);
        if (authResult == null) {
            log.info("Session: {} auth failed...", session.getId());
            JSONObject auth = new JSONObject();
            auth.put("msg", "auth");
            auth.put("result", "fail");
            BytesTool.sendText(session, auth.toJSONString());
            session.close();
        } else {
            log.info("Session: {} auth successful!", session.getId());
            JSONObject auth = new JSONObject();
            auth.put("msg", "auth");
            auth.put("result", "pass");
            auth.put("id", authResult.getId());
            auth.put("highTemp", authResult.getHighTemp());
            auth.put("highTempTime", authResult.getHighTempTime());
            auth.put("remoteTimeout", confListService.searchByKey(ConfType.REMOTE_DEBUG_TIMEOUT).getContent());
            BytesTool.sendText(session, auth.toJSONString());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonMsg = JSON.parseObject(message);
        if (jsonMsg.getString("msg").equals("ping")) {
            Session agentSession = BytesTool.agentSessionMap.get(jsonMsg.getInteger("agentId"));
            if (agentSession != null) {
                JSONObject pong = new JSONObject();
                pong.put("msg", "pong");
                BytesTool.sendText(agentSession, pong.toJSONString());
            }
            return;
        }
        log.info("Session :{} send message: {}", session.getId(), jsonMsg);
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
            case "agentInfo": {
                Session agentSession = BytesTool.agentSessionMap.get(jsonMsg.getInteger("agentId"));
                if (agentSession != null) {
                    try {
                        agentSession.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BytesTool.agentSessionMap.remove(jsonMsg.getInteger("agentId"));
                }
                BytesTool.agentSessionMap.put(jsonMsg.getInteger("agentId"), session);
                jsonMsg.remove("msg");
                agentsService.saveAgents(jsonMsg);
            }
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
                Session agentSession = BytesTool.agentSessionMap.get(jsonMsg.getInteger("agentId"));
                if (agentSession != null) {
                    BytesTool.sendText(agentSession, steps.toJSONString());
                }
                break;
            case "errCall":
                agentsService.errCall(jsonMsg.getInteger("agentId"), jsonMsg.getString("udId"), jsonMsg.getInteger("tem"), jsonMsg.getInteger("type"));
                break;
            case "generateStep":
                JSONObject step = generateStep(jsonMsg, "runStep");
                Session agentSession2 = BytesTool.agentSessionMap.get(jsonMsg.getInteger("agentId"));
                if (agentSession2 != null) {
                    BytesTool.sendText(agentSession2, step.toJSONString());
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

    /**
     * 增加元素定位时，为了方便验证定位是否有效，此时可能还没有入库，所以先生成一个临时的步骤
     *
     * @param jsonMsg
     * @param msg
     * @return
     */
    private JSONObject generateStep(JSONObject jsonMsg, String msg) {
        StepsDTO stepsDTO = new StepsDTO();
        ElementsDTO elementsDTO = new ElementsDTO();
        elementsDTO.setEleType(jsonMsg.getString("eleType"))
                .setEleValue(jsonMsg.getString("element"))
                .setEleName("定位控件测试");
        List<ElementsDTO> elements = new ArrayList<>();
        elements.add(elementsDTO);
        String stepType = jsonMsg.getString("eleType").equals("point") ? "tap" : "click";
        stepsDTO.setStepType(stepType)
                .setConditionType(0)
                .setElements(elements)
                .setPlatform(jsonMsg.getInteger("pf"));
        JSONArray step = new JSONArray();
        step.add(stepsDTO);
        JSONObject stepObj = new JSONObject();
        stepObj.put("msg", msg);
        stepObj.put("pf", jsonMsg.getInteger("pf"));
        stepObj.put("steps", step);
        stepObj.put("sessionId", jsonMsg.getString("sessionId"));
        stepObj.put("pwd", jsonMsg.getString("pwd"));
        stepObj.put("udId", jsonMsg.getString("udId"));
        return stepObj;
    }


    @OnClose
    public void onClose(Session session) {
        log.info("Agent: {} disconnected.", session.getId());
        for (Map.Entry<Integer, Session> entry : BytesTool.agentSessionMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                int agentId = entry.getKey();
                agentsService.offLine(agentId);
            }
        }
        BytesTool.agentSessionMap.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("Agent: {},on error", session.getId());
        log.error(error.getMessage());
    }
}
