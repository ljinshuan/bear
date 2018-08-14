package com.taobao.brand.bear.mongodb;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Test;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

/**
 * @author jinshuan.li 2018/8/14 09:43
 */
public class MongoDBTestA {

    @Test
    public void test() {

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();
        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Person> collection = database.getCollection("person", Person.class);

        Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));

        collection.insertOne(ada);

        collection.find().forEach(new Block<Person>() {
            @Override
            public void apply(Person person) {

                System.out.println(person);
            }
        });

    }
}
