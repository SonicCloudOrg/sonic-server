package com.sonic.control.Models.Enum;

/**
 * @author ZhouYiXun
 * @des 定义类型
 * @date 2021/8/15 19:30
 */
public enum BugsType {
    CRASH(1), //崩溃
    LAG(2); //卡顿
    private int type;

    BugsType(int type) {
        this.type = type;
    }
}