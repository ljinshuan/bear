package com.taobao.brand.bear.service;

import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.properties.UserProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author jinshuan.li 2017/12/22 08:14
 */
@Service
@Slf4j
public class DogService {


    @Resource
    private UserProperties userProperties;

    @PostConstruct
    public void init() {
        log.info(userProperties.toString());

    }

    /**
     * @param name
     * @param age
     * @return
     */
    public Dog creaetDog(String name, Integer age) {

        return Dog.builder().age(age).name(name).build();
    }
}
