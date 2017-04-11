package com.juxin.library.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串加密工具类
 */
public final class EncryptUtil {

    /**
     * 对字符串进行md5加密
     *
     * @param source 需要加密的字符串
     * @return 加密之后的字符串
     */
    public static String md5(String source) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(source.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        return StringUtils.hexString(hash);
    }

    /**
     * 对字符串进行sha1加密
     *
     * @param source 需要加密的字符串
     * @return 加密之后的字符串
     */
    public static String sha1(String source) {
        try {
            byte[] hash;
            try {
                hash = MessageDigest.getInstance("SHA-1").digest(source.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Huh, SHA-1 should be supported?", e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Huh, UTF-8 should be supported?", e);
            }

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取字符串的hashCode
     *
     * @param source 原字符串
     * @return hashCode
     */
    public static int getHashCode(String source) {
        if (source == null) return 0;
        return source.hashCode();
    }
}
