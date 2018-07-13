package com.taobao.brand.bear.reactivex;

import com.taobao.brand.bear.domain.Dog;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author jinshuan.li 2018/7/11 19:29
 */
public class FlowableliftTest {

    private Maybe<Dog> dogMaybe = Maybe.just(new Dog("ljinshuan", 26));

    private Maybe<Long> longMaybe = Maybe.just(1L);

    private Maybe<Integer> integerMaybe = Maybe.just(2);

    @Test
    public void zipTest() {

        Maybe.zip(dogMaybe, longMaybe, integerMaybe, (a, b, c) -> {

            return a;

        }).onErrorComplete(throwable -> {
            // 吃掉异常
            return true;
        }).subscribe(a -> System.out.println(a));

        dogMaybe.subscribe(s -> System.out.println(s));
    }

    @Test
    public void liftTest() {

        Flowable.rangeLong(0, 10).lift(new FlowableOperator<String, Long>() {
            @Override
            public Subscriber<? super Long> apply(Subscriber<? super String> observer) throws Exception {
                return new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                        observer.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        observer.onNext(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable t) {
                        observer.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                };
            }
        }).subscribe(c -> {

            System.out.println(c);
        });
    }

    @Test
    public void test3() {

        Flowable.error(new IllegalArgumentException()).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                System.out.println(Thread.currentThread().getName());
            }
        }).subscribe();

        System.out.println(1);
    }

    @Test
    public void testGroupBy() {

        Flowable.rangeLong(0L, 30L).groupBy(d -> String.valueOf(d % 3)).subscribe(c -> System.out.println(c));
    }

}
