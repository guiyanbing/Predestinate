package com.juxin.library.unread;

import android.content.Context;
import android.support.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;

/**
 * 未读角标单独使用的SP存储文件
 * Created by ZRP on 2016/11/30.
 */
public class UnreadPreferences extends TrayPreferences {

    private static final String MODULE_NAME = "unread";
    private static final int VERSION = 1;

    public UnreadPreferences(@NonNull Context context) {
        super(context, MODULE_NAME, VERSION);
    }
}
