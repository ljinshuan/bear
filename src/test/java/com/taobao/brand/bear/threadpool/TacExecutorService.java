package com.taobao.brand.bear.threadpool;

import java.util.concurrent.Callable;

/**
 * @author jinshuan.li 23/01/2018 14:02
 */
public interface TacExecutorService {

    /**
     * @param task
     * @param <T>
     * @return
     */
    <T> TacFuture<T> submit(Callable<T> task);

    /**
     * @param task
     * @param <T>
     * @return
     */
    <T> TacFuture<T> submit(Callable<T> task, Long timeOut);

    /**
     * @param task
     * @return
     */
    TacFuture<?> submit(Runnable task);
}
