package com.juxin.predestinate.module.util;

import android.util.Base64;

import com.juxin.library.utils.EncryptUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

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

    public static String encryptDES(String encryptString) throws Exception {
        return encryptDES(encryptString, "yfb+-73+");
    }

    public static String decryptDES(String encryptString) throws Exception {
        return decryptDES(encryptString, "yfb+-73+");
    }

    //第二用加密方式
    public static String encryptDESSecond(String encryptString) throws Exception {
        return encryptDESPCK(encryptString, "yuasa%kk");
    }

    //第二用解密方式
    public static String decryptDESSecond(String encryptString) throws Exception {
        return decryptDESPCK(encryptString, "yuasa%kk");
    }

    /**
     * 加密
     *
     * @param encryptString 需要加密的串
     * @param encryptKey    加密Key
     * @return
     * @throws Exception
     */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.getBytes());
        DESKeySpec desKey = new DESKeySpec(encryptKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    /**
     * 解密
     *
     * @param decryptString 需要解密的串
     * @param decryptKey    加密Key
     * @return
     * @throws Exception
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(decryptKey.getBytes());
        byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
        DESKeySpec desKey = new DESKeySpec(decryptKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, securekey, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }

    /**
     * 加密
     *
     * @param encryptString 需要加密的串
     * @param encryptKey    加密Key
     * @return
     * @throws Exception
     */
    public static String encryptDESPCK(String encryptString, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.getBytes());
        DESKeySpec desKey = new DESKeySpec(encryptKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Base64.encodeToString(encryptedData, Base64.NO_WRAP);
    }

    /**
     * 解密
     *
     * @param decryptString 需要解密的串
     * @param decryptKey    加密Key
     * @return
     * @throws Exception
     */
    public static String decryptDESPCK(String decryptString, String decryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(decryptKey.getBytes());
        byte[] byteMi = Base64.decode(decryptString, Base64.NO_WRAP);
        DESKeySpec desKey = new DESKeySpec(decryptKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
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