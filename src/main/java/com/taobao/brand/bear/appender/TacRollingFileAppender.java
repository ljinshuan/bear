package com.taobao.brand.bear.appender;

import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jinshuan.li 27/05/2018 10:57
 */
public class TacRollingFileAppender<E> extends RollingFileAppender<E> {

    private ConcurrentHashMap<String, RollingFileAppender> tacFileAppenders = new ConcurrentHashMap<>();

    @Override
    public void stop() {
        super.stop();

        tacFileAppenders.entrySet().stream().forEach(kv -> {
            kv.getValue().stop();
        });

        tacFileAppenders.clear();
    }

    @Override
    protected void subAppend(E event) {
        super.subAppend(event);

        RollingFileAppender currentAppender = this.getCurrentAppender();

        String name = Thread.currentThread().getName();
        if (currentAppender != null) {
            currentAppender.doAppend(event);
        }
    }

    private RollingFileAppender getCurrentAppender() {

        String currentMs = "abc";

        String key = this.getFile() + "." + currentMs;

        RollingFileAppender rollingFileAppender = tacFileAppenders.get(key);

        if (rollingFileAppender == null) {

            rollingFileAppender = new RollingFileAppender();
            rollingFileAppender.setContext(this.getContext());
            rollingFileAppender.setFile(key);

            TimeBasedRollingPolicy newRolePolicy = new TimeBasedRollingPolicy();
            newRolePolicy.setFileNamePattern(key + ".%d{yyyy-MM-dd}");
            newRolePolicy.setContext(this.getContext());
            newRolePolicy.setParent(rollingFileAppender);
            newRolePolicy.start();
            rollingFileAppender.setRollingPolicy(newRolePolicy);
            rollingFileAppender.setTriggeringPolicy(newRolePolicy);
            rollingFileAppender.setName(this.getName() + "." + "abc");
            rollingFileAppender.setEncoder(this.getEncoder());
            rollingFileAppender.start();

            tacFileAppenders.putIfAbsent(key, rollingFileAppender);

        }

        return rollingFileAppender;
    }
}
