package com.biu.wifi.component.analyze;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 用于分析dao操作超过3min的语句
 * 日志位置:hfmis.slow.log
 * </pre>
 */
@Aspect
@Component
public class ServiceSlowInterceptor {

    private Log logger = LogFactory.getLog(ServiceSlowInterceptor.class);

    @Pointcut("execution(* com.biu.wifi..service..*(..))")
    public void logger() {
    }

    @Around("com.biu.wifi.component.analyze.ServiceSlowInterceptor.logger()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {

        Object result = null;

        Date startDate = new Date();

        String targetName = null;

        targetName = String.valueOf(pjp);

        try {
            // 执行处理
            result = pjp.proceed();

        } finally {

            Date endDate = new Date();

            long time = endDate.getTime() - startDate.getTime();
            if (time > 500) {

                Map<String, Object> infoMap = new HashMap<String, Object>();

                Object[] argArray = pjp.getArgs();
                for (int i = 0; i < argArray.length; i++) {
                    infoMap.put(String.valueOf(i), argArray[i]);
                }

                logger.info("处理时间:" + time + "毫秒，方法:" + targetName + "属于缓慢时间设定，方法参数:" + pjp.getArgs());
            }
        }
        return result;
    }
}
