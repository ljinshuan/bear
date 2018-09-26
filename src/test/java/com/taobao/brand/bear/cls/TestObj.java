package com.taobao.brand.bear.cls;

/**
 * @author jinshuan.li 2018/8/31 12:54
 */
public class TestObj {

    static {

        if (true){
            throw new IllegalStateException("xxx");
        }
    }

    public TestObj(){


    }

    public static String name="testObj";
}
