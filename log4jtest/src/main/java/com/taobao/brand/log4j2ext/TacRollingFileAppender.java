package com.taobao.brand.log4j2ext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.net.Advertiser;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/9/23 14:17
 */
@Plugin(name = "TacRollingFile", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class TacRollingFileAppender extends RollingFileAppender {

    private ConcurrentHashMap<String, RollingFileAppender> tacFileAppenders = new ConcurrentHashMap<>();

    private static Builder tacBuilder;

    private TacRollingFileAppender(String name,
                                   Layout<? extends Serializable> layout,
                                   Filter filter,
                                   RollingFileManager manager,
                                   String fileName, String filePattern, boolean ignoreExceptions,
                                   boolean immediateFlush,
                                   Advertiser advertiser) {

        super(name, layout, filter, manager, fileName, filePattern, ignoreExceptions, immediateFlush, advertiser);
    }

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {

        tacFileAppenders.entrySet().stream().forEach(d -> {

            d.getValue().stop();

        });

        tacFileAppenders.clear();

        return super.stop(timeout, timeUnit);
    }

    /**
     * @param <B>
     */
    public static class Builder<B extends Builder<B>> extends RollingFileAppender.Builder<B> {

        @Override
        protected RollingFileAppender createRollingFileAppender(Layout<? extends Serializable> layout,
                                                                RollingFileManager manager) {

            return new TacRollingFileAppender(getName(), layout, getFilter(), manager, fileName, filePattern,
                isIgnoreExceptions(), isImmediateFlush(), advertise ? getConfiguration().getAdvertiser() : null);
        }
    }

    /**
     * Creates a new Builder.
     *
     * @return a new Builder.
     * @since 2.7
     */
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {

        B b = new Builder<B>().asBuilder();
        if (tacBuilder == null) {
            tacBuilder = b;
        }
        return b;
    }

    @Override
    public void append(LogEvent event) {

        RollingFileAppender currentAppender = getCurrentAppender(event);

        if (currentAppender == this) {
            super.append(event);
        } else {
            currentAppender.append(event);
        }
    }

    public RollingFileAppender getCurrentAppender(LogEvent event) {

        String msCode = ThreadLocals.getMsCode();

        if (StringUtils.isEmpty(msCode)) {

            if (event instanceof TacLogEvent) {
                msCode = ((TacLogEvent)event).getMsCode();
            }
        }

        if (StringUtils.isEmpty(msCode)) {
            return this;
        }
        String name = this.getName();

        String suffix = "." + msCode;
        name = name + suffix;

        RollingFileAppender currentAppender = tacFileAppenders.get(name);

        if (currentAppender == null) {

            currentAppender = copyAndBuild(name, suffix);

            tacFileAppenders.put(name, currentAppender);
        }
        return currentAppender;
    }

    /**
     * @param name
     * @param suffix
     * @return
     */
    private RollingFileAppender copyAndBuild(String name, String suffix) {

        String oldFileName = this.getFileName();
        String fileName = this.getFileName();
        fileName = fileName + suffix;
        String filePattern = this.getFilePattern();
        filePattern = StringUtils.replace(filePattern, oldFileName, fileName);
        TriggeringPolicy policy = tacBuilder.getPolicy();
        RollingFileAppender.Builder builder = RollingFileAppender.newOriginBuilder().withFileName(fileName)
            .withFilePattern(
                filePattern).withName(name).withConfiguration(tacBuilder.getConfiguration()).withPolicy(policy)
            .withLayout(tacBuilder.getLayout());
        RollingFileAppender build = builder.build();

        return build;
    }
}
