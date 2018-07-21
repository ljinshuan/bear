package com.taobao.brand.bear;

import com.taobao.brand.bear.service.DogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.taobao.brand"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BearApplicationTests {


}
