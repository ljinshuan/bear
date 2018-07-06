package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.async.AsyncQueueFullMessageUtil;
import org.apache.logging.log4j.core.async.EventRoute;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.List;

/**
 * @author jinshuan.li 2018/7/5 14:36
 */
@Plugin(name = "TacAsyncLogger", category = Node.CATEGORY, printObject = true)
public class TacAsyncLoggerConfig extends AsyncLoggerConfig {

    private final AsyncLoggerConfigDelegate tacDelegate = new TacAsyncLoggerConfigDisruptor();

    private Configuration configuration;

    protected TacAsyncLoggerConfig(String name, List<AppenderRef> appenders,
                                   Filter filter, Level level,
                                   boolean additive, Property[] properties,
                                   Configuration config, boolean includeLocation) {

        super(name, appenders, filter, level, additive, properties, config, includeLocation);
        this.configuration = config;

        tacDelegate.setLogEventFactory(getLogEventFactory());

    }

    @Override
    public void start() {
        super.start();

        // 这里要启动
        TacAsyncLoggerConfigDisruptor tacDelegate = (TacAsyncLoggerConfigDisruptor)this.tacDelegate;

        tacDelegate.start();

    }

    @Override
    protected void callAppenders(final LogEvent event) {

        populateLazilyInitializedFields(event);

        if (!tacDelegate.tryEnqueue(event, this)) {
            handleQueueFull(event);
        }

    }

    private void populateLazilyInitializedFields(final LogEvent event) {
        event.getSource();
        event.getThreadName();
    }

    private void handleQueueFull(final LogEvent event) {
        if (AbstractLogger.getRecursionDepth() > 1) { // LOG4J2-1518, LOG4J2-2031
            // If queue is full AND we are in a recursive call, call appender directly to prevent deadlock
            final Message message = AsyncQueueFullMessageUtil.transform(event.getMessage());
            this.callAppenders(new Log4jLogEvent.Builder(event).setMessage(message).build());
        } else {
            // otherwise, we leave it to the user preference
            final EventRoute eventRoute = tacDelegate.getEventRoute(event.getLevel());
            eventRoute.logMessage(this, event);
        }
    }

    @PluginFactory
    public static LoggerConfig createLogger(
        @PluginAttribute("additivity") final String additivity,
        @PluginAttribute("level") final String levelName,
        @PluginAttribute("name") final String loggerName,
        @PluginAttribute("includeLocation") final String includeLocation,
        @PluginElement("AppenderRef") final AppenderRef[] refs,
        @PluginElement("Properties") final Property[] properties,
        @PluginConfiguration final Configuration config,
        @PluginElement("Filter") final Filter filter
    ) {
        if (loggerName == null) {
            LOGGER.error("Loggers cannot be configured without a name");
            return null;
        }

        final List<AppenderRef> appenderRefs = Arrays.asList(refs);
        Level level;
        try {
            level = Level.toLevel(levelName, Level.ERROR);
        } catch (final Exception ex) {
            LOGGER.error(
                "Invalid Log level specified: {}. Defaulting to Error",
                levelName);
            level = Level.ERROR;
        }
        final String name = loggerName.equals(LoggerConfig.ROOT) ? Strings.EMPTY : loggerName;
        final boolean additive = Booleans.parseBoolean(additivity, true);

        return new TacAsyncLoggerConfig(name, appenderRefs, filter, level,
            additive, properties, config, includeLocation(includeLocation));
    }

}
