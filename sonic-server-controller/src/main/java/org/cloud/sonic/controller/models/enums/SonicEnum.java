package org.cloud.sonic.controller.models.enums;

import org.springframework.util.Assert;

import java.util.stream.Stream;

/**
 * * 跟agent同步，后续优化
 *
 * @author JayWenStar
 * @date 2022/3/13 1:55 下午
 */
public interface SonicEnum<T> {

    /**
     * 获取枚举值<T>
     *
     * @return enum value
     */
    T getValue();

    /**
     * 将value转成枚举
     */
    static <T, E extends Enum<E> & SonicEnum<T>> E valueToEnum(Class<E> enumType, T value) {
        Assert.notNull(enumType, "enum type must not be null");
        Assert.notNull(value, "value must not be null");
        Assert.isTrue(enumType.isEnum(), "type must be an enum type");

        return Stream.of(enumType.getEnumConstants())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown database value: " + value));
    }

}
