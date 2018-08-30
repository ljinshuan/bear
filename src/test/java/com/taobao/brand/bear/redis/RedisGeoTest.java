package com.taobao.brand.bear.redis;

import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/8/5 16:17
 */
@Slf4j
public class RedisGeoTest {

    private JedisPool jedisPool;

    @Before
    public void initPool() {

        jedisPool = new JedisPool("127.0.0.1", 6379);

    }

    @Test
    public void zset() {

        Jedis jedis = getJedis();

        String key = "timeMessage";

        jedis.zadd(key, 333, "t-3333");
        jedis.zadd(key, 444, "t-444");

        Set<String> strings = jedis.zrangeByScore(key, 0, 333, 0, 100);

        Set<Tuple> tuples = jedis.zrangeByScoreWithScores(key, 0, 333);

        for (Tuple tuple : tuples) {
            String element = tuple.getElement();
            System.out.println(element);

        }


        System.out.println(strings);

    }

    private Jedis getJedis() {

        return this.jedisPool.getResource();
    }

    private void closeJedis(Jedis jedis) {

        if (jedis != null) {
            jedis.close();
        }
    }

    @Test
    public void test() {

        Jedis resource = getJedis();

        String s = resource.get("name");

        closeJedis(resource);

        System.out.println(s);
    }

    @Test
    public void test2() {
        Jedis jedis = getJedis();

        GeoRadiusParam param = GeoRadiusParam.geoRadiusParam().withCoord().withDist();
        List<GeoRadiusResponse> sicily = jedis.georadius("Sicily", 15, 37, 100, GeoUnit.KM, param);

        sicily.stream().forEach(d -> {

            String memberByString = d.getMemberByString();

            System.out.println(memberByString);
        });

        closeJedis(jedis);
    }

    @Test
    public void addTest() {
        double baseLong = 120.0919500000d;
        double baselati = 30.3219600000d;

        Random random = new Random(System.currentTimeMillis());

        String key = "Hangzhou";
        Jedis jedis = getJedis();
        StopWatch started = StopWatch.createStarted();
        Flowable.rangeLong(0, 1000000).forEach(i -> {

            double v = random.nextGaussian() * 10.0;
            long start = System.currentTimeMillis();
            jedis.geoadd(key, baseLong + v, baselati + v, String.valueOf(i));
            long end = System.currentTimeMillis();

            log.info("cost i:{} cost:{}", i, end - start);

        });
        closeJedis(jedis);
        log.info("end  {}", started.getTime(TimeUnit.MILLISECONDS));
    }

    @Test
    public void georadiusTest() {
        double baseLong = 120.0919500000d;
        double baselati = 30.3219600000d;

        String key = "Hangzhou";
        Jedis jedis = getJedis();
        StopWatch started = StopWatch.createStarted();
        GeoRadiusParam param = GeoRadiusParam.geoRadiusParam().withCoord().withDist();

        List<GeoRadiusResponse> georadius = jedis.georadius(key, baseLong, baselati, 500, GeoUnit.KM, param);
        closeJedis(jedis);

        log.info("end  {} count:{}", started.getTime(TimeUnit.MILLISECONDS), georadius.size());
    }

}
