package com.taobao.brand.bear.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jinshuan.li on 2017/8/19 15:47.
 */
public class ThreadPoolUtils {

    /**
     * 创建线程池
     * @param nThreads
     * @param threadName
     * @return
     */
    public static ExecutorService createThreadPool(int nThreads, String threadName) {

        ExecutorService executorService = new ThreadPoolExecutor(nThreads, 300, 5, TimeUnit.MINUTES, new
            LinkedBlockingQueue<>(), new ThreadFactory() {

            private AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName + "_" + count.incrementAndGet());
            }
        });
        return executorService;
    }
}
