package com.taobao.brand.bear.controller;

import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.service.DogService;
import com.taobao.brand.bear.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/")
@Slf4j
public class HomeController {

    public HomeController(){
        log.info("home");
    }
    @Resource
    private HelloService helloServie;

    @Resource
    private DogService dogService;

    @GetMapping("/home")
    public String home() {
        return helloServie.sayHello("ljinshuan");
    }

    @GetMapping("/dog/new")
    public Dog newDog(@RequestParam String name, @RequestParam Integer age) {

        Dog ljinshuan = dogService.getDog("ljinshuan");

        log.info(ljinshuan.toString());
        return dogService.getDog("ljinshuan");
    }

    @GetMapping("/dog/create")
    public Dog newDog2() {

        Dog dog = dogService.createDog("ljinshuan", 1111);

        return dog;
    }

}
