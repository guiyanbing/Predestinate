package com.juxin.predestinate.ui.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.util.SDCardUtil;

import org.json.JSONObject;

import java.io.File;

public class Common {
    private static InputMethodManager imm;

    //缓存目录
    public static String getCahceDir() {
        return getCahceDir(App.context, "");
    }

    public static String getCahceDir(String dir) {
        return getCahceDir(App.context, dir);
    }

    public static String getCahceDir(Context context, String dir) {
        String savePath = "";
        try {
            if (SDCardUtil.isSdcardExist()) {
                savePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                savePath = context.getFilesDir().getAbsolutePath();
            }

            if (!savePath.endsWith(File.separator))
                savePath = savePath + File.separator;

            savePath = savePath + context.getResources().getString(R.string.app_storage_name);

            if (!TextUtils.isEmpty(dir)) {
                if (!dir.startsWith(File.separator))
                    savePath = savePath + File.separator;

                savePath = savePath + dir;
            }

            if (!savePath.endsWith(File.separator))
                savePath = savePath + File.separator;

            File file = new File(savePath);
            if (!file.exists() || !file.isDirectory()) {
                if (!file.mkdirs()) {
                    savePath = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savePath;
    }

    public static JSONObject joinJSON(JSONObject json1, JSONObject json2) {
        JSONObject json = null;
        try {
            json = new JSONObject(json1.toString());
            int len = json2.length();
            for (int i = 0; i < len; i++) {
                json.put(json2.names().getString(i), json2.opt(json2.names().getString(i)));
            }
        } catch (Exception e) {
        }
        return json;
    }

    //强制隐藏键盘
    public static void hideKeyBoard(Context context, View view) {
        if (imm == null)
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //强制显示键盘
    public static void showKeyBoard(Context context, View view) {
        if (imm == null)
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null)
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * ListView是否已到底部，非精准判断
     *
     * @param listView
     * @return
     */
    public static boolean isListViewReachBottomEdge(final ListView listView) {
        boolean result = false;

        //忽略新到来的一条消息，和最后一条消息的高度
        if (listView.getLastVisiblePosition() >= (listView.getCount() - 3))
            result = true;

        return result;
    }
}
