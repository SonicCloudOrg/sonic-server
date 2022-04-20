package org.cloud.sonic.common.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
			log.error("{%s} is not found.".formatted(name));
			return null;
		}
	}


	@Override
	public void setEmbeddedValueResolver(@NonNull StringValueResolver resolver) {
		stringValueResolver = resolver;
		setEnv();
	}

	private void setEnv() {
		String instanceHost = getPropertiesValue("zookeeper.instance-host");
		if (StringUtils.hasText(instanceHost)) {
			System.setProperty("DUBBO_IP_TO_REGISTRY", instanceHost);
		}
	}
}