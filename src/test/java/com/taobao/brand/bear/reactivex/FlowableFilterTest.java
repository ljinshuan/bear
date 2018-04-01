package com.taobao.brand.bear.reactivex;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 01/04/2018 16:55
 */
@Slf4j
public class FlowableFilterTest {

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
    public void debounceTest() throws InterruptedException {

        // 只出超过100ms没有发射的数据
        Flowable.interval(500, TimeUnit.MILLISECONDS).filter(s -> new Random().nextInt(100) > 50).debounce(100,
            TimeUnit.MILLISECONDS).subscribe(logConsumer);

        countDownLatch.await();
    }

    @Test
    public void distinceTest() {

        // 去重
        Flowable.range(0, 100).map(s -> new Random().nextInt(10)).distinct().subscribe(logConsumer);
    }

    @Test
    public void elementAtTest() {

        Flowable.range(0, 100).elementAt(101).subscribe(logConsumer);
    }

    @Test
    public void firstTest() {

        Integer integer = Flowable.range(0, 10).firstOrError().blockingGet();

    }

    @Test
    public void ignoreElementsTest() {

        Completable completable = Flowable.range(0, 100).ignoreElements();
    }

    @Test
    public void lastTest() {

        Flowable.range(0, 10);
    }



}
