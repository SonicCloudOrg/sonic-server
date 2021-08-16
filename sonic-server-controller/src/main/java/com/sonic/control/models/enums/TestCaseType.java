package com.sonic.control.models.enums;

/**
 * @author ZhouYiXun
 * @des 定义测试用例类型
 * @date 2021/8/15 19:50
 */
public enum TestCaseType {
    UI_AUTOMATION(1),
    MONKEY(2),
    TRAVERSE(3);
    private int type;

    TestCaseType(int type) {
        this.type = type;
    }
}
