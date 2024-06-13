package org.cloud.sonic.controller.transport;


import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.cloud.sonic.controller.config.WsEndpointConfigure;
import org.springframework.stereotype.Component;
import org.java_websocket.enums.ReadyState;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ServerEndpoint(value = "/hub/{host}/{port}/{platform}/{key}/{udId}/{token}", configurator = WsEndpointConfigure.class)
public class HubServer {
    HashMap<Session,TransportClient> hubMap = new HashMap<Session,TransportClient>();
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    
    @OnOpen
    public void onOpen(Session session, @PathParam("host") String host, @PathParam("port") String port, @PathParam("platform") String platform, @PathParam("key") String key, @PathParam("udId") String udId, @PathParam("token") String token) {    
        String agentUri = String.format("ws://%s:%s/websockets/%s/%s/%s/%s", host, port, platform, key, udId, token);
        
        try {
            URI uri = new URI(agentUri);
            TransportClient agent = new TransportClient(session, uri);
            agent.connect();
            hubMap.put(session, agent);
            log.info(String.format("Hub Server: Connected to agent\nUrl=%s", agentUri));
        }
        catch(Exception ex)
        {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(String.format("Hub Server: Failed to connect agent!\nUrl=%s\nError=%s", agentUri, sw.toString()));
        }
    }
    
    @OnMessage
    public void onMessage(Session session, String message) {
        if (hubMap.get(session).getReadyState() == ReadyState.NOT_YET_CONNECTED)
        {
            cachedThreadPool.execute(() -> {
                boolean needRetry = true;
                while (needRetry) {
                    try {
                        if (hubMap.get(session).getReadyState() == ReadyState.OPEN) {
                            log.info(String.format("Hub Server: Messages forwarded to agent\nUrl=%s", hubMap.get(session).getUrl()));
                            hubMap.get(session).send(message);
                            needRetry = false;
                        } else if (hubMap.get(session).getReadyState() == ReadyState.NOT_YET_CONNECTED) {
                            log.info(String.format("Hub Server: Wait agent in 1s\nStatus=%s\nUrl=%s", hubMap.get(session).getReadyState().toString(), hubMap.get(session).getUrl()));
                            Thread.sleep(1000);
                        } else {
                            log.info(String.format("Hub Server: Agent closed\nStatus=%s\nUrl=%s", hubMap.get(session).getReadyState().toString(), hubMap.get(session).getUrl()));
                            needRetry = false;
                        }

                    } catch (Exception error) {
                        needRetry = false;
                        StringWriter sw = new StringWriter();
                        error.printStackTrace(new PrintWriter(sw));
                        log.error(String.format("Hub Server: Fail to forward\nStatus=%s\nUrl=%s\nMessages=%s\nError=%s", hubMap.get(session).getReadyState().toString(), hubMap.get(session).getUrl(), message, sw.toString()));
                    }
                }
            });
        }
        else
        {
            log.info(String.format("Hub Server: Messages forwarded to agent\nUrl=%s", hubMap.get(session).getUrl()));
            hubMap.get(session).send(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info(String.format("Hub Server: Closed\nUrl=%s", hubMap.get(session).getUrl()));
        hubMap.get(session).close();
        hubMap.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        StringWriter sw = new StringWriter();
        error.printStackTrace(new PrintWriter(sw));
        log.error(String.format("Hub Server: Error caught!\nUrl=%s\nError=%s", hubMap.get(session).getUrl(), sw.toString()));
    }
}
