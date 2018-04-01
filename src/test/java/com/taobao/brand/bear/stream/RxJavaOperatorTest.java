package com.taobao.brand.bear.stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.*;
import io.reactivex.schedulers.Timed;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/1/7 09:30
 */
@Slf4j
public class RxJavaOperatorTest {

    public List<Dog> datas = Lists.newArrayList();

    public Observable<Dog> dogObservable;

    private Consumer<Object> logConsumer = dog -> log.debug(dog.toString());

    @Before
    public void before() {
        datas.add(new Dog("ljinshuan", 26));
        datas.add(new Dog("aoguilin", 24));

        dogObservable = Observable.fromIterable(datas);
    }

    @Test
    public void test() {

        Observable.fromIterable(datas).subscribe(logConsumer);
    }

    @Test
    public void test1() {
        /**
         * all 所有的成立才处理
         */
        dogObservable.all(dog -> dog.getAge() > 10).subscribe(logConsumer);
    }

    @Test
    public void test2() {

        /**
         * any 其中一个成立
         */
        dogObservable.any(dog -> dog.getAge() > 25).subscribe(logConsumer);
    }

    @Test
    public void test3() {

        /**
         * as 将observable转化成其他类
         */
        Observable<Dog> as = dogObservable.as(upstream -> upstream);
    }

    @Test
    public void test4() {

        /**
         * amb 只处理第一个产生数据 error  complete的数据源
         */
        dogObservable.ambWith(dogObservable).subscribe(logConsumer);
    }

    @Test
    public void test5() {

        /**
         * 阻塞等待第一个对象
         */
        Dog dog = dogObservable.blockingFirst();

        System.out.println(dog);

        /**
         * 直到所有数据产生完之后才开始执行
         */
        dogObservable.blockingForEach(logConsumer);

        Iterable<Dog> dogs = dogObservable.blockingIterable(1);
        for (Dog dog1 : dogs) {
            System.out.println(dog1);
        }
    }

    @Test
    public void test6() {

        dogObservable.cast(String.class).subscribe(logConsumer);
    }

    @Test
    public void test7() {

        /**
         * 类似于reduce
         */
        dogObservable.collect(() -> new StringBuilder(), (o, dog) -> o.append(dog.toString())).subscribe(logConsumer);
    }

    @Test
    public void teset8() {

        /**
         *  转化 dogObservable
         */
        dogObservable.compose(upstream -> null);
    }

    @Test
    public void test9() {

        Observable<Object> objectObservable = dogObservable.concatMap(new Function<Dog, ObservableSource<?>>() {

            @Override
            public ObservableSource<?> apply(Dog dog) throws Exception {
                return null;
            }
        });
    }

    @Test
    public void test10() {

        /**
         * 合并另一条流
         */
        dogObservable.concatWith(dogObservable).subscribe(logConsumer);
    }

    @Test
    public void test11() {

        /**
         * 统计数量
         */
        dogObservable.count().subscribe(logConsumer);
    }

    @Test
    public void test12() {

        /**
         * 只发射该时间段的最后一个结果  比如ABC三个事件分别在 1 9  20 秒发生 则返回B和C
         */
        dogObservable.debounce(10, TimeUnit.SECONDS).subscribe(logConsumer);
    }

    @Test
    public void test13() throws InterruptedException {

        /**
         * 延迟x时间
         */
        dogObservable.delay(5, TimeUnit.SECONDS).subscribe(logConsumer);

        Thread.sleep(1000000);
    }

    @Test
    public void test14() throws InterruptedException {

        dogObservable.delaySubscription(5, TimeUnit.SECONDS).subscribe(logConsumer);

        Thread.sleep(1000000);
    }

    @Test
    public void test15() {

        Disposable end = this.dogObservable.doOnComplete(new Action() {

            @Override
            public void run() throws Exception {
                System.out.println("end");
            }
        }).subscribe(logConsumer);
    }

    @Test
    public void test16() {

        /**
         * 取第N个
         */
        this.dogObservable.elementAt(5).subscribe(logConsumer);
    }

    @Test
    public void test17() {
        /**
         * 同stream的flatMap
         */
        this.dogObservable.flatMap(new Function<Dog, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Dog dog) throws Exception {
                return null;
            }
        });
    }

    @Test
    public void test18() {

        /**
         * 直接订阅
         */
        this.dogObservable.forEach(logConsumer);
    }

    @Test
    public void test19() {

        /**
         * 可以控制中断的foreach
         */
        this.dogObservable.forEachWhile(dog -> false);
    }

    @Test
    public void test20() {

        /**
         * 合并 注意与concat的区别  concat需要等第一个完成之后才发射第二流的第一个元素
         */
        this.dogObservable.mergeWith(this.dogObservable).subscribe(logConsumer);
    }

    @Test
    public void test21() {
        /**
         * 只处理指定类型
         */
        this.dogObservable.ofType(String.class);
    }

    @Test
    public void test22() {

        /**
         * 重复无限次
         */
        this.dogObservable.repeat().subscribe(logConsumer);
    }

    @Test
    public void test23() {

        this.dogObservable.subscribe(logConsumer);

        this.dogObservable.replay(20).subscribe(logConsumer);
    }

    @Test
    public void test24() {

        this.dogObservable.retry();
    }

    @Test
    public void test25() {

        this.dogObservable.timeout(5, TimeUnit.SECONDS).subscribe(logConsumer);
    }

    @Test
    public void test26() {

        /**
         * 增加时间戳
         */

        this.dogObservable.timestamp().subscribe(dogTimed -> System.out.println(dogTimed));
    }

    @Test
    public void test27() {



    }

}
