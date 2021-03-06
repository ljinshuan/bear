package com.taobao.brand.bear.disruptor;

import com.lmax.disruptor.EventHandler;
import com.taobao.brand.bear.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jinshuan.li 2018/8/5 11:05
 */
@Slf4j
public class HelloEventHandler implements EventHandler<HelloEvent> {

    @Override
    public void onEvent(HelloEvent event, long sequence, boolean endOfBatch) throws Exception {


        ThreadUtils.sleep(50000);
        log.info(event.toString());
    }
}
