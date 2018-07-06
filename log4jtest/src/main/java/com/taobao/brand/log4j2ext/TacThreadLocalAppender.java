package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/7/5 22:47
 */
@Plugin(name = "TacThreadLocalAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE,
    printObject = true)
public class TacThreadLocalAppender extends AbstractOutputStreamAppender {

    private Charset defaultCharset = Charset.forName("utf-8");

    /**
     * Instantiates a WriterAppender and set the output destination to a new {@link OutputStreamWriter} initialized with
     * <code>os</code> as its {@link OutputStream}.
     *
     * @param name             The name of the Appender.
     * @param layout           The layout to format the message.
     * @param filter
     * @param ignoreExceptions
     * @param immediateFlush
     * @param manager          The OutputStreamManager.
     */
    protected TacThreadLocalAppender(String name, Layout layout,
                                     Filter filter, boolean ignoreExceptions,
                                     boolean immediateFlush,
                                     OutputStreamManager manager) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
    }

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {

        return true;
    }

    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B>
        implements org.apache.logging.log4j.core.util.Builder<TacThreadLocalAppender> {

        public Builder() {
            super();
            withBufferSize(RollingRandomAccessFileManager.DEFAULT_BUFFER_SIZE);
            withIgnoreExceptions(true);
            withImmediateFlush(true);
        }

        @Override
        public TacThreadLocalAppender build() {
            final String name = getName();
            if (name == null) {
                LOGGER.error("No name provided for FileAppender");
                return null;
            }

            return new TacThreadLocalAppender(name, getOrCreateLayout(), getFilter(), isIgnoreExceptions(),
                isImmediateFlush(), null);
        }
    }

    @Override
    public void append(LogEvent event) {
        super.append(event);
    }

    @Override
    protected void directEncodeEvent(LogEvent event) {
        this.writeByteArrayToManager(event);
    }

    @Override
    protected void writeByteArrayToManager(final LogEvent event) {
        final byte[] bytes = getLayout().toByteArray(event);
        if (bytes != null && bytes.length > 0) {

            String data = new String(bytes, defaultCharset);
        }
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        B b = new Builder<B>().asBuilder();
        return b;
    }
}
