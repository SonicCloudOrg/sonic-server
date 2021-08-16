package com.sonic.control.models.enums;

/**
 * @author ZhouYiXun
 * @des 定义Agent端状态类型
 * @date 2021/8/15 19:29
 */
public enum AgentStatus {
    ONLINE(1),
    OFFLINE(2);
    private int status;

    AgentStatus(int status) {
        this.status = status;
    }
}
