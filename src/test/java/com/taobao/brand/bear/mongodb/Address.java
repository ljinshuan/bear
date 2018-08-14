package com.taobao.brand.bear.mongodb;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jinshuan.li 2018/8/14 09:46
 */
@Data
public class Address implements Serializable {

    private String street;
    private String city;
    private String zip;

    public Address(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public Address() {

    }
}
