package com.taobao.brand.bear.utils;

/**
 * @author jinshuan.li 2017/12/23 10:25
 */
public class ThreadUtils {

    /**
     *
     * @param sleepTimeMs
     */
    public static void sleep(Long sleepTimeMs) {

        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {

        }

    }
}
