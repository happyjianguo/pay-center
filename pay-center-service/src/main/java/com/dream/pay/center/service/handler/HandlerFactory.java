package com.dream.pay.center.service.handler;

import com.dream.pay.center.common.enums.ErrorEnum;
import com.dream.pay.center.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理器工厂类.
 * <pre>
 * 1).实现BeanPostProcessor, 在所有bean实例化后查找所有Handler子类.
 * </pre>
 *
 * @author mengzhenbin
 * @version HandlerFactory.java
 *          2016-12-21 21:08
 */
@Slf4j
@Component
public class HandlerFactory implements BeanPostProcessor {

    //指令处理器容器
    Map<Class, Handler> handlerMap = new ConcurrentHashMap<Class, Handler>();


    /**
     * 根据类型获取处理器实例
     *
     * @param clazz 类型
     * @return 处理器实例
     */
    public <T> T getHandler(Class<T> clazz) {
        return (T) handlerMap.get(clazz);
    }


    /**
     * {@inheritDoc}
     */

    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    /**
     * {@inheritDoc} <p> 所有bean实例化后查找Handler子类,解析指令及对应的处理器,放在容器中 </p>
     */

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Handler) {
            Class clazz = bean.getClass();
            if (handlerMap.containsKey(clazz)) {
                log.error("重复的Handler定义,name={}", beanName);
                throw new BaseException(ErrorEnum.SYSTEM_ERROR, "重复的Handler定义,name=" + beanName);
            } else {
                handlerMap.put(clazz, (Handler) bean);
                log.info("Handler实例放入缓存,name={}", beanName);
            }
        }
        return bean;
    }
}
