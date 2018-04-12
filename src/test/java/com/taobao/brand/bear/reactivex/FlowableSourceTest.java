package com.taobao.brand.bear.reactivex;

import com.google.common.collect.Lists;
import com.taobao.brand.bear.domain.Dog;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author jinshuan.li 04/04/2018 15:41
 */
@Slf4j
public class FlowableSourceTest {

    public List<Dog> datas = Lists.newArrayList();

    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void before() {

        datas.add(new Dog("ljinshuan", 26));
    }

    @Test
    public void test() {

        Flowable.just(new Dog("shuan", 11)).observeOn(Schedulers.newThread()).map(dog -> dog.getName())
            .subscribe(s -> log.info(s));

        System.out.println("end");

    }

    @Test
    public void testThrow() {

        try {
            throw new IllegalStateException("fewfew");
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));

            String s = stringWriter.toString();

            System.out.println(s);
        }

    }
}
