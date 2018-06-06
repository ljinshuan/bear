package com.taobao.brand.bear.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author jinshuan.li 28/05/2018 22:05
 */
public class TacAsyncAppender extends TacAsyncAppenderBase<ILoggingEvent> {

    @Override
    public void start() {

        super.start();
    }

    @Override
    protected void put(ILoggingEvent eventObject) {

        String msCode = "abcd";
        TacLoggingEvent loggingEvent = new TacLoggingEvent(msCode, eventObject);
        super.put(loggingEvent);
    }

    @Override
    protected ILoggingEvent takeEventFromQueue() {
        return super.takeEventFromQueue();
    }
}
