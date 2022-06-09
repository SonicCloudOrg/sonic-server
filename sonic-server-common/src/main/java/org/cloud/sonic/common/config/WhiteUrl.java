package org.cloud.sonic.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yaming116
 * @des 标记当前方法是白名单，不需要进行鉴权
 * @date 2022/6/4 23:26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WhiteUrl {
}
