package com.taobao.brand.log4j2ext;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.*;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.message.ReusableMessage;

import java.util.concurrent.ThreadFactory;

public class TacAsyncLoggerConfigDisruptor extends AsyncLoggerConfigDisruptor {

    /**
     * Factory used to populate the RingBuffer with events. These event objects are then re-used during the life of the
     * RingBuffer.
     */
    private static final EventFactory<Log4jEventWrapper> FACTORY = new EventFactory<Log4jEventWrapper>() {
        @Override
        public Log4jEventWrapper newInstance() {
            return new Log4jEventWrapper();
        }
    };

    /**
     * Factory used to populate the RingBuffer with events. These event objects are then re-used during the life of the
     * RingBuffer.
     */
    private static final EventFactory<Log4jEventWrapper> MUTABLE_FACTORY = () -> new Log4jEventWrapper(
        new TacMutableLogEvent());

    private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, TacAsyncLoggerConfig> TRANSLATOR =
        (EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, TacAsyncLoggerConfig>)(ringBufferElement, sequence,
                                                                                   logEvent, loggerConfig) -> {
            ringBufferElement.event = logEvent;
            ringBufferElement.loggerConfig = loggerConfig;
        };

    /**
     * Object responsible for passing on data to a RingBuffer event with a MutableLogEvent.
     */
    private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, TacAsyncLoggerConfig> MUTABLE_TRANSLATOR =
        (ringBufferElement, sequence, logEvent, loggerConfig) -> {
            ((TacMutableLogEvent)ringBufferElement.event).initFrom(logEvent);
            ringBufferElement.loggerConfig = (TacAsyncLoggerConfig)loggerConfig;
        };

    /**
     * RingBuffer events contain all information necessary to perform the work in a separate thread.
     */
    public static class Log4jEventWrapper extends AsyncLoggerConfigDisruptor.Log4jEventWrapper {
        public Log4jEventWrapper() {
        }

        public Log4jEventWrapper(final MutableLogEvent mutableLogEvent) {
            event = mutableLogEvent;
        }

        private TacAsyncLoggerConfig loggerConfig;
        private LogEvent event;

        /**
         * Release references held by ring buffer to allow objects to be garbage-collected.
         */
        @Override
        public void clear() {
            loggerConfig = null;
            if (event instanceof MutableLogEvent) {
                ((MutableLogEvent)event).clear();
            } else {
                event = null;
            }
        }

        @Override
        public String toString() {
            return String.valueOf(event);
        }
    }

    /**
     * EventHandler performs the work in a separate thread.
     */
    private static class Log4jEventWrapperHandler implements
        SequenceReportingEventHandler<Log4jEventWrapper> {
        private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
        private Sequence sequenceCallback;
        private int counter;

        @Override
        public void setSequenceCallback(final Sequence sequenceCallback) {
            this.sequenceCallback = sequenceCallback;
        }

        @Override
        public void onEvent(final Log4jEventWrapper event, final long sequence, final boolean endOfBatch)
            throws Exception {
            event.event.setEndOfBatch(endOfBatch);
            event.loggerConfig.asyncCallAppenders(event.event);
            event.clear();

            notifyIntermediateProgress(sequence);
        }

        /**
         * Notify the BatchEventProcessor that the sequence has progressed. Without this callback the sequence would not
         * be progressed until the batch has completely finished.
         */
        private void notifyIntermediateProgress(final long sequence) {
            if (++counter > NOTIFY_PROGRESS_THRESHOLD) {
                sequenceCallback.set(sequence);
                counter = 0;
            }
        }
    }

    private volatile Disruptor<Log4jEventWrapper> tacDisruptor;

    private EventFactory<Log4jEventWrapper> factory;
    private EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, TacAsyncLoggerConfig> translator;
    private Boolean mutable = Boolean.FALSE;
    private int ringBufferSize;

    private long backgroundThreadId;

    private AsyncQueueFullPolicy asyncQueueFullPolicy;

    // LOG4J2-471

    private volatile boolean alreadyLoggedWarning = false;

    @Override
    public void setLogEventFactory(LogEventFactory logEventFactory) {
        super.setLogEventFactory(logEventFactory);

        this.mutable = mutable || (logEventFactory instanceof ReusableLogEventFactory);
    }

    @Override
    public synchronized void start() {

        super.start();

        translator = mutable ? MUTABLE_TRANSLATOR : TRANSLATOR;
        factory = mutable ? MUTABLE_FACTORY : FACTORY;
        ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLoggerConfig.RingBufferSize");

        final WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLoggerConfig.WaitStrategy");

        final ThreadFactory threadFactory = new Log4jThreadFactory("AsyncLoggerConfig-", true, Thread.NORM_PRIORITY) {
            @Override
            public Thread newThread(final Runnable r) {
                final Thread result = super.newThread(r);
                backgroundThreadId = result.getId();
                return result;
            }
        };

        asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();

        tacDisruptor = new Disruptor<>(factory, ringBufferSize, threadFactory, ProducerType.MULTI, waitStrategy);

        final ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper> errorHandler = DisruptorUtil
            .getAsyncLoggerConfigExceptionHandler();
        tacDisruptor.setDefaultExceptionHandler(errorHandler);

        final Log4jEventWrapperHandler[] handlers = {new Log4jEventWrapperHandler()};
        tacDisruptor.handleEventsWith(handlers);

        LOGGER.debug("Starting AsyncLoggerConfig disruptor for this configuration with ringbufferSize={}, "
            + "waitStrategy={}, exceptionHandler={}...", tacDisruptor.getRingBuffer().getBufferSize(), waitStrategy
            .getClass().getSimpleName(), errorHandler);
        tacDisruptor.start();

    }

    @Override
    public boolean tryEnqueue(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {

        final LogEvent logEvent = prepareEvent(event);

        return tacDisruptor.getRingBuffer().tryPublishEvent(translator, logEvent,
            (TacAsyncLoggerConfig)asyncLoggerConfig);
    }

    private LogEvent prepareEvent(final LogEvent event) {
        LogEvent logEvent = ensureImmutable(event);
        if (logEvent.getMessage() instanceof ReusableMessage) {
            if (logEvent instanceof Log4jLogEvent) {
                ((Log4jLogEvent)logEvent).makeMessageImmutable();
            } else if (logEvent instanceof MutableLogEvent) {
                // MutableLogEvents need to be translated into the RingBuffer by the MUTABLE_TRANSLATOR.
                // That translator calls MutableLogEvent.initFrom to copy the event, which will makeMessageImmutable
                // the message.
                if (translator != MUTABLE_TRANSLATOR) { // should not happen...
                    // TRANSLATOR expects an immutable LogEvent
                    logEvent = ((MutableLogEvent)logEvent).createMemento();
                }
            } else { // custom log event, with a ReusableMessage
                showWarningAboutCustomLogEventWithReusableMessage(logEvent);
            }
        } else { // message is not a ReusableMessage; makeMessageImmutable it to prevent
            // ConcurrentModificationExceptions
            InternalAsyncUtil.makeMessageImmutable(logEvent.getMessage()); // LOG4J2-1988, LOG4J2-1914
        }
        return logEvent;
    }

    private LogEvent ensureImmutable(final LogEvent event) {
        LogEvent result = event;
        if (event instanceof RingBufferLogEvent) {
            // Deal with special case where both types of Async Loggers are used together:
            // RingBufferLogEvents are created by the all-loggers-async type, but
            // this event is also consumed by the some-loggers-async type (this class).
            // The original event will be re-used and modified in an application thread later,
            // so take a snapshot of it, which can be safely processed in the
            // some-loggers-async background thread.
            result = ((RingBufferLogEvent)event).createMemento();
        }
        return result;
    }

    private void showWarningAboutCustomLogEventWithReusableMessage(final LogEvent logEvent) {
        if (!alreadyLoggedWarning) {
            LOGGER.warn("Custom log event of type {} contains a mutable message of type {}." +
                    " AsyncLoggerConfig does not know how to make an immutable copy of this message." +
                    " This may result in ConcurrentModificationExceptions or incorrect log messages" +
                    " if the application modifies objects in the message while" +
                    " the background thread is writing it to the appenders.",
                logEvent.getClass().getName(), logEvent.getMessage().getClass().getName());
            alreadyLoggedWarning = true;
        }
    }

    @Override
    public EventRoute getEventRoute(final Level logLevel) {
        final int remainingCapacity = remainingDisruptorCapacity();
        if (remainingCapacity < 0) {
            return EventRoute.DISCARD;
        }

        return asyncQueueFullPolicy.getRoute(backgroundThreadId, logLevel);
    }

    private int remainingDisruptorCapacity() {
        final Disruptor<Log4jEventWrapper> temp = tacDisruptor;
        if (hasLog4jBeenShutDown(temp)) {
            return -1;
        }
        return (int)temp.getRingBuffer().remainingCapacity();
    }

    /**
     * Returns {@code true} if the specified disruptor is null.
     */
    private boolean hasLog4jBeenShutDown(final Disruptor<Log4jEventWrapper> aDisruptor) {
        if (aDisruptor == null) { // LOG4J2-639
            LOGGER.warn("Ignoring log event after log4j was shut down");
            return true;
        }
        return false;
    }

}