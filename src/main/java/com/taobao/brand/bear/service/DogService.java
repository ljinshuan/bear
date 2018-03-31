package com.taobao.brand.bear.service;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.properties.UserProperties;
import com.taobao.brand.bear.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinshuan.li 2017/12/22 08:14
 */
@Service
@Slf4j
public class DogService implements BeanNameAware {

    @Resource
    private UserProperties userProperties;

    private List<Dog> allDogs = Lists.newArrayList();

    public DogService() {

        this.allDogs.add(Dog.builder().age(26).name("Ljinshuan").build());
        this.allDogs.add(Dog.builder().age(24).name("Louise").build());
    }

    @PostConstruct
    public void init() {

        System.out.println("dd");
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
        ThreadUtils.sleep(100L);
        log.info("get from db");
        return createDog(name, 20);
    }

    @Override
    public void setBeanName(String name) {
        // 实现了BeanNameAware 接口 如果被初始化 会调用setBeanName方法
        //log.info(name);
    }

    /**
     * 获取所有的dog
     *
     * @return
     */
    public List<Dog> getAllDogs() {

        return this.allDogs;
    }

}
