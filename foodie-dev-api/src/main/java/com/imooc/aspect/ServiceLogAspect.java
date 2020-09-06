package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/*
* 记录每个service所需要的时间
* */
@Aspect
@Component
public class ServiceLogAspect {
    private static final Logger log= LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * 切面表达式：
     * execution 执行的位置
     * 第一处：* 代表所有的返回类型
     * 第二处：包名 代表aop监控的类所在的包
     * 第三处：.. 代表该包及其子包
     * 第四处：* 所有的类
     * 第五处：*（..) * 代表所有的方法名，(..)代表方法中的任何参数
     * @param joinPoint
     * @return
     */
    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //{} 代表占位符 {}.{} 类名.方法名
        log.info("===== 开始执行 {}.{}=====",joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        //开始记录时间
        long start=System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //结束记录时间
        long end=System.currentTimeMillis();
        long takeTime=end-start;
        if(takeTime>3000){
            log.error("===== 执行结束，耗时：{}毫秒 =====",takeTime);
        }else if(takeTime>2000){
            log.warn("===== 执行结束，耗时：{}毫秒 =====",takeTime);
        }else{
            log.info("===== 执行结束，耗时：{}毫秒 =====",takeTime);
        }
        return result;
    }
}
