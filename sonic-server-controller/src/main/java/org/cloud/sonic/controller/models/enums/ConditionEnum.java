package org.cloud.sonic.controller.models.enums;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @date 2022/3/13 1:49 下午
 */
public enum ConditionEnum implements SonicEnum<Integer>, Serializable {

    /**
     * 非条件
     */
    NONE(0, "none"),

    /**
     * if 条件
     */
    IF(1, "if"),

    /**
     * else if 条件
     */
    ELSE_IF(2, "else_if"),

    /**
     * else 条件
     */
    ELSE(3, "else"),

    /**
     * while 条件
     */
    WHILE(4, "while");

    private final Integer value;

    private final String name;

    ConditionEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
