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
package org.cloud.sonic.controller.tools.robot;

import org.springframework.web.client.RestTemplate;

/**
 * @author ayumi760405
 * @des 推送机器人方法介面
 * @date 2022/12/19
 */
public interface RobotMessenger {

    void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int pass,
                                int warn, int fail, int projectId, int resultId);

    void sendDayReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName,
                              String yesterday, String today, int passCount, int warnCount, int failCount);

    void sendErrorDevice(RestTemplate restTemplate, String token, String secret, int errorType, int tem, String udId);

    void sendWeekReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName,
                               String yesterday, String today, int passCount, int warnCount, int failCount, int count);

}
