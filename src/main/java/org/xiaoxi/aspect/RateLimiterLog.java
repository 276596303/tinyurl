package org.xiaoxi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xiaoxi.rateLimiter.dao.impl.RateHandleImpl;

import java.util.Arrays;

/**
 * Created by YanYang on 2016/6/26.
 */
@Component
@Aspect
public class RateLimiterLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateHandleImpl.class);

    @Around("execution(* org.xiaoxi.rateLimiter.dao.impl.RateHandleImpl.isLimit(..))")
    public Object rateLimit(ProceedingJoinPoint joinPoint) {
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
