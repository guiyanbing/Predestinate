package com.juxin.predestinate.module.util;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;

import com.juxin.library.log.PToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ChineseFilter {

    /**
     * @param string 中文字符串
     * @return 将中文字符串转换为Unicode编码的字符串
     */
    public static String toUnicode(final String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * 判断是否为中文字符
     *
     * @param c 传入的char
     * @return 是否为中文字符
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }

        return false;
    }

    public static String filter(String str) {
        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(str);
        String repickStr = mat.replaceAll("");
        return repickStr;
    }

    /**
     * 判断是否为手机号码
     */
    public static boolean isPhoneNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0])|(17[6-8])|(18[0,0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 检查Email的格式
     */
    public static boolean checkEmailFormat(String email) {
        String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * 判断是否为QQ号码
     *
     * @auther Kyle.DY
     * @date 2016-1-26 11:53
     */
    public static boolean isQQNumber(String qq) {
        Pattern p = Pattern.compile("^[1-9][0-9]{5,12}$");
        Matcher m = p.matcher(qq);
        return m.matches();
    }

    /**
     * 判断是否为合法微信号码
     *
     * @param wechat
     * @return
     * @auther Kyle.DY
     * @date 2016-1-28 14:29
     */
    public static boolean isWeChatNumber(String wechat) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_]{5,25}$");
        Matcher m = p.matcher(wechat);
        return m.matches();
    }

    /**
     * 过滤输入的中文字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String chineseFilter(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 过滤输入的特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 使用正则表达式去掉字符串末尾多余的.与0
     */
    public static String subZeroString(String s) {
        if (!TextUtils.isEmpty(s) && s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 将JSONObject转换成Map<String, Object>
     *
     * @param jsonObject JSONObject对象
     * @return Map对象
     */
    public static Map<String, Object> JSONObjectToMap(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        if (jsonObject == null) return result;

        Iterator<String> iterator = jsonObject.keys();
        String key = null;
        Object value = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            value = jsonObject.opt(key);
            result.put(key, value);
        }
        return result;
    }

    /**
     * 复制字符串到剪贴板，并提示：已复制到剪贴板
     *
     * @param copy_string 需要赋值的文字
     */
    public static void copyString(Context context, String copy_string) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(copy_string);
        PToast.showShort("已复制到剪贴板");
    }
}
