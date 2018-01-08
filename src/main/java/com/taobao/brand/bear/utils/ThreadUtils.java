package com.taobao.brand.bear.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author jinshuan.li
 */
public class ThreadUtils {

    public static ExecutorService executorService = ThreadPoolUtils.createThreadPool(70, "batch_process");

    /**
     * 多线程执行任务并且等待完成
     *
     * @param tasks
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static <T> List<T> runWaitCompleteTask(List<Callable<T>> tasks, Long timeout, TimeUnit timeUnit)
        throws Exception {

        if (CollectionUtils.isEmpty(tasks)) {
            return new ArrayList<T>();
        }

        CompletionService<T> completionService = new ExecutorCompletionService<T>(executorService);

        for (Callable<T> runnable : tasks) {
            completionService.submit(runnable);

        }

        Future<T> resultFuture = null;

        List<T> result = new ArrayList<T>();

        for (int i = 0; i < tasks.size(); i++) {
            resultFuture = completionService.take();
            result.add(resultFuture.get(timeout, timeUnit));
        }
        return result;

    }

    public static <T> Future<T> runAsync(Callable<T> task) {
        return executorService.submit(task);
    }

    public static <T> Future<T> runAsync(ExecutorService executor, Callable<T> task) {
        return executor.submit(task);
    }

    public static void sleep(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }
}
