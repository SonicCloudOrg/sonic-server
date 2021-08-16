package com.sonic.control.models.enums;

/**
 * @author ZhouYiXun
 * @des 定义状态
 * @date 2021/8/15 19:50
 */
public enum ResultStatus {
    PASS(1),
    WARNING(2),
    FAIL(3),
    RUNNING(4);
    private int status;

    ResultStatus(int status) {
        this.status = status;
    }
}
