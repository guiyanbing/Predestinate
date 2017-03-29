package com.juxin.predestinate.bean.db.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Kind on 2017/3/28.
 */
@SuppressWarnings("WeakerAccess")
public class CloseUtil {

    private CloseUtil() { }

    public static void close(Closeable... params) {
        if (null != params) {
            try {
                for (Closeable closeable : params) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}