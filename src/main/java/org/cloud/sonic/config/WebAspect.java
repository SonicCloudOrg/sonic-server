package org.cloud.sonic.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ZhouYiXun
 * @des 配合AOP指定对应的接口打印web请求日志
 * @date 2021/8/15 18:26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebAspect {
}