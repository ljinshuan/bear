package com.taobao.brand.log4j2ext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import java.util.Map;

/**
 * @author jinshuan.li 2018/7/5 19:02
 */
public class TacMutableLogEvent extends MutableLogEvent implements TacLogEvent {

    private String msCode;

    private Boolean syncHandled = false;

    public Boolean getSyncHandled() {
        return syncHandled;
    }

    public void setSyncHandled(Boolean syncHandled) {
        this.syncHandled = syncHandled;
    }

    @Override
    public void initFrom(LogEvent event) {
        super.initFrom(event);

        // 设置msCode
        this.msCode = ThreadLocals.getMsCode();

    }

    /**
     * @return
     */
    @Override
    public String getMsCode() {
        return this.msCode;
    }
}
