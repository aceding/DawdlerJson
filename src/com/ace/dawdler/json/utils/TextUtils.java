package com.ace.dawdler.json.utils;

/**
 * util class help handle the text.
 *
 * @author aceding
 */
public class TextUtils {

    /**
     * check the string is null or empty.
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

    /**
     * convert the string's first word to upper case.
     * @param str
     * @return
     */
    public static String firstWord2UpperCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String firstWord = str.substring(0, 1);
        return firstWord.toUpperCase() + str.substring(1);
    }
}
