package com.taobao.brand.bear.cls;

import org.joda.time.DateTime;

/**
 * @author jinshuan.li 2018/8/29 00:10
 */
public class TacHandlerImpl implements TacHandler {
    @Override
    public Object execute() {

        DateTime now = DateTime.now();

        System.out.println(now);

        return now.getDayOfWeek();
    }
}
