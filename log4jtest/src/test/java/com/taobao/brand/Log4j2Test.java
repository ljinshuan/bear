package com.taobao.brand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * @author jinshuan.li 2018/7/4 10:49
 */
public class Log4j2Test {

    private static final Logger logger = LogManager.getLogger(Log4j2Test.class.getName());

    @Test
    public void test() {

        logger.info("xxx {}", 10);
    }
}
