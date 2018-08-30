package com.taobao.brand.bear.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author jinshuan.li 2018/8/22 10:52
 */
@Component
@Scope(value = "prototype")
public class TQLFacade {

    private String name;

    public TQLFacade(String name) {
        this.name = name;
    }

    public TQLFacade() {

    }
}
