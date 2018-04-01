package com.taobao.brand.bear.reactivex;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 01/04/2018 15:28
 */
@Slf4j
public class FlowableCreateTest {

    public List<Dog> datas = Lists.newArrayList();

    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void before() {

        datas.add(new Dog("ljinshuan", 26));
        datas.add(new Dog("aoguilin", 24));
    }

    private Consumer logConsumer = s -> {
        log.info(s.toString());
    };

    @Test
    public void normalTest() {

        Flowable<Dog> dogFlowable = Flowable.fromIterable(datas);

        // 一个流可以被重复消费
        dogFlowable.subscribe(logConsumer);

        dogFlowable.subscribe(logConsumer);
    }

    @Test
    public void createTest() {

        Flowable<Dog> dogFlow = Flowable.create(emitter -> emitter.onError(new IllegalArgumentException("not data")),
            BackpressureStrategy.MISSING);
        try {

            dogFlow.doOnError(s -> {
                System.out.println("ff");
            }).subscribe(s -> {

                System.out.println(s);
            }, error -> {
                // 需要单独额外提供一个 error处理
                System.out.println(error);
            });
        } catch (Throwable t) {

            log.error(t.getMessage(), t);
        }

        log.info("end");

        dogFlow.subscribe();
    }

    @Test
    public void deferTest() {

        Flowable<Dog> dogFlowable = Flowable.defer(() -> {
            System.out.println("create publisher");
            return s -> s.onNext(new Dog("ljinshuan", 20));
        });

        dogFlowable.subscribe(logConsumer);
        dogFlowable.subscribe(logConsumer);
    }

    @Test
    public void emptyTest() {

        // 创建一个空的流
        Flowable<Dog> empty = Flowable.empty();

        log.info("size:{}", empty.count().blockingGet());
    }

    @Test
    public void neverTest() {

        Flowable<Dog> never = Flowable.never();

        // 代码不会走到这里
        log.info("size:{}", never.count().blockingGet());
    }

    @Test
    public void errorTest() {

        Flowable.error(new IllegalStateException("no data")).subscribe(logConsumer, logConsumer);
    }

    @Test
    public void intervalTest() throws InterruptedException {

        Flowable<Long> interval = Flowable.interval(1000, TimeUnit.MILLISECONDS);

        // 以下不在主线程中执行
        interval.subscribe(logConsumer);

        // 需要等待
        countDownLatch.await();

    }

    @Test
    public void rangeTest() throws InterruptedException {

        Disposable subscribe =
            Flowable.rangeLong(0, 10).subscribe(logConsumer);

        countDownLatch.await();
    }

    @Test
    public void repeatTest() {

        // 重复N次
        Flowable.range(0, 2).filter(s -> s > 0).repeat(2).subscribe(logConsumer);

    }

    @Test
    public void startTest() {

        // 首先emit一个值
        Flowable.range(0, 2).startWith(3).subscribe(logConsumer);
    }

    @Test
    public void timerTest() throws InterruptedException {

        // 延迟delta 时间之后触发
        Flowable.timer(1, TimeUnit.SECONDS).repeat(10).subscribe(logConsumer);

        countDownLatch.await();
    }



}
