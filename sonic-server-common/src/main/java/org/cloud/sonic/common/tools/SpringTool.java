package org.cloud.sonic.common.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
@Slf4j
public final class SpringTool implements ApplicationContextAware, EmbeddedValueResolverAware {
	private static ApplicationContext applicationContext = null;
	private static StringValueResolver stringValueResolver = null;

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		if (SpringTool.applicationContext == null) {
			SpringTool.applicationContext = applicationContext;
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	public static <T> T getBean(Class<T> clazz){
		return getApplicationContext().getBean(clazz);
	}

	public static String getPropertiesValue(String name) {
		try {
			name = "${" + name + "}";
			return stringValueResolver.resolveStringValue(name);
		} catch (Exception e) {
			log.error("当前环境变量中没有{%s}的配置".formatted(name));
			return null;
		}
	}


	@Override
	public void setEmbeddedValueResolver(@NonNull StringValueResolver resolver) {
		stringValueResolver = resolver;
	}
}