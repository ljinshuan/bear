package com.taobao.brand.bear.app;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author jinshuan.li 2018/1/1 23:07
 */
public class AppTest {

    @Test
    public void test() {

        List<Long> data = Lists.newArrayList(1L, 2L, 3L);

        Stream<Long> objectStream = data.stream().map(new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) {
                return aLong + 2L;
            }
        });
    }
}
