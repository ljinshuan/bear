package com.taobao.brand.bear.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author jinshuan.li 2017/12/22 23:15
 */
public class StringHelper {

    public static String toJsonString(Object obj) {

        if (obj == null) {
            return null;
        }

        return JSON.toJSONString(obj);
    }
}
