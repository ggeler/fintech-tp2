package com.up.fintech.armagedon.tp4.misc.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;

	public static <T extends Object> T getBean(Class<T> beanClass) {
		return (context != null) ? context.getBean(beanClass): null;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContext.context = applicationContext;
	}

}