package com.neoniou.swu.util;

/**
 * @author Neo.Zzj
 */
public class XqmUtil {

    /**
     * 转换学期数 3 -> 16, 2 -> 12, 1 -> 3
     * @param xqm
     * @return
     */
    public static String exchange(String xqm) {
        xqm = "3".equals(xqm) ? "16" : xqm;
        xqm = "2".equals(xqm) ? "12" : xqm;
        xqm = "1".equals(xqm) ? "3" : xqm;
        return xqm;
    }
}
