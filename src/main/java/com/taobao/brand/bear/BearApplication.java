package com.taobao.brand.bear;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.taobao.brand"})
@PropertySource({"classpath:application.properties"})
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BearApplication {

    public static void main(String[] args) {

        String name = Thread.currentThread().getName();
        log.info("hello " + name);
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BearApplication.class, args);

    }
}
