package com.taobao.brand.bear.feature.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Repository;

/**
 * @author jinshuan.li 2017/12/23 12:30
 */

@Aspect
@Slf4j
@Repository
public class LogAspect {

    @Pointcut("execution(* com.taobao.brand.bear.service.*.create*(..))")
    private void anyCreate() {
    }

    @Before("anyCreate()")
    public void doLog() {
        log.info("doLog");
    }
}
