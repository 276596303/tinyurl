package org.xiaoxi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.xiaoxi.service.impl.TinyurlServiceImpl;

import java.util.Arrays;

/**
 * Created by YanYang on 2016/6/26.
 */
@Component
@Aspect
public class TinyurlLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyurlServiceImpl.class);

    //TODO

    //@Around("execution(* org.xiaoxi.web.UrlTransferContrl.getLongUrl(..))")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        Object o = null;
        try {
            o = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("传入参数: " + Arrays.asList(joinPoint.getArgs()));
        LOGGER.info(joinPoint.toShortString() + " 方法执行耗时 " + (endTime - startTime) + "ms");

        return o;
    }
}
