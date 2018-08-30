package com.taobao.brand.bear.mongodb;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.Test;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

/**
 * @author jinshuan.li 2018/8/14 09:43
 */
@Slf4j
public class MongoDBTestA {

    CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    MongoClientSettings settings = MongoClientSettings.builder()
        .codecRegistry(pojoCodecRegistry)
        .build();

    private MongoClient mongoClient = MongoClients.create(settings);

    @Test
    public void test() {

        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Person> collection = database.getCollection("person", Person.class);

        Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));

        ada.setName(null);

        collection.insertOne(ada);

        collection.find().forEach(new Block<Person>() {
            @Override
            public void apply(Person person) {

                System.out.println(person);
            }
        });

    }

    @Test
    public void testUpdateName() {

        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Person> collection = database.getCollection("person", Person.class);

        Person ada = new Person("Ada Byron", 23, new Address("St James Square", "London", "W1"));

        ada.setName("jinshuan.li-repace");

        Bson filter = Filters.eq("name", "jinshuan.li-repace");
        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        ReplaceOptions replaceOptions = ReplaceOptions.createReplaceOptions(options);
        collection.replaceOne(filter, ada, replaceOptions);

        collection.find(filter).forEach((Block<Person>)person -> log.info("new data {}", person));
    }

    @Test
    public void testQueryGeo() {

        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Restaurant> collection = database.getCollection("restaurants", Restaurant.class);

        Restaurant first = collection.find().first();

        Point refPoint = new Point(new Position(-73.9667, 40.78));
       /* collection.find().forEach(new Block<Restaurant>() {
            @Override
            public void apply(Restaurant restaurant) {

                log.info(restaurant.toString());
            }
        });*/

        Bson locationFilter = Filters.near("location", refPoint, 500000.0, 1000.0);

        Bson location = Filters.geoWithinCenterSphere("location", -73.9667, 40.78, 500000.0);

        CountOptions countOptions = new CountOptions();

        countOptions.limit(5000000);
        //countOptions.maxTime(1, TimeUnit.MILLISECONDS);
        long count = collection.countDocuments(location, countOptions);

        System.out.println(count);

    }

    @Test
    public void addTest() {
        double baseLong = 120.0919500000d;
        double baselati = 30.3219600000d;

        Random random = new Random(System.currentTimeMillis());

        String key = "Hangzhou";
        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Restaurant> collection = database.getCollection("restaurants_hangzhou", Restaurant.class);
        StopWatch started = StopWatch.createStarted();
        Flowable.rangeLong(0, 1000000).forEach(i -> {

            double v = random.nextGaussian() * 10.0;
            long start = System.currentTimeMillis();

            Restaurant restaurant = new Restaurant();
            restaurant.setName(i + "");
            restaurant.setLocation(new Point(new Position(baseLong + v, baselati + v)));
            collection.insertOne(restaurant);
            long end = System.currentTimeMillis();

            log.info("cost i:{} cost:{}", i, end - start);

        });

        log.info("end  {}", started.getTime(TimeUnit.MILLISECONDS));

        //  long l = collection.countDocuments();

        //System.out.println(l);
    }

    @Test
    public void geoQuery() {

        double baseLong = 120.0919500000d;
        double baselati = 30.3219600000d;

        String key = "Hangzhou";
        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Restaurant> collection = database.getCollection("restaurants_hangzhou", Restaurant.class);
        StopWatch started = StopWatch.createStarted();
        Point refPoint = new Point(new Position(baseLong, baselati));

        Bson locationFilter = Filters.nearSphere("location", refPoint, 500000.0, 0.0);

        long count = collection.count(locationFilter);

        System.out.println(count);

        log.info("end  {} count:{}", started.getTime(TimeUnit.MILLISECONDS), count);

    }
}
