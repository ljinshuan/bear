package com.taobao.brand.bear.stream;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author jinshuan.li 19/01/2018 16:46
 */
@Slf4j
public class RxJavaFlowableTest {

    public List<Dog> datas = Lists.newArrayList();



    private Consumer<Object> logConsumer = dog -> log.debug(dog.toString());

    @Before
    public void before() {
        datas.add(new Dog("ljinshuan", 26));
        datas.add(new Dog("aoguilin", 24));

    }


    @Test
    public void test(){

        Flowable.fromIterable(datas);
    }
}
