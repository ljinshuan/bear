package com.taobao.brand.bear.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jinshuan.li 2017/12/24 09:28
 */

@Slf4j
@RestController
@RequestMapping("/graphql")
public class GraphQLController {

    @GetMapping("/test")
    public JSONObject graphqlQuery(@RequestParam String query) {

        JSONObject data = new JSONObject();
        data.put("name", "ljinshuan");
        return data;
    }


    @GetMapping("/")
    public JSONObject graphqlQuery2() {


        JSONObject data = new JSONObject();
        data.put("query", "fff");
        return data;
    }
}
