package com.taobao.brand.bear.log;

import com.google.common.hash.Hashing;
import com.taobao.brand.bear.utils.ThreadUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author jinshuan.li 2018/10/25 09:56
 */
public class RollingTest {

    private static Logger logger = LoggerFactory.getLogger(RollingTest.class);

    private CountDownLatch countDownLatch=new CountDownLatch(1);
    @Test
    public void rolling() throws InterruptedException {


        for (int i=0;i<10;i++){
            ThreadUtils.runAsync(()->{
                while (true) {

                    long now = System.currentTimeMillis();
                    logger.info("rolling {} {}", now, Hashing.sha256().hashLong(now).toString());
                    //ThreadUtils.sleep(30);
                }
            });
        }


        countDownLatch.await();


    }
}
