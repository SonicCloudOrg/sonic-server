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
package org.cloud.sonic.controller.tools;

import java.io.IOException;
import java.net.ServerSocket;

public class PortTool {
    public static int port = 0;

    public static Integer getPort() {
        if (port == 0) {
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(0);
                port = serverSocket.getLocalPort();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return port;
    }
}