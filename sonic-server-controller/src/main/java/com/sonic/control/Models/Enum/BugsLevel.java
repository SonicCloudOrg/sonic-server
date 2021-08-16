package com.sonic.control.Models.Enum;

/**
 * @author ZhouYiXun
 * @des 定义风险等级
 * @date 2021/8/15 19:30
 */
public enum BugsLevel {
    UNKNOWN(1), //未知
    LOW(2), //低
    MIDDLE(3), //中
    HIGH(4); //高
    private int level;

    BugsLevel(int level) {
        this.level = level;
    }
}