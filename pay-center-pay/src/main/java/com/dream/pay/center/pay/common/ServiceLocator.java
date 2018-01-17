package com.dream.pay.center.pay.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ServiceLocator implements ApplicationContextAware {

	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext cxt) throws BeansException {
		context = cxt;
	}

	public Object getObject(String name) {
		return context.getBean(name);
	}

}
