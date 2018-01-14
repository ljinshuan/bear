package com.taobao.brand.bear.stream;

import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.service.DogService;
import com.taobao.brand.bear.utils.ThreadUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Future;

/**
 * @author jinshuan.li 2018/1/14 09:06
 */
@Slf4j
public class RxSchedulersTest {

    private DogService dogService = new DogService();

    private Consumer<Object> logConsumer = dog -> log.debug(dog.toString());

    private Dog dog = new Dog("ljinshuan", 3333);

    @Test
    public void test() {

        Future<Dog> dogFuture = ThreadUtils.runAsync(() -> {

            ThreadUtils.sleep(5000L);
            log.info("get future");
            return dog;
        });
        Observable.fromFuture(dogFuture).observeOn(Schedulers.computation()).subscribe(
            logConsumer);

        // observeOn 指定subscribe里代码发生的线程
        ThreadUtils.sleep(100000L);
    }

    @Test
    public void test0() {

        Observable.create((ObservableOnSubscribe<Long>)emitter -> {
            log.info("onNext before");
            emitter.onNext(123L);
            log.info("onNext after");
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).map(data -> {
            log.info("map 1", data);
            return data;
        }).observeOn(Schedulers.newThread()).doOnSubscribe(disposable -> log.info("doOnSubscribe")).subscribe(
            logConsumer);

        System.out.println("end");
        ThreadUtils.sleep(100000L);
    }

    @Test
    public void test1() {

        Observable.just(1L, 2L).observeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).map(data -> {
            log.info(data.toString());
            return data + 1;
        }).observeOn(Schedulers.computation()).subscribe(logConsumer);

        System.out.println("end");
        ThreadUtils.sleep(100000L);
    }

    @Test
    public void test2() {

        Observable.just(1L, 2L).observeOn(Schedulers.newThread()).map(data -> {
            log.info(data.toString());
            return data + 1;
        }).observeOn(Schedulers.newThread()).map(data -> {

            log.info("map2 {}", data);
            return data;
        }).observeOn(Schedulers.newThread())
            .observeOn(Schedulers.computation()).subscribe(logConsumer);

        System.out.println("end");
        //observeOn  指定下一次变换发生的线程 最后一次有效
        ThreadUtils.sleep(100000L);
    }
}
