package com.czc.cmq.util;

/**
 * Created by chenzhichao on 19/3/17.
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0)
            return true;
        char empty = ' ';
        int i = 0;
        for (; i < str.length(); i++) {
            if (str.charAt(i) != empty) {
                return false;
            }
        }
        return true;
    }
}
