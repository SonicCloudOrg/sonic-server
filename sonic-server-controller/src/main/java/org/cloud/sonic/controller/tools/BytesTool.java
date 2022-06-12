package org.cloud.sonic.controller.tools;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BytesTool {
    public static Map<Integer,Session> agentSessionMap = new HashMap<>();

    public static void sendText(Session session, String message) {
        if (session == null || !session.isOpen()) {
            return;
        }
        synchronized (session) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IllegalStateException | IOException e) {
                log.error("WebSocket send msg failed...");
            }
        }
    }
}
