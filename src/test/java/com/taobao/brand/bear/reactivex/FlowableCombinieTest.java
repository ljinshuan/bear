package com.taobao.brand.bear.reactivex;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 01/04/2018 17:17
 */
@Slf4j
public class FlowableCombinieTest {

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

    // and then when 在rxjava标准版本中未实现
    @Test
    public void zipTest() {

        Flowable.range(0, 10).zipWith(datas, (integer, dog) -> Pair.of(integer, dog))
            .subscribe(logConsumer);
    }

    @Test
    public void combineLatestTest() {

        // TODO: 01/04/2018 N个都使用当前最新的值来聚合

    }


    @Test
    public void joinTest(){

        // TODO: 01/04/2018 join  groupJoin
    }



}
