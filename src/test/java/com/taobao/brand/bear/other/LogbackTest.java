package com.taobao.brand.bear.other;

import com.taobao.brand.bear.utils.ThreadUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinshuan.li 23/01/2018 19:12
 */
public class LogbackTest {

    private Logger logger = null;

    @Test
    public void test() {
        logger = LoggerFactory.getLogger(LogbackTest.class);

        while (true) {
            ThreadUtils.sleep(300);
            logger.info("saaaaa" + System.currentTimeMillis());
        }


    }
}
