package com.taobao.brand.bear.cls;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author jinshuan.li 2018/8/29 00:19
 */
public class ClassLoaderTest {

    @Test
    public void test()
        throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        URL url = new File("/Users/jinshuan.li/Source/bear/target/bear-0.0.1-SNAPSHOT-test.jar").toURI().toURL();

        URLClassLoader classLoader = new URLClassLoader(new URL[] {url});

        Class<?> aClass = classLoader.loadClass("com.taobao.brand.bear.cls.TacHandlerImpl");

        TacHandler o = (TacHandler)aClass.newInstance();

        Object execute = o.execute();

        System.out.println(execute);
    }
}
