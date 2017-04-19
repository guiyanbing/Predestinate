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

    public static String getHashMethod(Map<String, Object> getParams, Map<String, Object> postParams) {
        StringBuilder builder = new StringBuilder("yuafenba(&^%_=>yuafenba|");
        String[] getKeys;
        String[] postKeys;
        if (getParams != null) {
            getKeys = new String[getParams.size()];
            int index = 0;
            for (Entry<String, Object> entry : getParams.entrySet()) {
                getKeys[index] = entry.getKey();
                index++;
            }
            Arrays.sort(getKeys, String.CASE_INSENSITIVE_ORDER);
            for (int i = 0; i < getKeys.length; i++) {
                String value = getParams.get(getKeys[i]) == null ? "" : getParams.get(getKeys[i]).toString();
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
                String value = postParams.get(postKeys[i]) == null ? "" : postParams.get(postKeys[i]).toString();
                builder.append(value);
                if (i != postParams.size() - 1) {
                    builder.append("|");
                }
            }
        }
        return EncryptUtil.md5(builder.toString());
    }

    private static String getHashMethodJsonStr(Map<String, Object> getParams, String jsonStr) {
        StringBuilder builder = new StringBuilder("yuafenba(&^%_=>yuafenba|");
        String[] getKeys;
        if (getParams != null) {
            getKeys = new String[getParams.size()];
            int index = 0;
            for (Entry<String, Object> entry : getParams.entrySet()) {
                getKeys[index] = entry.getKey();
                index++;
            }
            Arrays.sort(getKeys, String.CASE_INSENSITIVE_ORDER);
            for (int i = 0; i < getKeys.length; i++) {
                String value = getParams.get(getKeys[i]) == null ? "" : getParams.get(getKeys[i]).toString();
                builder.append(value);
                builder.append("|");
            }
        }

        if (jsonStr != null) {
            builder.append(jsonStr);
        }
        return EncryptUtil.md5(builder.toString());
    }

    /**
     * webview拼接接口
     *
     * @param url
     * @param getParams  get 参数
     * @param postParams post 参数
     * @return
     */
    public static String appendUrl(String url, Map<String, Object> getParams, Map<String, Object> postParams) {
        if (getParams == null) getParams = new HashMap<>();
        getParams.put("ts", TimeUtil.getCurrentTimeMil());
        getParams.put("hash", Url_Enc.getHashMethod(getParams, postParams));
        StringBuilder builder = new StringBuilder(url);
        if (!url.contains("?")) {
            builder.append("?");
            boolean flag = false;
            for (Map.Entry<String, Object> entry : getParams.entrySet()) {
                if (flag) {
                    builder.append("&");
                }
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                flag = true;
            }
        }
        return builder.toString();
    }

    /**
     * 拼接缓存接口
     *
     * @param url
     * @param getParams
     * @return
     */
    public static String appendCacheUrl(String url, Map<String, Object> getParams) {
        StringBuilder builder = new StringBuilder(url);
        if (!url.contains("?")) {
            builder.append("?");
            boolean flag = false;
            for (Map.Entry<String, Object> entry : getParams.entrySet()) {
                if (flag) {
                    builder.append("&");
                }
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                flag = true;
            }

            builder.append("&hash=" + Url_Enc.getHashMethod(getParams, null));
        }
        return builder.toString();
    }

    public static String appendJsonUrl(String url, Map<String, Object> getParams, String JsonStr) {
        String ts = TimeUtil.getCurrentTimeMil();
        if (getParams == null) {
            getParams = new HashMap<String, Object>();
        }
        getParams.put("ts", ts);
        String hash = Url_Enc.getHashMethodJsonStr(getParams, JsonStr);
        getParams.put("hash", hash);
        StringBuilder builder = new StringBuilder(url);
        if (!url.contains("?")) {
            builder.append("?");
            boolean flag = false;
            for (Map.Entry<String, Object> entry : getParams.entrySet()) {
                if (flag) {
                    builder.append("&");
                }
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                flag = true;
            }
        }

        //getParams.remove("hash");
        //Log.e("=============", "=========" + builder.toString());
        return builder.toString();
    }

    // ===============================  老缘分吧地址加密 Start ==============================================
    public static String appendOldUrl(String url, Map<String, Object> getParams, Map<String, Object> postParams) {
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
    // ===============================  老缘分吧地址加密 End ==============================================
}