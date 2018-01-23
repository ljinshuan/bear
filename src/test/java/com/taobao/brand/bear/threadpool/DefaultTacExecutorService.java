package com.taobao.brand.bear.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jinshuan.li 23/01/2018 14:06
 */
public class DefaultTacExecutorService implements TacExecutorService {

    /**
     * 内部线程池
     */
    private ExecutorService executorService = createPools(50, 200, 60, 500, "TAC_INNER_POOL_");

    @Override
    public <T> TacFuture<T> submit(Callable<T> task) {

        return submit(task, null);
    }

    @Override
    public <T> TacFuture<T> submit(Callable<T> task, Long timeOut) {

        // 获取当前线程ThreadLocal
        Future<T> submit = executorService.submit(() -> {

            try {
                return task.call();
            } finally {
                // 清除数据
            }
        });

        return new TacFuture<T>(submit, timeOut);
    }

    @Override
    public TacFuture<?> submit(Runnable task) {

        Future<?> submit = executorService.submit(() -> {
            try {
                task.run();
                return 1;
            } finally {

            }
        });

        return new TacFuture<>(submit, null);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param queueSize
     * @param threadName
     * @return
     */
    public static ExecutorService createPools(int corePoolSize,
                                              int maximumPoolSize,
                                              long keepAliveTime, int queueSize, String threadName) {

        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
            TimeUnit.SECONDS, new
            LinkedBlockingQueue<>(queueSize), new ThreadFactory() {

            private AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                String name = threadName + "_" + count.incrementAndGet();
                return new Thread(r, name);
            }
        });

        return executorService;

    }
}
