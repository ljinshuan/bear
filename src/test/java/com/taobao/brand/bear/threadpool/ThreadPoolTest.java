package com.taobao.brand.bear.threadpool;

import com.taobao.brand.bear.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jinshuan.li 23/01/2018 09:53
 */
@Slf4j
public class ThreadPoolTest {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 创建线程池
     *
     * @param nThreads
     * @param threadName
     * @return
     */
    public static ExecutorService createThreadPool(int nThreads, String threadName) {

        /**
         * corePoolSize  线程池里的常驻线程数
         * maximumPoolSize  最大线程数
         * keepAliveTime 如果 maximumPoolSize>corePoolSize  超过这个时间 多余的线程数将被关闭
         *
         * 1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。
         2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行
         3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务
         4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理
         5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程
         6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭
         *
         */
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
                log.info("create thread {}", name);
                return new Thread(r, name);
            }
        });

        return executorService;

    }

    @Test
    public void test() throws InterruptedException {

        String threadName = "testThread";
        ExecutorService executorService = createPools(1, 2, 5, Integer.MAX_VALUE, threadName);
        // 第一个会立即执行 第二个需要等第一个执行完毕之后再执行 因为队列未满 不需要创建创建最大线程
        executorService.submit(() -> {
            log.info("start in thread ");
            ThreadUtils.sleep(6000);
        });

        executorService.submit(() -> {
            log.info("start in thread ");
            ThreadUtils.sleep(6000);
        });

        countDownLatch.await();

    }

    @Test
    public void test1() throws InterruptedException {

        String threadName = "testThread";
        // 第一个会立即执行 第二个需要等第一个执行完毕之后再执行 因为队列未满 不需要创建创建最大线程 (第一个直接执行 不入队列)
        ExecutorService executorService = createPools(1, 2, 5, 1, threadName);
        executorService.submit(() -> {
            log.info("start in thread ");
            ThreadUtils.sleep(6000);
        });

        executorService.submit(() -> {
            log.info("start in thread ");
            ThreadUtils.sleep(6000);
        });

        countDownLatch.await();
    }

    @Test
    public void test2() throws InterruptedException {

        String threadName = "testThread";
        // A 立即执行  B 入队 C 入队时队列满 创建现成 立即执行  A执行完毕之后 B执行
        // 顺序 createThread_1 -> startA -> createThread_2 -> startC -> startB
        ExecutorService executorService = createPools(1, 2, 5, 1, threadName);
        executorService.submit(() -> {
            log.info("start in thread test2 A");
            ThreadUtils.sleep(7000);
        });

        executorService.submit(() -> {
            log.info("start in thread test2 B ");
            ThreadUtils.sleep(7000);
        });

        executorService.submit(() -> {
            log.info("start in thread test2 C ");
            ThreadUtils.sleep(7000);
        });

        countDownLatch.await();
    }

    @Test
    public void test3() throws InterruptedException {

        String threadName = "testThread";

        /**
         *
         */
        /**
         * 10:36:50.594 [main] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - create thread testThread_1
         10:36:50.598 [testThread_1] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - start in thread test3 A
         10:36:50.599 [main] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - create thread testThread_2
         10:36:50.599 [testThread_2] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - start in thread test3 C
         10:36:53.603 [testThread_2] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - start in thread test3 B
         10:37:00.607 [testThread_2] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - start in thread test3 D
         10:37:00.609 [main] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - create thread testThread_3
         10:37:00.610 [testThread_3] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - start in thread test3 F
         10:37:07.609 [testThread_2] INFO com.taobao.brand.bear.threadpool.ThreadPoolTest - start in thread test3 E
         */
        ExecutorService executorService = createPools(1, 2, 1, 1, threadName);
        executorService.submit(() -> {
            log.info("start in thread test3 A");
            ThreadUtils.sleep(3000);
        });

        executorService.submit(() -> {
            log.info("start in thread test3 B ");
            ThreadUtils.sleep(3000);
        });

        executorService.submit(() -> {
            log.info("start in thread test3 C ");
            ThreadUtils.sleep(3000);
        });

        // 提交三个任务 保证有两个线程创建

        ThreadUtils.sleep(10000);

        executorService.submit(() -> {
            log.info("start in thread test3 D");
            ThreadUtils.sleep(7000);
        });

        executorService.submit(() -> {
            log.info("start in thread test3 E ");
            ThreadUtils.sleep(7000);
        });

        executorService.submit(() -> {
            log.info("start in thread test3 F ");
            ThreadUtils.sleep(7000);
        });

        countDownLatch.await();
    }

    @Test
    public void test4() throws InterruptedException {

        ExecutorService executorService = createPools(1, 2, 1, 1, "threadName");
        Future<Integer> submit = executorService.submit(() -> {

            try {
                while (true) {
                    log.info("run in thread  A {}", System.currentTimeMillis());
                    log.info("interupt thread . {} ", Thread.currentThread().isInterrupted());
                    ThreadUtils.sleep(2000);
                }
            } catch (Exception e) {

                log.error(e.getMessage(), e);
            }
            return 1;

        });

        ThreadUtils.sleep(5000);

        boolean cancel = submit.cancel(true);

        log.info(submit.toString());
        log.info(String.valueOf(cancel));

     /*   submit = executorService.submit(() -> {

            while (true) {
                log.info("run in thread  B {}", System.currentTimeMillis());
                ThreadUtils.sleep(2000);
            }

        });
        executorService.submit(() -> {

            while (true) {
                log.info("run in thread  C {}", System.currentTimeMillis());
                ThreadUtils.sleep(2000);
            }

        });*/

        countDownLatch.await();
    }
}
