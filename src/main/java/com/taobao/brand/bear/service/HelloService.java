package com.taobao.brand.bear.service;

import com.taobao.brand.bear.domain.Dog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@Slf4j
public class HelloService {

    @Resource
    private DogService dogService;

    public String sayHello(String name){

        return "hello "+name;
    }

    @PostConstruct
    public void init(){

    }
}
