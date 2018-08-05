package com.taobao.brand.bear.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.taobao.brand.bear.utils.ThreadUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshuan.li 2018/8/5 11:09
 */
public class App {

    private static final EventTranslatorOneArg<HelloEvent, String> TRANSLATOR =
        new EventTranslatorOneArg<HelloEvent, String>() {
            public void translateTo(HelloEvent event, long sequence, String bb) {
                event.setName(bb);
            }
        };

    public static void main(String[] args) throws TimeoutException {

        Executor executor = Executors.newCachedThreadPool();

        HelloEventFactory factory = new HelloEventFactory();

        int bufferSize = 1024;

        Disruptor<HelloEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        disruptor.handleEventsWith(new HelloEventHandler());

        disruptor.start();

        disruptor.publishEvent(TRANSLATOR, "ljinshuan");

        disruptor.shutdown(1L, TimeUnit.SECONDS);
    }
}
