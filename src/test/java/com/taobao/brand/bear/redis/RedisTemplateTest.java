package com.taobao.brand.bear.redis;

import com.taobao.brand.bear.BearAppBaseTest;
import org.junit.Test;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;

/**
 * @author jinshuan.li 2018/9/5 22:14
 */
public class RedisTemplateTest extends BearAppBaseTest {

    @Resource(name = "redisCountTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Test
    public void test(){


        hashOperations.increment("test", "abc", 1);
        hashOperations.increment("test", "abc", 1);

        String aLong = hashOperations.get("test", "abc");

        System.out.println(aLong);
    }
}
