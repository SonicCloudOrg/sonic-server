package org.cloud.sonic.controller.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.config.WsEndpointConfigure;
import org.cloud.sonic.controller.models.domain.Cabinet;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@Slf4j
@ServerEndpoint(value = "/agent/{agentKey}/{cabinetKey}", configurator = WsEndpointConfigure.class)
public class TransportServer {
    @Autowired
    private AgentsService agentsService;
    @Autowired
    private CabinetService cabinetService;

    @OnOpen
    public void onOpen(Session session, @PathParam("agentKey") String agentKey,@PathParam("cabinetKey") String cabinetKey) throws IOException {
        log.info("Session: {} is requesting auth server.", session.getId());
        if (agentKey == null || agentKey.length() == 0) {
            log.info("Session: {} missing key.", session.getId());
            session.close();
            return;
        }
        int authResult = agentsService.auth(agentKey);
        if (authResult == 0) {
            log.info("Session: {} auth failed...", session.getId());
            session.close();
        }else{
            log.info("Session: {} auth successful!", session.getId());
            JSONObject auth = new JSONObject();
            auth.put("msg", "auth");
            auth.put("result", "pass");
            auth.put("id", authResult);
            if (cabinetKey != null && cabinetKey.length()!=0) {
                Cabinet cabinet = cabinetService.getIdByKey(cabinetKey);
                if (cabinet != null) {
                    auth.put("cabinetAuth", "pass");
                    auth.put("cabinet", JSON.toJSONString(cabinet));
                } else {
                    auth.put("cabinetAuth", "fail");
                }
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject msg = JSON.parseObject(message);
        log.info("Session :{} send message: {}", session.getId(), msg);
    }
}
