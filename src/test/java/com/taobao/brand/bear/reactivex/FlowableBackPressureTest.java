package com.taobao.brand.bear.reactivex;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.utils.ThreadUtils;
import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 02/04/2018 09:41
 */
@Slf4j
public class FlowableBackPressureTest {

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
    public void test() throws InterruptedException {

        Flowable.interval(1000L, TimeUnit.MILLISECONDS).observeOn(Schedulers.newThread()).subscribe(s -> {

            ThreadUtils.sleep(100);

            log.info(s.toString());
        });

        countDownLatch.await();
    }

    @Test
    public void testPress() throws InterruptedException {

        // 支持背压
        Flowable.interval(5L, TimeUnit.MILLISECONDS).onBackpressureBuffer(1024, () -> System.out.println("over flow"),
            BackpressureOverflowStrategy.DROP_LATEST)
            .observeOn(Schedulers.newThread()).subscribe(
            new Subscriber<Long>() {

                private Subscription subscription;

                @Override
                public void onSubscribe(Subscription s) {
                    subscription = s;
                    s.request(1);
                }

                @Override
                public void onNext(Long aLong) {

                    log.info(aLong.toString());
                    ThreadUtils.sleep(100);
                    subscription.request(1);
                }

                @Override
                public void onError(Throwable t) {

                    t.printStackTrace();
                }

                @Override
                public void onComplete() {

                    System.out.println("end");
                }
            });

        countDownLatch.await();
    }

    Subscriber<Long> pressSub = new Subscriber<Long>() {

        private Subscription subscription;

        @Override
        public void onSubscribe(Subscription s) {
            subscription = s;
            s.request(1);
        }

        @Override
        public void onNext(Long aLong) {

            log.info(aLong.toString());
            ThreadUtils.sleep(1000);
            subscription.request(1);
        }

        @Override
        public void onError(Throwable t) {

            t.printStackTrace();
        }

        @Override
        public void onComplete() {

            System.out.println("end");
        }
    };

    @Test
    public void testPress2() throws InterruptedException {

        Flowable.interval(5, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.newThread()).subscribe(pressSub);

        System.out.println("subscribe");
        countDownLatch.await();

    }

    @Test
    public void testPress3() throws InterruptedException {

        Flowable.create((FlowableOnSubscribe<Long>)emitter -> {

            while (true) {

                emitter.onNext(System.currentTimeMillis());
            }
        }, BackpressureStrategy.ERROR).observeOn(Schedulers.newThread()).subscribe(pressSub);

        System.out.println("subscribe");
        countDownLatch.await();

    }

}
