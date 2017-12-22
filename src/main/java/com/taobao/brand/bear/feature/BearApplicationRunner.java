package com.taobao.brand.bear.feature;

import com.taobao.brand.bear.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author jinshuan.li 2017/12/22 23:13
 */
@Component
@Slf4j
public class BearApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        log.info(StringHelper.toJsonString(applicationArguments));
    }
}
