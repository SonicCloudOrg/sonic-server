package com.sonic.control.models.enums;

/**
 * @author ZhouYiXun
 * @des 定义状态
 * @date 2021/8/15 19:31
 */
public enum BugsStatus {
    NO_FIX(1),//未修复
    FIXED(2),//已修复
    CANNOT_FIX(3);//无法修复
    private int status;

    BugsStatus(int status) {
        this.status = status;
    }
}
