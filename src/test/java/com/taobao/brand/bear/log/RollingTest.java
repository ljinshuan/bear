package com.taobao.brand.bear.log;

import com.google.common.hash.Hashing;
import com.taobao.brand.bear.utils.ThreadUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinshuan.li 2018/10/25 09:56
 */
public class RollingTest {

    private static Logger logger = LoggerFactory.getLogger(RollingTest.class);

    @Test
    public void rolling() {

        while (true) {

            long now = System.currentTimeMillis();
            logger.info("rolling {} {}", now, Hashing.sha256().hashLong(now).toString());

            //ThreadUtils.sleep(30);
        }
    }
}
