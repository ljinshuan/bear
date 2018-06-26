package com.taobao.brand.bear.appender;

import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.status.ErrorStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

import static ch.qos.logback.core.CoreConstants.CODES_URL;

/**
 * @author jinshuan.li 2018/6/26 11:56
 */
public class TacThreadLocalAppender<E> extends UnsynchronizedAppenderBase<E> {

    /**
     * It is the encoder which is ultimately responsible for writing the event to an {@link OutputStream}.
     */
    protected Encoder<E> encoder;

    /**
     * All synchronization in this class is done via the lock object.
     */
    protected final ReentrantLock lock = new ReentrantLock(false);

    /**
     * Checks that requires parameters are set and if everything is in order, activates this appender.
     */
    @Override
    public void start() {
        int errors = 0;
        if (this.encoder == null) {
            addStatus(new ErrorStatus("No encoder set for the appender named \"" + name + "\".", this));
            errors++;
        }

        // only error free appenders should be activated
        if (errors == 0) {
            super.start();
        }
    }

    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }

        subAppend(eventObject);
    }

    protected void writeOut(E event) throws IOException {
        byte[] byteArray = this.encoder.encode(event);
        writeBytes(byteArray);
    }

    private void writeBytes(byte[] byteArray) throws IOException {
        if (byteArray == null || byteArray.length == 0) { return; }

        lock.lock();
        try {

            // TODO: 2018/6/26 观察数据

            String s = new String(byteArray);

            System.out.println(s);

        } finally {
            lock.unlock();
        }
    }

    /**
     * Actual writing occurs here.
     * <p>
     * Most subclasses of <code>WriterAppender</code> will need to override this method.
     *
     * @since 0.9.0
     */
    protected void subAppend(E event) {
        if (!isStarted()) {
            return;
        }
        try {
            // this step avoids LBCLASSIC-139
            if (event instanceof DeferredProcessingAware) {
                ((DeferredProcessingAware)event).prepareForDeferredProcessing();
            }
            // the synchronization prevents the OutputStream from being closed while we
            // are writing. It also prevents multiple threads from entering the same
            // converter. Converters assume that they are in a synchronized block.
            // lock.lock();

            byte[] byteArray = this.encoder.encode(event);
            writeBytes(byteArray);

        } catch (IOException ioe) {
            // as soon as an exception occurs, move to non-started state
            // and add a single ErrorStatus to the SM.
            this.started = false;
            addStatus(new ErrorStatus("IO failure in appender", this, ioe));
        }
    }

    public Encoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

}
