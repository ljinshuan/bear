package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.TimestampMessage;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import java.util.Map;

/**
 * @author jinshuan.li 2018/7/5 19:02
 */
public class TacMutableLogEvent extends MutableLogEvent implements TacLogEvent {

    private String msCode;


    transient boolean reserved = false;


    @Override
    public void initFrom(LogEvent event) {
        super.initFrom(event);

        // 设置msCode
        this.msCode = ThreadLocals.getMsCode();

    }

    /**
     * @return
     */
    @Override
    public String getMsCode() {
        return this.msCode;
    }

    void initTime(final Clock clock, final NanoClock nanoClock) {
        Message message = this.getMessage();
        MutableInstant instant = (MutableInstant)this.getInstant();
        if (message instanceof TimestampMessage) {
            instant.initFromEpochMilli(((TimestampMessage)message).getTimestamp(), 0);
        } else {
            instant.initFrom(clock);
        }
        long nanoTime = nanoClock.nanoTime();
        setNanoTime(nanoTime);
    }
}
