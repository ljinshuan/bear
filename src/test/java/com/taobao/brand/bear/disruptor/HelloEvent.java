package com.taobao.brand.bear.disruptor;

import com.lmax.disruptor.EventFactory;
import lombok.Data;

/**
 * @author jinshuan.li 2018/8/5 11:03
 */
@Data
public class HelloEvent {

    private String name;

}

class HelloEventFactory implements EventFactory<HelloEvent> {

    @Override
    public HelloEvent newInstance() {
        return new HelloEvent();
    }
}


