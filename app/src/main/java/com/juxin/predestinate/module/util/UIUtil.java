package com.juxin.predestinate.module.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.text.Editable;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.MsgMgr;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.lang.reflect.Field;

/**
 * 封装一些UI的功能，比如需要在主线程中执行的操作。<br>
 * 各种UI的一些操作。
 */
public class UIUtil {

    /**
     * 保证在UI Thread中执行。
     */
    public static void addView(final ViewManager viewManager, final View view, final ViewGroup.LayoutParams params) {
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {// 对activity并未开始创建时的添加行为进行错误拦截
                    viewManager.addView(view, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 保证在UI Thread中执行。
     */
    public static void removeView(final ViewManager viewManager, final View view) {
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewManager.removeView(view);
            }
        });
    }

    /**
     * 在Main UIThread中设置ImageView的src。
     *
     * @param imageView ImageView实例。
     * @param resId     资源Id。
     */
    public static void setImageResource(final ImageView imageView, final int resId) {
        if (imageView == null) {
            return;
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            imageView.setImageResource(resId);
            return;
        }

        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(resId);
            }
        });
    }

    /**
     * 在Main UIThread中设置ImageView的src。
     *
     * @param imageView ImageView实例。
     * @param bm        图片资源。
     */
    public static void setImageBitmap(final ImageView imageView, final Bitmap bm) {
        if (imageView == null) {
            return;
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            imageView.setImageBitmap(bm);
            return;
        }

        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bm);
            }
        });
    }

    /**
     * 通过文件名获取资源Id。
     *
     * @param fileName 资源文件名。
     * @return 资源的Id。
     */
    public static int getResIdFromDrawable(String fileName) {
        try {
            return App.context.getResources().getIdentifier(fileName, "drawable", App.context.getPackageName());
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }

        return 0;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f) - 15;
    }

    /**
     * 获取屏幕密度density
     */
    public static float getDensity() {
        return App.context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕转换的倍数，主要是XML中的dp单位对px的转换
     */
    public static float toDpMultiple(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变。
     */
    public static int px2sp(float pxValue) {
        final float scaledDensity = App.context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(pxValue / scaledDensity);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变。
     */
    public static int sp2px(float spValue) {
        final float scaledDensity = App.context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(spValue * scaledDensity);
    }

    /**
     * 计算手机状态栏的高度
     *
     * @param activity 上下文
     * @return 状态栏的高度
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 70;

        if (activity == null) return statusHeight;

        try {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusHeight = frame.top;

            if (statusHeight == 0) {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                statusHeight = activity.getResources().getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
        return statusHeight;
    }

    /**
     * 根据屏幕宽度及比例计算控件所需的高度
     *
     * @param d 宽高比：高/宽
     * @return 返回控件的高度
     */
    public static int getViewHeight(double d) {
        int screenWidth = ModuleMgr.getAppMgr().getScreenWidth();
        return (int) (screenWidth * d);
    }

    /**
     * 根据屏幕宽度及比例计算控件所需的高度
     *
     * @param d      宽高比：高/宽
     * @param offset 宽度偏移
     */
    public static int getViewHeight(double d, double offset) {
        int screenWidth = ModuleMgr.getAppMgr().getScreenWidth();
        return (int) ((screenWidth - offset) * d);
    }

    /**
     * 设置背景bitmap： 兼容低版本
     */
    public static void setBackground(View v, Drawable background) {
        if (v == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(background);
        } else {
            v.setBackgroundDrawable(background);
        }
    }

    /**
     * 得到压缩后的Bitmap，主要用于Activity中设置大图背景
     */
    public static Bitmap getDecodeBitmap(int resID, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(App.activity.getResources(), resID, options);

        options.inSampleSize = PhotoUtils.calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeResource(App.activity.getResources(), resID, options);
        return bitmap;
    }

    /**
     * edittext光标定位到文字后
     */
    public static void endCursor(EditText editText) {
        editText.requestFocus();
        Editable etext = editText.getText();
        Selection.setSelection(etext, etext.length());
    }
}
