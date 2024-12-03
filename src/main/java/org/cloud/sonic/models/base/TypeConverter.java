package org.cloud.sonic.controller.models.base;

import org.cloud.sonic.common.tools.BeanTool;
import org.cloud.sonic.common.tools.ReflectionTool;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import static org.cloud.sonic.common.tools.BeanTool.updateProperties;

/**
 * 映射dto与domain关系
 *
 * @param <CURRENT>
 * @param <TARGET>
 */

public interface TypeConverter<CURRENT extends TypeConverter<CURRENT, TARGET>, TARGET> {

    /**
     * Convert from target.(shallow)
     *
     * @param target target data
     * @return converted current data
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <T extends CURRENT> CURRENT convertFrom(@NonNull TARGET target) {

        updateProperties(target, this);

        return (CURRENT) this;
    }

    /**
     * Convert to target.(shallow)
     *
     * @return new target with same value(not null)
     */
    @SuppressWarnings("unchecked")
    default TARGET convertTo() {
        // Get parameterized type
        ParameterizedType currentType = parameterizedType();

        // Assert not equal
        Objects.requireNonNull(currentType, "Cannot fetch actual type because parameterized type is null");

        Class<TARGET> domainClass = (Class<TARGET>) currentType.getActualTypeArguments()[1];

        return BeanTool.transformFrom(this, domainClass);
    }

    /**
     * Update a target by current.(shallow)
     *
     * @param target updated target
     */
    default void update(TARGET target) {
        BeanTool.updateProperties(this, target);
    }


    /**
     * Get parameterized type.
     *
     * @return parameterized type or null
     */
    @Nullable
    default ParameterizedType parameterizedType() {
        return ReflectionTool.getParameterizedType(TypeConverter.class, this.getClass());
    }

}
