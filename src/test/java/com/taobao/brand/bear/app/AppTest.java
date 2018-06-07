package com.taobao.brand.bear.app;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.domain.Dog2;
import com.taobao.brand.bear.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jinshuan.li 2018/1/1 23:07
 */
@Slf4j
public class AppTest {

    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Test
    public void test() throws InterruptedException {

        Date oldTimeDate = new Date(1528344487000L);
        Long oldTime = oldTimeDate.getTime();

        LocalDate localDate = Instant.ofEpochMilli(oldTime).atZone(ZoneId.systemDefault()).toLocalDate().with(
            ChronoField.DAY_OF_WEEK, 1);

        LocalDate now = LocalDate.now();
        now = now.with(ChronoField.DAY_OF_WEEK, 1);

        boolean equals = localDate.equals(now);
        long between = ChronoUnit.MONTHS.between(localDate, now);
        if (between == 0) {
            // 同一周

            System.out.println("xxxx");
        }

    }

    public static void main(String[] args) {

        List<Integer> datas = Lists.newArrayList(1, 2, 3, 4, 5);

        List<Integer> integers = datas.subList(0, datas.size());

        System.out.println(integers);

        int fromIndex = 0;
        int maxEndIndex = datas.size();

        int count = 7;
        while (fromIndex < maxEndIndex) {

            int toIndex = fromIndex + count;
            toIndex = maxEndIndex >= toIndex ? toIndex : maxEndIndex;
            List<Integer> integers1 = datas.subList(fromIndex, toIndex);

            System.out.println(integers1);

            fromIndex = toIndex;
        }

    }

    @Test
    public void test2() {

        List<Dog2> datas = Lists.newArrayList();
        Dog2 a = new Dog2("ljinshuan", 11, 1);
        Dog2 c = new Dog2("ljinshuan3", 16, 0);
        Dog2 b = new Dog2("ljinshuan2", 12, 0);

        datas.add(a);
        datas.add(c);
        datas.add(b);
        List<Dog2> collect = datas.stream().sorted(Comparator.comparing(Dog2::getStatus).reversed()
            .thenComparing(Dog2::getAge))
            .collect(
                Collectors.toList());

        System.out.println(collect);
    }
}
