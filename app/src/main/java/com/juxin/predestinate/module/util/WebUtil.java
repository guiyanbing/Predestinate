package com.juxin.predestinate.module.util;

import android.text.TextUtils;

import com.juxin.predestinate.module.logic.application.App;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * html及url拼接工具类
 * Created by ZRP on 2017/5/3.
 */
public class WebUtil {

    /* url正则匹配字符串 */
    private static final String URL_PATTERN = "^((https|http)?:\\/\\/)[^\\s]+";

    /**
     * 判断字符串是否为url
     *
     * @param input 需要判断的字符串
     * @return
     */
    public static boolean isUrl(String input) {
        if (TextUtils.isEmpty(input)) return false;

        return Pattern.matches(URL_PATTERN, input);
    }

    /**
     * 拼接url参数，控制刷新时间及清晰度
     *
     * @param url 需要拼接的url
     * @return 拼接完成的url
     */
    public static String jointUrl(String url) {
        return url + "?resolution=" + (PerformanceHelper.isHighPerformance(App.context) ? "2" : "1")
                + "&v=" + System.currentTimeMillis();
    }

    /**
     * 拼接带参数的url
     *
     * @param url    需要拼接的url
     * @param params 请求参数
     * @return 拼接完成的url
     */
    public static String jointUrl(String url, HashMap<String, Object> params) {
        String joint = jointUrl(url);
        if (params == null || params.isEmpty()) return joint;

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            joint += "&" + entry.getKey() + "=" + String.valueOf(entry.getValue());
        }
        return joint;
    }
}
