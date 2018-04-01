package com.taobao.brand.bear.stream;

import com.taobao.brand.bear.utils.ThreadUtils;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.internal.operators.flowable.FlowableSingle;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/1/4 07:37
 */
@Slf4j
public class RxJavaTest {

    @Test
    public void testJust() {

        Observable.just(1, 2, 3).subscribe(integer -> System.out.println(integer));
    }

    @Test
    public void testInterval() {

        Observable.interval(3, 2, TimeUnit.SECONDS).subscribe(aLong -> {
            System.out.println(aLong);
        });

        ThreadUtils.sleep(100000L);
    }

    @Test
    public void testNostop() {

        Observable<Integer> naturalNumbers = Observable.create(subscriber -> {

            Integer data = 0;
            while (true) {

                subscriber.onNext(data++);
            }
        });

        naturalNumbers.subscribe(s -> System.out.println(s));

        System.out.println("xx");
    }

    @Test
    public void testNot() throws InterruptedException {

        Single<ArrayList<Object>> fs = Flowable.range(1, 10)
            .parallel(10)
            .runOn(Schedulers.io())
            .map(s -> {
                Thread.sleep(1000);
                return s;
            })
            .sequential()
            .collect(ArrayList::new, ArrayList::add)
            .doOnSuccess(l -> System.out.println(l));

        Flowable.range(1, 10)
            .parallel(10)
            .runOn(Schedulers.io())
            .map(s -> fs.subscribe())
            .sequential()
            .observeOn(Schedulers.trampoline()).blockingSubscribe();
        //.subscribe();

        Thread.sleep(1000000);
    }
}
