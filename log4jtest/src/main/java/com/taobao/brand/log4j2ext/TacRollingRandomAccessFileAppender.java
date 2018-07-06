package com.taobao.brand.log4j2ext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.net.Advertiser;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/7/4 17:40
 */
@Plugin(name = "TacRollingRandomAccessFile", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE,
    printObject = true)
public class TacRollingRandomAccessFileAppender extends RollingRandomAccessFileAppender {

    private ConcurrentHashMap<String, RollingRandomAccessFileAppender> tacFileAppenders = new ConcurrentHashMap<>();

    private static TacBuilder tacBuilder = null;

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {

        boolean stop = super.stop(timeout, timeUnit);

        tacFileAppenders.entrySet().stream().forEach(kv -> {
            kv.getValue().stop();
        });

        tacFileAppenders.clear();

        return stop;
    }

    public static class TacBuilder<B extends Builder<B>> extends Builder<B> {

        @Override
        protected RollingRandomAccessFileAppender createAppenderInstance(String name, Layout layout,
                                                                         Filter filter,
                                                                         RollingRandomAccessFileManager manager,
                                                                         String fileName, String filePattern,
                                                                         boolean ignoreExceptions,
                                                                         boolean immediateFlush, int bufferSize,
                                                                         Advertiser advertiser) {
            return new TacRollingRandomAccessFileAppender(name, layout, filter, manager, fileName, filePattern,
                ignoreExceptions,
                immediateFlush, bufferSize, advertiser);
        }
    }

    private TacRollingRandomAccessFileAppender(String name,
                                               Layout<? extends Serializable> layout,
                                               Filter filter,
                                               RollingRandomAccessFileManager manager,
                                               String fileName, String filePattern,
                                               boolean ignoreExceptions,
                                               boolean immediateFlush, int bufferSize,
                                               Advertiser advertiser) {
        super(name, layout, filter, manager, fileName, filePattern, ignoreExceptions, immediateFlush, bufferSize,
            advertiser);
    }

    /**
     * Write the log entry rolling over the file when required.
     *
     * @param event The LogEvent.
     */
    @Override
    public void append(LogEvent event) {
        RollingRandomAccessFileAppender currentAppender = getCurrentAppender(event);
        if (currentAppender == this) {
            super.append(event);
        } else {
            currentAppender.append(event);
        }

    }

    private RollingRandomAccessFileAppender getCurrentAppender(LogEvent event) {

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

        RollingRandomAccessFileAppender currentAppender = tacFileAppenders.get(name);

        if (currentAppender == null) {

            currentAppender = copyAndBuild(name, suffix);

            tacFileAppenders.put(name, currentAppender);
        }
        return currentAppender;
    }

    /**
     * 复制创建appender
     *
     * @param name
     * @param suffix
     * @return
     */
    private RollingRandomAccessFileAppender copyAndBuild(String name, String suffix) {

        String oldFileName = this.getFileName();
        String fileName = this.getFileName();
        fileName = fileName + suffix;
        String filePattern = this.getFilePattern();
        filePattern = StringUtils.replace(filePattern, oldFileName, fileName);
        TriggeringPolicy policy = tacBuilder.getPolicy();
        Builder builder = RollingRandomAccessFileAppender.newBuilder().withFileName(fileName)
            .withFilePattern(
                filePattern).withName(name).withConfiguration(tacBuilder.getConfiguration()).withPolicy(policy)
            .withLayout(tacBuilder.getLayout());
        RollingRandomAccessFileAppender build = builder.build();

        return build;
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        B b = new TacBuilder<B>().asBuilder();
        if (tacBuilder == null) {
            tacBuilder = (TacBuilder)b;
        }
        return b;
    }
}
