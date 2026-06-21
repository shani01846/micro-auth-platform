package com.example.demo.Aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.demo.Controllers.*.*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info(">>> נכנס ל-{}.{}() עם פרמטרים: {}", className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;

        logger.info("<<< יצא מ-{}.{}() - לקח {}ms - תוצאה: {}", className, methodName, duration, result);

        return result;
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logException(Exception exception) {
        logger.error("!!! שגיאה ב-Controller: {} - סוג: {}",
                exception.getMessage(),
                exception.getClass().getSimpleName());
    }
}
