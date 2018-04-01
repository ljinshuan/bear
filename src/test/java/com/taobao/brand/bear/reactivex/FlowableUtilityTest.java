package com.taobao.brand.bear.reactivex;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 01/04/2018 17:42
 */
@Slf4j
public class FlowableUtilityTest {

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
    public void delayTest() throws InterruptedException {

        // 延迟一段时间之后开始发射
        Flowable.range(0, 10).delay(1, TimeUnit.SECONDS).subscribe(logConsumer);

        countDownLatch.await();

    }

    @Test
    public void materiableTest() {

        Flowable.range(0, 10).materialize().subscribe(d -> {

            Integer value = d.getValue();

            System.out.println(value);
        });

    }

    @Test
    public void timeIntervalTest() {

        // 计算时间
        Flowable.range(0, 10).serialize().timeInterval().subscribe(logConsumer);
    }

}
