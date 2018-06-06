package com.taobao.brand.bear.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import org.slf4j.Marker;

import java.util.Map;

/**
 * @author jinshuan.li 28/05/2018 22:24
 */
public class TacLoggingEvent implements ILoggingEvent {

    private String msCode;

    private ILoggingEvent rawLoggingEvent;

    public TacLoggingEvent(String msCode, ILoggingEvent rawLoggingEvent) {
        this.msCode = msCode;
        this.rawLoggingEvent = rawLoggingEvent;
    }

    public String getMsCode() {
        return msCode;
    }

    public void setMsCode(String msCode) {
        this.msCode = msCode;
    }

    @Override
    public String getThreadName() {return rawLoggingEvent.getThreadName();}

    @Override
    public Level getLevel() {return rawLoggingEvent.getLevel();}

    @Override
    public String getMessage() {return rawLoggingEvent.getMessage();}

    @Override
    public Object[] getArgumentArray() {return rawLoggingEvent.getArgumentArray();}

    @Override
    public String getFormattedMessage() {return rawLoggingEvent.getFormattedMessage();}

    @Override
    public String getLoggerName() {return rawLoggingEvent.getLoggerName();}

    @Override
    public LoggerContextVO getLoggerContextVO() {return rawLoggingEvent.getLoggerContextVO();}

    @Override
    public IThrowableProxy getThrowableProxy() {return rawLoggingEvent.getThrowableProxy();}

    @Override
    public StackTraceElement[] getCallerData() {return rawLoggingEvent.getCallerData();}

    @Override
    public boolean hasCallerData() {return rawLoggingEvent.hasCallerData();}

    @Override
    public Marker getMarker() {return rawLoggingEvent.getMarker();}

    @Override
    public Map<String, String> getMDCPropertyMap() {return rawLoggingEvent.getMDCPropertyMap();}

    @Override
    public Map<String, String> getMdc() {return rawLoggingEvent.getMdc();}

    @Override
    public long getTimeStamp() {return rawLoggingEvent.getTimeStamp();}

    @Override
    public void prepareForDeferredProcessing() {rawLoggingEvent.prepareForDeferredProcessing();}

}