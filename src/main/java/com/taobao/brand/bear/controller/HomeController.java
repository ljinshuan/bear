package com.taobao.brand.bear.controller;

import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.service.DogService;
import com.taobao.brand.bear.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/")
public class HomeController {

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


        return dogService.creaetDog(name, age);
    }

}
