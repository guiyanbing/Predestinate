package com.juxin.predestinate.module.util;

public class JniUtil {
    static {
        System.loadLibrary("JniDes");
    }

    public static native String GetEncryptString(String data);

    public static native String GetDecryptString(String data);
}
