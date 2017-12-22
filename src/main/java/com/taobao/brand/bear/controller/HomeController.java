package com.taobao.brand.bear.controller;

import com.taobao.brand.bear.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/")
public class HomeController {


    @Resource
    private HelloService helloServie;
    @GetMapping("/home")
    public String home(){
        return helloServie.sayHello("ljinshuan");
    }


}
