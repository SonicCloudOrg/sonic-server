package org.cloud.sonic.common.models.interfaces;
/**
 * @author ZhouYiXun
 * @des 定义Agent端状态类型
 * @date 2021/8/15 19:29
 */
public interface AgentStatus {
    int ONLINE = 1;
    int OFFLINE = 2;
    // Server to agent error.
    int S2AE = 3;
}
