package com.taobao.brand.bear.reactivex;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author jinshuan.li 01/04/2018 16:28
 */
@Slf4j
public class FlowableTransformTest {

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
    public void bufferTest() {

        // 缓存一部分数据
        Disposable subscribe = Flowable.range(0, 100).buffer(10).subscribe(logConsumer);

    }

    @Test
    public void flatMapTest() {

        Flowable.range(0, 10).flatMap(d -> Flowable.range(0, d)).subscribe(logConsumer);
    }

    @Test
    public void groupByTest() {

      /*  Flowable.range(0, 100).groupBy(integer -> integer % 2 == 0).forEach(
            groupedFlowable -> groupedFlowable.buffer(100).subscribe(logConsumer));
*/
        Flowable.range(0, 100).groupBy(i -> i % 2 == 0).subscribe(
            groupedFlowable -> groupedFlowable.buffer(100).subscribe(logConsumer));
    }

    @Test
    public void scanTest() {

        // 对每个元素应用一个变换  a是之前返回的值 b是当前每个元素
        Flowable.range(0, 10).scan((integer, integer2) -> {
            log.info("a:{} b:{}", integer, integer2);
            return integer + integer2;
        }).subscribe(logConsumer);
    }

    @Test
    public void windowTest() {

        // TODO: 01/04/2018 windiow
    }

    @Test
    public void zipTest() {

        BiFunction zipper = (a, b) -> Pair.of(a, b);
        Maybe.just(new Dog("ljinshuan", 26)).zipWith(Maybe.just(1L), zipper);
    }

}
