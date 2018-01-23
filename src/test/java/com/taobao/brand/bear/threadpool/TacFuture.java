package com.taobao.brand.bear.threadpool;

import lombok.Data;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jinshuan.li 23/01/2018 14:20
 */

@Data
public class TacFuture<V> {

    private static final Long defaultTimeOut = 3000L;

    /**
     * 真实future
     */
    private Future<V> future;

    /**
     * 超时时间
     */
    private Long timeOut;

    public TacFuture(Future<V> future) {

        this.future = future;
        this.timeOut = defaultTimeOut;
    }

    public TacFuture(Future<V> future, Long timeOut) {

        this.future = future;
        this.timeOut = timeOut == null ? defaultTimeOut : timeOut;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isDone() {
        return future.isDone();
    }

    /**
     * 获取数据
     *
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public V get() throws InterruptedException, ExecutionException, TimeoutException {

        return future.get(timeOut, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取数据
     *
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        return future.get(timeout, unit);
    }
}
