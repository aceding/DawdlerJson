package com.ace.dawdler.json.utils;

/**
 * 文本相关的工具类。
 */
public class TextUtils {

    /**
     * 判断文本是不是为null或者是空字符串。
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null == str || str.length() <= 0) {
            return true;
        }
        return false;
    }
}
