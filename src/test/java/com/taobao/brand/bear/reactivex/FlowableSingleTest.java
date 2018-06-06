package com.taobao.brand.bear.reactivex;

import com.taobao.brand.bear.domain.Dog;
import io.reactivex.Single;
import org.junit.Test;

/**
 * @author jinshuan.li 25/04/2018 15:20
 */
public class FlowableSingleTest {

    private Dog dog = new Dog("ljinshuan", 20);

    @Test
    public void test() {

        Single.just(dog).map(d -> d.getName()).blockingGet();
    }
}
