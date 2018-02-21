package com.wildox.calculator;

import com.wildox.loghere.LogHere;

/**
 * Created by Sujay on 20/02/18.
 * helper
 */

public class UtilHelper {

    static String getLongIfPossible(double x) {
        boolean isLong = (x == (long) x);

        if (isLong)
            return String.valueOf((long) x);
        else
            return String.valueOf(x);
    }

    public static String stripLast(String str) {
        int len = str.length() - 1;
        while (len >= 0 && (Character.isDigit(str.charAt(len)) || str.charAt(len) == '.')) {
            len--;
        }
        return str.substring(0, len + 1);
    }

    public static double fetchLast(String str) {

        try {
            double d = Double.valueOf(str);
            LogHere.e("-> " + str + " " + d);
            return d;
        } catch (NumberFormatException e) {

            int len = str.length() - 1;
            while (len != 0 && Character.isDigit(str.charAt(len))) {
                len--;
            }
            return Double.valueOf(str.substring(len + 1, str.length()));
        }
    }

    public static String stripFirst(String str) {

        int len = 0;
        while (len < str.length() && (Character.isDigit(str.charAt(len)) || str.charAt(len) == '.')) {
            len++;
        }
        return str.substring(len);
    }

    public static double fetchFirst(String str) {

        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            int len = 0;
            while (len < str.length() && Character.isDigit(str.charAt(len))) {
                len++;
            }
            return Double.valueOf(str.substring(0, len));
        }
    }

}
