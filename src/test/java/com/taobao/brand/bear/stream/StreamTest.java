package com.taobao.brand.bear.stream;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jinshuan.li 2018/1/4 01:11
 */
public class StreamTest {

    private List<String> datas = Lists.newArrayList("ljinshuan", "aoguilin");

    @Test
    public void flatMap() {

        List<List<Integer>> datas = Lists.newArrayList();
        datas.add(Lists.newArrayList(1, 2));
        datas.add(Lists.newArrayList(3, 4));

        List<Integer> collect = datas.stream().flatMap(items -> items.stream()).collect(Collectors.toList());

        System.out.println(collect);

    }

    @Test
    public void test() {
        datas.stream().peek(s -> System.out.println(s)).forEach(s -> System.out.println(s));

    }

    @Test
    public void test2() {

        Optional<String> reduce = datas.stream().reduce((s, s2) -> s + s2);

        String s = reduce.get();

        System.out.println(s);
    }

    @Test
    public void test3() {

        Map<String, String> collect = datas.stream().collect(Collectors.toMap(s -> s, s -> s));

        collect.put("xxx", null);
        System.out.println(collect);
    }

    @Test
    public void test4() {

        datas.stream().map(s -> s.length()).filter(d -> d > 0).collect(Collectors.toList());
    }

    @Test
    public void test5() {

        datas.stream().map(s -> {

            throw new IllegalStateException("hahaha");
        }).forEach(System.out::println);
    }
}
