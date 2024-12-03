package org.cloud.sonic.controller.models.interfaces;

/**
 * @author ZhouYiXun
 * @des 定义状态
 * @date 2021/8/15 19:50
 */
public interface ResultStatus {
    int RUNNING = 0;
    int PASS = 1;
    int WARNING = 2;
    int FAIL = 3;
}
