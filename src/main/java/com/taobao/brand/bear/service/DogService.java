package com.taobao.brand.bear.service;

import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.properties.UserProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author jinshuan.li 2017/12/22 08:14
 */
@Service
@Slf4j
public class DogService implements BeanNameAware {

    @Resource
    private UserProperties userProperties;

    @PostConstruct
    public void init() {
        log.info(userProperties.toString());
        //ThreadUtils.sleep(10000L);
    }

    /**
     * @param name
     * @param age
     * @return
     */
    public Dog createDog(String name, Integer age) {

        return Dog.builder().age(age).name(name).build();
    }

    @Cacheable("ddd")
    public Dog getDog(String name) {

        log.info("get from db");
        return createDog(name, 20);
    }

    @Override
    public void setBeanName(String name) {
        // 实现了BeanNameAware 接口 如果被初始化 会调用setBeanName方法
        //log.info(name);
    }

}
