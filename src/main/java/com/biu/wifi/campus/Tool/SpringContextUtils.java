package com.biu.wifi.campus.Tool;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取ApplicationContext和Object的工具类
 *
 * @author HJD
 */

public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        applicationContext = arg0;
    }

    /**
     * 获取applicationContext对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    public static Object getBean(String beanName) {
        if (!applicationContext.containsBean(beanName)) {
            // 第一位如果是小写就大写,如果大写就小写
            if (!beanName.substring(0, 1).toLowerCase().equals(beanName.substring(0, 1))) {
                beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
            } else if (!beanName.substring(0, 1).toUpperCase().equals(beanName.substring(0, 1))) {
                beanName = beanName.substring(0, 1).toUpperCase() + beanName.substring(1);
            }
        }
        return applicationContext.getBean(beanName);
    }


    /**
     * 根据bean的注解来查找所有的对象
     *
     * @param clazz
     * @return
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> clazz) {
        return applicationContext.getBeansWithAnnotation(clazz);
    }
}