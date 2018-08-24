package com.taobao.brand.bear.jctools;

import org.jctools.queues.MpscUnboundedArrayQueue;
import org.jctools.queues.atomic.MpscAtomicArrayQueue;
import org.jctools.queues.atomic.MpscLinkedAtomicQueue;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/8/24 07:06
 */
public class QueueTest {

    PriorityBlockingQueue<Long> priorityBlockingQueue = new PriorityBlockingQueue<>(100);

    @Test
    public void test() throws InterruptedException {

        priorityBlockingQueue.offer(2L);

        priorityBlockingQueue.offer(1L);

        // 没数据就一直等
        Long poll = priorityBlockingQueue.take();

        System.out.println(poll);

    }
}
