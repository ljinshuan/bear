package com.taobao.brand.bear.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

/**
 * @author jinshuan.li 2018/8/22 21:12
 */
@Slf4j
public class LocationTest {

    CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    MongoClientSettings settings = MongoClientSettings.builder()
        .codecRegistry(pojoCodecRegistry)
        .build();

    private MongoClient mongoClient = MongoClients.create(settings);

    public MongoCollection<Restaurant> getCollection() {

        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Restaurant> collection = database.getCollection("restaurants_hangzhou2", Restaurant.class);

        collection.createIndex(Indexes.geo2dsphere("location"));

        return collection;
    }

    @Test
    public void addTest() {
        double baseLong = 120.0919500000d;
        double baselati = 30.3219600000d;

        Random random = new Random(System.currentTimeMillis());

        MongoCollection<Restaurant> collection = getCollection();
        StopWatch started = StopWatch.createStarted();

        Restaurant restaurant = new Restaurant();
        restaurant.setName("jinshuan.li");
        restaurant.setLocation(new Point(new Position(baseLong, baselati)));
        collection.insertOne(restaurant);

        restaurant = new Restaurant();
        restaurant.setName("jinshuan.li-2");
        restaurant.setLocation(new Point(new Position(baseLong + 1, baselati + 1)));
        collection.insertOne(restaurant);

    }

    @Test
    public void geoQueryTest() {
        double baseLong = 120.0919500000d;
        double baselati = 30.3219600000d;

        List<Restaurant> restaurants = queryNearBy(baseLong, baselati, 140.0, 100);

        System.out.println(restaurants);
    }

    /**
     * 查询附近人数
     *
     * @param longitude
     * @param latitude
     * @param maxDistance
     * @param count
     * @return
     */
    public List<Restaurant> queryNearBy(Double longitude, Double latitude, Double maxDistance,
                                        int count) {

        maxDistance = maxDistance / 6378.1d;

        log.info("queryNearBy longitude:{}  latitude:{} maxDistance:{} count:{}", latitude, latitude, maxDistance,
            count);
        Bson location = Filters.geoWithinCenterSphere("location", longitude, latitude, maxDistance);

        MongoCollection<Restaurant> collection = getCollection();

        FindIterable<Restaurant> limit = collection.find(location);

        List<Restaurant> collect = StreamSupport.stream(limit.spliterator(), false).collect(Collectors.toList());

        return collect;
    }
}
