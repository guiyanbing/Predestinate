package com.juxin.library.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 字符串工具类
 * <p/>
 * 目前包括：
 * 1:  由标志位截取字符串，
 * 2:  字符串按分隔符转换为数组
 * 3:  去除字符串中空格
 * <p/>
 * 如有特殊需求请自行添加
 * <p/>
 * Created by Su on 2016/7/4.
 */
public final class StringUtils {

    public static final String EMPTY = "";      // 默认空值字符串

    /**
     * 截取并保留标志位之前的字符串, 不包含标志位
     *
     * @param str  需截取字符串
     * @param expr 标志位
     * @return
     */
    public static String getBeforeNoFlag(String str, String expr) {
        return substringBefore(str, expr, false);
    }

    /**
     * 截取并保留标志位之前的字符串, 包含标志位
     *
     * @param str  需截取字符串
     * @param expr 标志位
     * @return
     */
    public static String getBeforeWithFlag(String str, String expr) {
        return substringBefore(str, expr, true);
    }

    /**
     * 截取并保留标志位之前的字符串
     *
     * @param isReserve 截取字符串中是否包括标志位： true 包括 ， false 不包括
     */
    private static String substringBefore(String str, String expr, boolean isReserve) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(expr)) {
            return str;
        }

        int pos = str.indexOf(expr);
        if (pos == -1) {
            return str;
        }

        if (isReserve) {
            return str.substring(0, pos + expr.length());
        }

        return str.substring(0, pos);
    }

    /**
     * 截取标志位之后的字符串, 不保留标志位
     *
     * @param str  字符串
     * @param expr 标志位
     * @return
     */
    public static String getAfterNoFlag(String str, String expr) {
        return substringAfter(str, expr, false);
    }


    /**
     * 截取标志位之后的字符串,保留标志位
     *
     * @param str  字符串
     * @param expr 标志位
     * @return
     */
    public static String getAfterWithFlag(String str, String expr) {
        return substringAfter(str, expr, true);
    }

    /**
     * 截取并保留标志位之后的字符串
     *
     * @param isReserve 截取字符串中是否包括标志位： true 包括 ， false 不包括
     */
    private static String substringAfter(String str, String expr, boolean isReserve) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(expr)) {
            return str;
        }

        int pos = str.indexOf(expr);
        if (pos == -1) {
            return EMPTY;
        }
        if (isReserve) {
            return str.substring(pos);
        }
        return str.substring(pos + expr.length());
    }

    /**
     * 截取并保留最后一个标志位之前的字符串： 不包括标志位
     *
     * @param str  字符串
     * @param expr 标志位
     * @return
     */
    public static String getBeforeLastNoFlag(String str, String expr) {
        return substringBeforeLast(str, expr, false);
    }

    /**
     * 截取并保留最后一个标志位之前的字符串： 包括标志位
     *
     * @param str  字符串
     * @param expr 标志位
     * @return
     */
    public static String getBeforeLastWithFlag(String str, String expr) {
        return substringBeforeLast(str, expr, true);
    }

    /**
     * 截取并保留最后一个标志位之前的字符串
     *
     * @param isReserve 截取字符串中是否包括标志位： true 包括 ， false 不包括
     * @return
     */
    private static String substringBeforeLast(String str, String expr, boolean isReserve) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(expr)) {
            return str;
        }
        int pos = str.lastIndexOf(expr);
        if (pos == -1) {
            return str;
        }
        if (isReserve) {
            return str.substring(0, pos + expr.length());
        }

        return str.substring(0, pos);
    }

    /**
     * 截取并保留最后一个标志位之后的字符串： 不包括标志位
     *
     * @param str
     * @param expr 标志位
     * @return
     */
    public static String getAfterLastNoFlag(String str, String expr) {
        return substringAfterLast(str, expr, false);
    }

    /**
     * 截取并保留最后一个标志位之后的字符串： 包括标志位
     *
     * @param str
     * @param expr 标志位
     * @return
     */
    public static String getAfterLastWithFlag(String str, String expr) {
        return substringAfterLast(str, expr, true);
    }

    /**
     * 截取并保留最后一个标志位之后的字符串
     *
     * @param isReserve 截取字符串中是否包括标志位： true 包括 ， false 不包括
     * @return
     */
    private static String substringAfterLast(String str, String expr, boolean isReserve) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(expr)) {
            return str;
        }

        int pos = str.lastIndexOf(expr);
        if (pos == -1 || pos == (str.length() - expr.length())) {
            return EMPTY;
        }
        if (isReserve) {
            return str.substring(pos);
        }
        return str.substring(pos + expr.length());
    }

    /**
     * 把字符串按分隔符转换为数组
     *
     * @param string 字符串
     * @param expr   分隔符
     * @return
     */
    public static String[] stringToArray(String string, String expr) {
        return string.split(expr);
    }

    /**
     * 去除字符串中的空格
     *
     * @param str
     * @return
     */
    public static String noSpace(String str) {
        str = str.trim();
        str = str.replace(" ", "");
        return str;
    }

    /**
     * 重要,支付专用,别的地方请不要用,出问题自负
     *
     * @return
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String key = format.format(new Date());
        int i = new Random().nextInt(89999) + 10000;
        String str = key + i;
        return str;
    }
}
