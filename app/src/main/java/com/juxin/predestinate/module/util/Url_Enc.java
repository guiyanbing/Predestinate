package com.juxin.predestinate.module.util;

import com.juxin.library.utils.EncryptUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 加密
 *
 * @author Kind
 */
public class Url_Enc {

    /**
     * 对请求参数进行hash拼接
     *
     * @param getParams  get参数
     * @param postParams post参数
     * @return 加密之后的数值
     */
    private static String getHashMethod(Map<String, Object> getParams, Map<String, Object> postParams) {
        StringBuilder builder = new StringBuilder("yuafenba(&^%_=>yuafenba|");
        String[] getKeys = null;
        String[] postKeys = null;
        if (getParams != null) {
            getKeys = new String[getParams.size()];
            int index = 0;
            for (Entry<String, Object> entry : getParams.entrySet()) {
                getKeys[index] = entry.getKey();
                index++;
            }
            Arrays.sort(getKeys, String.CASE_INSENSITIVE_ORDER);
            for (int i = 0; i < getKeys.length; i++) {
                String value = String.valueOf(getParams.get(getKeys[i]));
                builder.append(value);
                builder.append("|");
            }
        }

        if (postParams != null) {
            postKeys = new String[postParams.size()];
            int index = 0;
            for (Entry<String, Object> entry : postParams.entrySet()) {
                postKeys[index] = entry.getKey();
                index++;
            }
            Arrays.sort(postKeys, String.CASE_INSENSITIVE_ORDER);
            for (int i = 0; i < postKeys.length; i++) {
                String value = String.valueOf(postParams.get(postKeys[i]));
                builder.append(value);
                if (i != postParams.size() - 1) {
                    builder.append("|");
                }
            }
        }
        return EncryptUtil.md5(builder.toString());
    }

    /**
     * 拼接请求url
     *
     * @param url        请求url
     * @param getParams  get参数
     * @param postParams post参数
     * @return 加密之后的请求url
     */
    public static String appendUrl(String url, Map<String, Object> getParams, Map<String, Object> postParams) {
        if (getParams == null) getParams = new HashMap<>();
        getParams.put("ts", TimeUtil.getCurrentTimeMil());
        getParams.put("hash", Url_Enc.getHashMethod(getParams, postParams));
        StringBuilder builder = new StringBuilder(url);
        builder.append(!url.contains("?") ? "?" : "&");
        boolean flag = false;
        for (Entry<String, Object> entry : getParams.entrySet()) {
            if (flag) builder.append("&");
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            flag = true;
        }
        return builder.toString();
    }
}