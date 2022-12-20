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
