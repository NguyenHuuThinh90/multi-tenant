package com.app.config;

import com.app.annotation.TrackAction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggerManager {

    @Around(value = "@annotation(trackAction)")
    public Object returnUserAction(ProceedingJoinPoint joinPoint, TrackAction trackAction) throws Throwable {
        Object object = joinPoint.proceed();
        return object;
    }

    @AfterThrowing(value = "@annotation(com.app.annotation.TrackAction)", throwing = "ex")
    public void throwAction(JoinPoint joinPoint, Throwable ex) {
        System.out.println("throw");
    }
}
