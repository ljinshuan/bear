package com.taobao.brand.bear.geo;

import ch.hsr.geohash.GeoHash;
import ch.qos.logback.core.net.SyslogOutputStream;
import com.taobao.brand.bear.BearApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author jinshuan.li 2017/12/22 21:47
 */
@Slf4j
public class GeoHashTest extends BearApplicationTests{

    @Test
    public void test(){
        double latitude=36.1688952096;
        double lng=99.9920272319;
        GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, lng, 6);

        log.info(geoHash.toBase32());
    }
}
