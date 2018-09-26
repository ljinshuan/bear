package com.taobao.brand;

import com.taobao.brand.log4j2ext.ThreadLocals;
import org.apache.logging.log4j.ThreadContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinshuan.li 2018/7/4 10:49
 */
public class Log4j2Test {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2Test.class);

    @Test
    public void test() throws InterruptedException {

        ThreadLocals.setMsCode("other");

        while (true) {

            logger.info("hahaa {}", System.currentTimeMillis());

            Thread.sleep(50);
        }

    }
}