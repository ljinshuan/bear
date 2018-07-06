package com.taobao.brand.log4j2ext;

/**
 * @author jinshuan.li 2018/7/5 12:54
 */
public class ThreadLocals {

    private static ThreadLocal<String> msCodes = new ThreadLocal<>();

    /**
     * @param msCode
     */
    public static void setMsCode(String msCode) {

        msCodes.set(msCode);
    }

    /**
     * @return
     */
    public static String getMsCode() {

        String s = msCodes.get();

        return s;
    }

}
