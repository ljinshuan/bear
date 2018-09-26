package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;

import java.util.List;

/**
 * @author jinshuan.li 2018/9/24 10:04
 */
public class TacDefaultLogEventFactory extends DefaultLogEventFactory {

    @Override
    public LogEvent createEvent(String loggerName, Marker marker, String fqcn, Level level, Message data,
                                List<Property> properties, Throwable t) {
        return new TacLog4jLogEvent(loggerName, marker, fqcn, level, data, properties, t);
    }
}
