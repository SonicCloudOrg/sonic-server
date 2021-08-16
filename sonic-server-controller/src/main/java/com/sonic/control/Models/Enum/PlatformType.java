package com.sonic.control.Models.Enum;

/**
 * @author ZhouYiXun
 * @des 定义全局平台类型
 * @date 2021/8/15 19:26
 */
public enum PlatformType {
    ANDROID(1),
    IOS(2),
    WINDOWS(3),
    MAC(4),
    WEB(5);
    private int type;

    PlatformType(int type) {
        this.type = type;
    }
}
