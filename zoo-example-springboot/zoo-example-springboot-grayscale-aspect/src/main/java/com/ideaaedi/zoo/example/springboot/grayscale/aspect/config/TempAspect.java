package com.ideaaedi.zoo.example.springboot.grayscale.aspect.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * (non-javadoc)
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Aspect
@Component
public class TempAspect implements Ordered {
    
    @Around("execution(* com.ideaaedi.zoo.example.springboot.grayscale.aspect.controller.Test2Controller.grayAbc(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.err.println(TempAspect.class + "：我是grayAbc的切面");
        return pjp.proceed();
    }
    
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
