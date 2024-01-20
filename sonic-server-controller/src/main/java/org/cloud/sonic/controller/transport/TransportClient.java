/*
 *   sonic-agent  Agent of Sonic Cloud Real Machine Platform.
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
package org.cloud.sonic.controller.transport;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import org.cloud.sonic.controller.tools.BytesTool;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.io.StringWriter;
import java.io.PrintWriter;

@Slf4j
public class TransportClient extends WebSocketClient {
    Session hubSession;
    String url;

    public TransportClient(Session session, URI serverUri) {
        super(serverUri);
        hubSession = session;
        url = serverUri.toString();
    }

    public String getUrl()
    {
        return url;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info(String.format("Hub Client: Connected to '%s'", url));
    }

    @Override
    public void onMessage(String s) {
        log.info(String.format("Hub Client: Messages forwarded to server\nUrl=%s", url));
        BytesTool.sendText(hubSession, s);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        log.info(String.format("Hub Client: Bytes forwarded to server\nUrl=%s", url));
        BytesTool.sendByte(hubSession, bytes);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info(String.format("Hub Client: Disconnected\nUrl=%s\nReason=%s", url, reason));
    }

    @Override
    public void onError(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        log.error(String.format("Hub Server: Error caught!\nUrl=%s\nError=%s", url, sw.toString()));
    }
}
