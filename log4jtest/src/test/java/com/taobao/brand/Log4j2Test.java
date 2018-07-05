package com.taobao.brand;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinshuan.li 2018/7/4 10:49
 */
public class Log4j2Test {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2Test.class.getName());

    @Test
    public void test() throws InterruptedException {

        while (true) {
            logger.info("hahaa {}", System.currentTimeMillis());

            Thread.sleep(500L);
        }

    }
}
