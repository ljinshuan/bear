package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;

import java.util.List;

/**
 * @author jinshuan.li 2018/9/24 10:02
 */
public class TacLog4jLogEvent extends Log4jLogEvent implements TacLogEvent {

    private String msCode;

    public TacLog4jLogEvent() {
        super();
        this.msCode = ThreadLocals.getMsCode();
    }

    public TacLog4jLogEvent(String loggerName, Marker marker, String fqcn, Level level, Message data,
                            List<Property> properties, Throwable t) {

        super(loggerName, marker, fqcn, level, data, properties, t);

        this.msCode = ThreadLocals.getMsCode();
    }

    /**
     * @return
     */
    @Override
    public String getMsCode() {
        return this.msCode;
    }
}
