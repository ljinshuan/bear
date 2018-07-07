package com.taobao.brand.bear.stream;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.*;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

/**
 * @author jinshuan.li 2018/7/7 09:42
 */
@Slf4j
public class RxJavaOperatorTest2 {

    public List<Dog> datas = Lists.newArrayList();

    public Flowable<Dog> dogFlow;

    public Flowable<Long> longFlow = Flowable.rangeLong(0, 10);

    private Consumer<Object> logConsumer = dog -> log.debug(dog.toString());

    @Before
    public void before() {
        datas.add(new Dog("ljinshuan", 26));
        datas.add(new Dog("aoguilin", 24));

        dogFlow = Flowable.fromIterable(datas);
    }

    @Test
    public void test1() {

        Maybe<Dog> other = new Maybe<Dog>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super Dog> observer) {
                log.info("error");
                observer.onError(new IllegalStateException("xxx"));
            }
        };
        Dog dog = Maybe.just(new Dog("ljinshuan", 26)).filter(d -> d.getAge() > 200).
            switchIfEmpty(other).onErrorResumeNext(new Maybe<Dog>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super Dog> observer) {
                observer.onComplete();
            }
        }).blockingGet();
    }

    @Test
    public void test2() {

        longFlow.filter(d -> {

            if (d > 4) {
                throw new IllegalStateException("d>4");
            }
            return true;
        }).onErrorResumeNext(Flowable.just(11L, 22L, 33L)).subscribe(logConsumer);
    }
}
