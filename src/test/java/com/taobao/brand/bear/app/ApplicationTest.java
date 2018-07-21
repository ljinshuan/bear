package com.taobao.brand.bear.app;

import com.taobao.brand.bear.BearAppBaseTest;
import com.taobao.brand.bear.service.DogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;

import javax.annotation.Resource;

/**
 * @author jinshuan.li 2018/7/15 11:30
 */
@Slf4j
public class ApplicationTest extends BearAppBaseTest {

    @Resource
    private DogService dogService;

    @Test
    public void test() {

        MDC.put("s_name", "ljinshuan");
        log.error("xxx");

        System.out.println("xxxx");
    }

}
