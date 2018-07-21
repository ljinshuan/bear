package com.taobao.brand.bear.appender;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author jinshuan.li 2018/7/15 11:51
 */
public class TraceIdConverter extends ClassicConverter {
    /**
     * The convert method is responsible for extracting data from the event and storing it for later use by the write
     * method.
     *
     * @param event
     */
    @Override
    public String convert(ILoggingEvent event) {

        return "xxxx traceId";
    }
}
