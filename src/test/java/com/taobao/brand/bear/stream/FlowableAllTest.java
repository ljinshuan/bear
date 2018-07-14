package com.taobao.brand.bear.stream;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.utils.ThreadUtils;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.*;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.jsr166.Flow;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.err;
import static java.lang.System.in;
import static java.lang.System.out;

/**
 * @author jinshuan.li 2018/7/13 21:24
 */
@Slf4j
public class FlowableAllTest {

    private Consumer logConsumer = d -> log.info("{}", d);

    public List<Dog> getDogs() {

        List<Dog> datas = Lists.newArrayList();
        datas.add(new Dog("channel", 26));
        datas.add(new Dog("dabulin", 24));
        datas.add(new Dog("channel", 100));
        datas.add(new Dog("dabulin", 24));
        datas.add(new Dog("dabulin", 28));
        log.info("invoke getDogs-我很耗时");

        return datas;
    }

    public Flowable<Long> getIntervalFlow() {

        return Flowable.intervalRange(0L, 20L, 1, 100, TimeUnit.MILLISECONDS);
    }

    @Test
    public void reduceTest() {

        // 将一条流汇聚成一个最终值
        Flowable.just(1, 2).reduce((a, b) -> a + b).subscribe(logConsumer);
    }

    @Test
    public void allTest() {
        // 所有的是否满足条件
        Flowable.just(1).all(d -> d > 10).subscribe(logConsumer);
    }

    @Test
    public void ambTest() {

        // 只取先到达的流的所有数据  BBB delay 1秒 AAA delay 10秒 B先到 以后只处理B的数据

        Flowable<Long> ambAAA = Flowable.intervalRange(5L, 10L, 10, 1, TimeUnit.SECONDS);
        Flowable<Long> ambBBB = Flowable.intervalRange(100L, 10L, 1, 1, TimeUnit.SECONDS);

        ambAAA.ambWith(ambBBB).blockingForEach(logConsumer);

    }

    @Test
    public void anyTest() {

        // 只要流中一个元素满足条件即可
        Flowable.just(1, 2).any(a -> a > 1).subscribe(logConsumer);
    }

    @Test
    public void createTest() {

        // TODO: 2018/7/13 createTest
    }

    @Test
    public void toTest() {

        // to 系列 将流转换成对象   blockingIterable blockingLatest blockingMostRecent blockingNext sorted to toFuture toList
        // toMap toMultimap toSortedList
        Single<List<Long>> listSingle = getIntervalFlow().toList();
        List<Long> to = listSingle.blockingGet();
        System.out.println(to);
    }

    @Test
    public void fromTest() {

        // from系列  直接从现有数据中构建流
        Flowable.fromArray(1, 2, 3).subscribe(logConsumer);
    }

    @Test
    public void sequenceEqualTest() {

        // sequenceEqual 多条流里的数据严格相等

        Flowable<Long> ambAAA = Flowable.intervalRange(5L, 10L, 10, 1, TimeUnit.SECONDS);
        Flowable<Long> ambBBB = Flowable.intervalRange(100L, 10L, 1, 1, TimeUnit.SECONDS);

        Flowable.sequenceEqual(ambAAA, ambBBB).toFlowable().blockingForEach(logConsumer);
    }

    @Test
    public void firstTest() {

        Flowable.just(1, 2).firstElement().subscribe(logConsumer);
    }

    @Test
    public void blockingForEachTest() {

        // subscribe  与  blockingForEach 区别 : 前者流里抛异常不会抛到外面  后者会抛到外面
        //Flowable.just(1, 2).flatMap(d -> Flowable.error(new IllegalStateException())).subscribe(logConsumer);

        Flowable.just(1, 2).flatMap(d -> {
            throw new IllegalStateException("xxx");
        }).subscribe(logConsumer, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // 这里处理异常
            }
        });
    }

    @Test
    public void buffer() {

        // buffer 缓存部分数据再发射 当中间有异常 一个异常停止
        // onErrorReturnItem 一旦有异常  使用该值替换 并停止整条流
        // onErrorResumeNext 一旦有异常  是用返回的流接上 原来的流停止 相当于 switchIfError

        Flowable.rangeLong(0, 100).map(d -> {

            if (d == 89) {
                throw new IllegalStateException("xxx");
            }
            return d;
        }).buffer(10).subscribe(logConsumer);
    }

    @Test
    public void cacheTest() {

        // 缓存上一个订阅发出的数据  下一个订阅来时一次性吐出全部数据
        Flowable<Long> intervalFlow = getIntervalFlow().cache();

        // 10秒后订阅
        intervalFlow.delaySubscription(10, TimeUnit.SECONDS).subscribe(logConsumer);

        // 立即订阅
        intervalFlow.subscribe(logConsumer);

        ThreadUtils.sleep(100000);
    }

    @Test
    public void cacheTest2() {

        // cache 以下两种情况  日志只会打一次
        Flowable<Long> count = Flowable.just(1, 2).map(s -> {

            log.info("comput-count");
            return s;
        }).count().toFlowable().cache();

        count.subscribe(logConsumer);

        count.subscribe(logConsumer);

        ThreadUtils.sleep(1000);
    }

    @Test
    public void cacheTest3() {

        // cache 以下两种情况  日志只会打一次
        Flowable<Dog> dogFlowable = Flowable.just(1, 2).flatMap(s -> Flowable.fromIterable(getDogs())).cache();

        dogFlowable.subscribe(logConsumer);

        dogFlowable.subscribe(logConsumer);

        ThreadUtils.sleep(1000);
    }

    @Test
    public void deferTest() {

        // defer 没有订阅不创建流 每个新订阅都创建一个新的流
        Flowable<Dog> defer = Flowable.defer(() -> {
            log.info("defer-call");
            return Flowable.fromIterable(getDogs());
        });

        defer.subscribe(logConsumer);
        defer.delaySubscription(1, TimeUnit.SECONDS).subscribe(logConsumer);
        ThreadUtils.sleep(10000);
    }

    @Test
    public void combineLatestTest() {

        // combineLatest 合并N个流的最新值

        Flowable.just(1, 2).withLatestFrom(Flowable.just(5), (a, b) -> a + b).blockingForEach(logConsumer);

    }

    @Test
    public void shareTest() {
        // TODO: 2018/7/14

        ConnectableFlowable<Dog> dogFlowable = Flowable.just(1, 2).flatMap(s -> Flowable.fromIterable(getDogs()))
            .publish();

        Flowable<Long> countFlow = dogFlowable.count().toFlowable();

        Flowable<String> map = dogFlowable.map(d -> d.getName());

        Flowable.combineLatest(countFlow, map, (a, b) -> a + b).subscribe(logConsumer);

        dogFlowable.connect();

        sleepForWait();
    }

    @Test
    public void concatMapTest() {

        // 类似 flatMap
        Flowable.just(1, 2).concatMap(d -> Flowable.just(1, 2, 3)).subscribe(logConsumer);
    }

    @Test
    public void flatMapTest() {

        // flatMap 返回publisher的merge 抛异常 终止
        // delay errors 决定开始还是最后处理异常

        Flowable.rangeLong(0, 10).flatMap(s -> {

            if (s == 5) {
                throw new IllegalStateException("5");
            }
            return Flowable.just(s + "", s + 1 + "");
        }, new BiFunction<Long, String, Object>() {
            @Override
            public Object apply(Long aLong, String s) throws Exception {
                return aLong + s;
            }
        }).subscribe(logConsumer, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                log.info("complete");
            }
        });
    }

    @Test
    public void connectTest() {

        // 这是一组操作  publish  将普通流转换成ConnectableFlowable   connect 开始发射数据
        // 以下流被订阅两次 但是getDogs只被调用两次 否则会调用4次
        ConnectableFlowable<Dog> publish = Flowable.just(1, 2).flatMap(d -> Flowable.fromIterable(getDogs())).publish();

        publish.subscribe(logConsumer);

        publish.map(d -> d.getName()).subscribe(logConsumer);

        publish.connect();

        sleepForWait();
    }

    @Test
    public void replayTest() {

        // 第一次延迟订阅 若没有 replay  会丢失部分数据
        Flowable<Long> intervalFlow = getIntervalFlow();

        ConnectableFlowable<Long> publish = intervalFlow.replay();

        publish.subscribe(logConsumer);

        publish.delaySubscription(1, TimeUnit.SECONDS).map(d -> d + "--ddd").subscribe(logConsumer);

        publish.connect();

        sleepForWait();
    }

    @Test
    public void doTest() {

        // onNext 里的异常也会影响生命周期
        Flowable.rangeLong(0, 10).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (aLong.equals(5L)) {
                    throw new IllegalStateException("5");
                }
                System.out.println(aLong);
            }
        }).subscribe(logConsumer);
    }

    @Test
    public void switchMapTest() {

        // TODO: 2018/7/14
        Flowable.just(1, 2).switchMap(a -> Flowable.just("---" + a)).subscribe(logConsumer);
    }

    @Test
    public void groupTest() {

        Flowable<GroupedFlowable<String, Dog>> groupedFlowableFlowable = Flowable.just(1, 2).flatMap(
            d -> Flowable.fromIterable(getDogs())).groupBy(Dog::getName);

        groupedFlowableFlowable.subscribe(d -> {

            String key = d.getKey();

            d.buffer(2).subscribe(l -> {
                log.info(key);
                log.info(l.toString());
            });
        });
    }

    @Test
    public void groupTest2() {

        Flowable<GroupedFlowable<String, Dog>> groupedFlowableFlowable = Flowable.just(1, 2).flatMap(
            d -> Flowable.fromIterable(getDogs())).groupBy(Dog::getName);

        groupedFlowableFlowable.subscribe(g -> {

            String key = g.getKey();

            g.groupBy(n -> n.getAge() > 50).subscribe(g2 -> {

                Boolean key1 = g2.getKey();

                g2.buffer(2).subscribe(l -> {

                    log.info(key + key1);
                    log.info(l.toString());
                });
            });
        });
    }

    @Test
    public void groupTest3() {

        Flowable<GroupedFlowable<String, Dog>> groupedFlowableFlowable = Flowable.just(1, 2).flatMap(
            d -> Flowable.fromIterable(getDogs())).groupBy(Dog::getName);

        groupedFlowableFlowable.flatMap(g -> g.groupBy(n -> n.getAge() > 50)).subscribe(d -> {

        });
    }

    @Test
    public void groupTest4() {

        List<String> crowdIds = Lists.newArrayList("aaa", "bbb", "ccc", "dddd");

        Integer integer = Flowable.fromIterable(crowdIds).skip(0).flatMap(cid -> {

            return Flowable.fromIterable(getTestDatas());

        }).groupBy(d -> d > 40).flatMap(g -> {
            log.info("in group by : {}", g.getKey());
            if (g.getKey() == true) {
                return g.buffer(100).map(b -> handleBatch(true, b));
            } else {
                return g.buffer(100).map(b -> handleBatch(false, b));
            }
        }).reduce((a, b) -> {
            log.info("reduce a:{} b:{}", a, b);
            return a + b;
        }).blockingGet();

        System.out.println(integer);
    }

    private Integer handleBatch(boolean b, List<Long> b1) {

        log.info("handleBatch {} size:{}", b, b1.size());

        return b1.size();
    }

    @Test
    public void groupTest5() {

        List<String> crowdIds = Lists.newArrayList("aaa", "bbb", "ccc", "dddd");

        Flowable.fromIterable(crowdIds).skip(0).flatMap(cid -> {

            return Flowable.fromIterable(getTestDatas()).groupBy(d -> d > 30).flatMap(g -> {
                log.info("in group by : {}", g.getKey());
                if (g.getKey() == true) {
                    return g.buffer(100).map(b -> handleBatch(true, b));
                } else {
                    return g.buffer(100).map(b -> handleBatch(false, b));
                }
            });

        }).reduce((a, b) -> {
            log.info("reduce a:{} b:{}", a, b);
            return a + b;
        }).blockingGet();

    }

    public List<Long> getTestDatas() {

        log.info("invoke getDogs-我很耗时");

        return Flowable.rangeLong(0, 100).toList().blockingGet();

    }

    public static void sleepForWait() {

        log.info("====over====");
        ThreadUtils.sleep(10000);
    }

}
