package com.taobao.brand.bear.stream;

import com.sun.activation.registries.LogSupport;
import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.service.DogService;
import com.taobao.brand.bear.utils.ThreadUtils;
import io.reactivex.*;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/1/7 12:51
 */
@Slf4j
public class RxJavaObservableTest {

    private DogService dogService = new DogService();

    private Consumer<Object> logConsumer = dog -> log.debug(dog.toString());

    private Dog dog = new Dog("ljinshuan", 3333);

    @Test
    public void test() {

        Observable.create((ObservableOnSubscribe<Dog>)emitter -> {

            Dog ljinshuan = dogService.getDog("ljinshuan");

            emitter.onNext(ljinshuan);
            emitter.onComplete();
        });

        // 此处会阻塞

        System.out.println("end");
    }

    @Test
    public void test1() {

        /**
         * 有订阅时才创建
         */
        Observable.defer(() -> Observable.just(dogService.getDog("xx")));
    }

    @Test
    public void test2() {

        Observable.fromCallable(() -> new Dog("ljinshuan", 23)).subscribe(logConsumer);
    }

    @Test
    public void test3() {

        Future<Dog> dogFuture = ThreadUtils.runAsync(() -> {

            ThreadUtils.sleep(5000L);
            log.info("get future");
            return dog;
        });
        Observable.fromFuture(dogFuture).subscribe(logConsumer);
    }

    @Test
    public void test4() {

        /**
         * 每隔一段时间发射一个序列
         */
        Observable.interval(2, TimeUnit.SECONDS).subscribe(logConsumer);

        ThreadUtils.sleep(100000L);
    }

    @Test
    public void test5() {

        Observable.range(0, 100).subscribe(logConsumer);
    }

    @Test
    public void test6() {

        Observable.timer(1, TimeUnit.SECONDS).subscribe(logConsumer);

        ThreadUtils.sleep(100000L);
    }

}
