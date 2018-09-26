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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author jinshuan.li 2018/9/26 15:41
 */

@Plugin(name = "TacAsyncLogger", category = Node.CATEGORY, printObject = true)
public class TacAsyncLoggerConfig extends AsyncLoggerConfig {

    private TacAsyncLoggerConfigDisruptor tacDelegate;

    private AppenderControlArraySet parentAppenders;

    protected TacAsyncLoggerConfig(String name, List<AppenderRef> appenders,
                                   Filter filter, Level level,
                                   boolean additive, Property[] properties,
                                   Configuration config, boolean includeLocation) {
        super(name, appenders, filter, level, additive, properties, config, includeLocation);

        tacDelegate = new TacAsyncLoggerConfigDisruptor();

        tacDelegate.setLogEventFactory(getLogEventFactory());

    }

    @Override
    public void start() {

        super.start();

        tacDelegate.start();
    }

    /**
     * Called by AsyncLoggerConfigHelper.RingBufferLog4jEventHandler.
     */
    public void asyncCallAppenders(final LogEvent event) {

        if (parentAppenders == null) {
            parentAppenders = getParentAppenders();
        }
        final AppenderControl[] controls = parentAppenders.get();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < controls.length; i++) {
            controls[i].callAppender(event);
        }

    }

    /**
     * Passes on the event to a separate thread that will call {@link #asyncCallAppenders(LogEvent)}.
     */
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

    void callAppendersInCurrentThread(final LogEvent event) {
        super.callAppenders(event);
    }

    void callAppendersInBackgroundThread(final LogEvent event) {
        tacDelegate.enqueueEvent(event, this);
    }

    private void handleQueueFull(final LogEvent event) {
        if (AbstractLogger.getRecursionDepth() > 1) { // LOG4J2-1518, LOG4J2-2031
            // If queue is full AND we are in a recursive call, call appender directly to prevent deadlock
            final Message message = AsyncQueueFullMessageUtil.transform(event.getMessage());
            callAppendersInCurrentThread(new Log4jLogEvent.Builder(event).setMessage(message).build());
        } else {
            // otherwise, we leave it to the user preference
            final EventRoute eventRoute = tacDelegate.getEventRoute(event.getLevel());
            eventRoute.logMessage(this, event);
        }
    }

    /**
     * Factory method to create a LoggerConfig.
     *
     * @param additivity      True if additive, false otherwise.
     * @param levelName       The Level to be associated with the Logger.
     * @param loggerName      The name of the Logger.
     * @param includeLocation "true" if location should be passed downstream
     * @param refs            An array of Appender names.
     * @param properties      Properties to pass to the Logger.
     * @param config          The Configuration.
     * @param filter          A Filter.
     * @return A new LoggerConfig.
     */
    @PluginFactory
    public static LoggerConfig createLogger(
        @PluginAttribute("additivity") final String additivity,
        @PluginAttribute("level") final String levelName,
        @PluginAttribute("name") final String loggerName,
        @PluginAttribute("includeLocation") final String includeLocation,
        @PluginElement("AppenderRef") final AppenderRef[] refs,
        @PluginElement("Properties") final Property[] properties,
        @PluginConfiguration final Configuration config,
        @PluginElement("Filter") final Filter filter) {
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

    public AppenderControlArraySet getParentAppenders() {

        try {
            Field appenders = LoggerConfig.class.getDeclaredField("appenders");
            appenders.setAccessible(true);

            AppenderControlArraySet o = (AppenderControlArraySet)appenders.get(this);

            return o;
        } catch (Exception e) {

            System.out.println("xx");
        }
        return null;
    }
}
