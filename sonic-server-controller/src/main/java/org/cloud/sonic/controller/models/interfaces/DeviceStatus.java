package org.cloud.sonic.controller.models.interfaces;

/**
 * @author ZhouYiXun
 * @des 枚举设备状态
 * @date 2021/08/16 19:26
 */
public interface DeviceStatus {
    String ONLINE = "ONLINE";
    String OFFLINE = "OFFLINE";
    String TESTING = "TESTING";
    String DEBUGGING = "DEBUGGING";
    String ERROR = "ERROR";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String DISCONNECTED = "DISCONNECTED";
}
