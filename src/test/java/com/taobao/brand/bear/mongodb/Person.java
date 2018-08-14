package com.taobao.brand.bear.mongodb;

import lombok.Data;
import org.bson.types.ObjectId;

/**
 * @author jinshuan.li 2018/8/14 09:46
 */
@Data
public class Person {

    private ObjectId id;
    private String name;
    private int age;
    private Address address;

    public Person(String name, int age, Address address) {

        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Person() {

    }
}
