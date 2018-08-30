package com.taobao.brand.bear.mongodb;

import com.mongodb.client.model.geojson.Point;
import lombok.Data;
import org.bson.types.ObjectId;

/**
 * @author jinshuan.li 2018/8/14 10:49
 */
@Data
public class Restaurant {

    private ObjectId id;

    private String name;

    private Point location;
}
