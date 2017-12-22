package com.taobao.brand.bear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.taobao.brand"})
@PropertySource({"classpath:application.properties"})
public class BearApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BearApplication.class, args);

    }
}
