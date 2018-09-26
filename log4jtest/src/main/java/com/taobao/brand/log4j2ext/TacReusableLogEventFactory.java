package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.ThreadNameCachingStrategy;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringMap;

import java.util.List;

/**
 * @author jinshuan.li 2018/9/24 10:23
 */
public class TacReusableLogEventFactory extends ReusableLogEventFactory {

    private static ThreadLocal<TacMutableLogEvent> mutableLogEventThreadLocal = new ThreadLocal<>();

    private static final Clock CLOCK = ClockFactory.getClock();

    private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();

    private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();

    @Override
    public LogEvent createEvent(String loggerName, Marker marker, String fqcn, Level level, Message message,
                                List<Property> properties, Throwable t) {

        TacMutableLogEvent result = mutableLogEventThreadLocal.get();
        if (result == null || result.reserved) {
            final boolean initThreadLocal = result == null;
            result = new TacMutableLogEvent();

            // usually no need to re-initialize thread-specific fields since the event is stored in a ThreadLocal
            result.setThreadId(Thread.currentThread().getId());
            result.setThreadName(Thread.currentThread().getName()); // Thread.getName() allocates Objects on each call
            result.setThreadPriority(Thread.currentThread().getPriority());
            if (initThreadLocal) {
                mutableLogEventThreadLocal.set(result);
            }
        }
        result.reserved = true;
        result.clear(); // ensure any previously cached values (thrownProxy, source, etc.) are cleared

        result.setLoggerName(loggerName);
        result.setMarker(marker);
        result.setLoggerFqcn(fqcn);
        result.setLevel(level == null ? Level.OFF : level);
        result.setMessage(message);
        result.initTime(CLOCK, Log4jLogEvent.getNanoClock());
        result.setThrown(t);
        result.setContextData(injector.injectContextData(properties, (StringMap)result.getContextData()));
        result.setContextStack(
            ThreadContext.getDepth() == 0 ? ThreadContext.EMPTY_STACK : ThreadContext.cloneStack());// mutable copy

        if (THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
            result.setThreadName(Thread.currentThread().getName()); // Thread.getName() allocates Objects on each call
            result.setThreadPriority(Thread.currentThread().getPriority());
        }
        return result;
    }

}
