package com.taobao.brand.bear.feature.aop;

import com.google.common.base.Stopwatch;
import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.service.DogService;
import com.taobao.brand.bear.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/9/4 20:05
 */

@Aspect
@Slf4j
@Repository
public class DogServiceAspect {

    @Pointcut("target(com.taobao.brand.bear.service.DogService)")
    private void flowProxy() {
    }

    @Pointcut("args(name,..,dog)")
    private void flowPassArgs(String name,Dog dog) {
    }

    @Before("flowProxy()  && flowPassArgs(name,dog)")
    public void beforeInvokeData(JoinPoint joinPoint, String name,Dog dog) {

        String singName = joinPoint.getSignature().getName();
        log.info("name:{} loginInfo:{}", name, dog);
        throw new IllegalArgumentException("xxx");


    }

}
