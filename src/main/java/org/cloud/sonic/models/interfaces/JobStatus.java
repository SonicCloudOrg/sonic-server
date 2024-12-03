package org.cloud.sonic.controller.models.interfaces;

/**
 * @author ZhouYiXun
 * @des 定时任务状态
 * @date 2021/8/22 11:09
 */
public interface JobStatus {
    int ENABLE = 1;
    int DISABLE = 2;
    int ONCE = 3;
}
