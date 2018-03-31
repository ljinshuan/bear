package com.taobao.brand.bear.stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableBuffer;
import io.reactivex.internal.operators.flowable.FlowableCollectSingle;
import io.reactivex.internal.operators.flowable.FlowableFromCallable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author jinshuan.li 19/01/2018 16:46
 */
@Slf4j
public class RxJavaFlowableTest {

    public List<Dog> datas = Lists.newArrayList();

    private Consumer<Object> logConsumer = dog -> log.debug(dog.toString());

    Map<String, Dog> maps = Maps.newHashMap();

    @Before
    public void before() {
        datas.add(new Dog("ljinshuan", 26));
        datas.add(new Dog("aoguilin", 24));

        maps.put("ljinshuan", datas.get(0));

    }

    @Test
    public void test() {


    }
}
