package com.juxin.predestinate.module.logic.baseui;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.picker.common.popup.BottomPopup;
import com.juxin.predestinate.module.util.TimerUtil;


/**
 * 进度条
 * Created by Kind on 16/3/25.
 */
public class LoadingDialog extends BottomPopup {

    private static int resId = -1;
    private static String loadingTxt;
    private static View.OnClickListener onCancelListener;

    /**
     * Instantiates a new Confirm popup.
     *
     * @param activity the activity
     */
    public LoadingDialog(FragmentActivity activity) {
        super(activity);
    }

    private static LoadingDialog loadingDialog;

    /**
     * 显示不带取消按钮的loading弹框
     */
    public static void show(FragmentActivity activity) {
        show(activity, null, -1, null);
    }

    /**
     * 显示带取消按钮的默认loading弹框
     *
     * @param onClickListener 取消按钮的点击事件
     */
    public static void show(FragmentActivity activity, View.OnClickListener onClickListener) {
        show(activity, null, -1, onClickListener);
    }

    /**
     * 显示不带取消按钮的loading弹框
     *
     * @param loadingTxt loading的显示文字
     */
    public static void show(FragmentActivity activity, String loadingTxt) {
        show(activity, loadingTxt, -1, null);
    }

    /**
     * 显示带取消按钮的默认loading弹框
     *
     * @param loadingTxt      loading的显示文字
     * @param onClickListener 取消按钮的点击事件
     */
    public static void show(FragmentActivity activity, String loadingTxt, View.OnClickListener onClickListener) {
        show(activity, loadingTxt, -1, onClickListener);
    }

    /**
     * 显示loading
     */
    public static void show(FragmentActivity activity, String text, int id, View.OnClickListener onClickListener) {
        loadingTxt = text;
        resId = id;
        onCancelListener = onClickListener;

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
        }
        loadingDialog.setWidth(-1);
        loadingDialog.show();
    }

    /**
     * 关闭loading
     */
    public static void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 关闭loading
     *
     * @param tm 延迟关闭时间
     */
    public static void closeLoadingDialog(int tm) {
        TimerUtil.beginTime(new TimerUtil.CallBack() {
            @Override
            public void call() {
                closeLoadingDialog();
            }
        }, tm);
    }

    /**
     * 关闭loading
     *
     * @param tm       延迟关闭时间
     * @param callBack 延迟结束后进行的操作回调
     */
    public static void closeLoadingDialog(int tm, final TimerUtil.CallBack callBack) {
        TimerUtil.beginTime(new TimerUtil.CallBack() {
            @Override
            public void call() {
                closeLoadingDialog();
                callBack.call();
            }
        }, tm);
    }

    @Override
    protected View makeContentView() {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.common_loading_dialog, null);
        TextView loading_txt = (TextView) inflate.findViewById(R.id.loading_txt);
        View loading_gif = inflate.findViewById(R.id.loading_gif);
        View cancel = inflate.findViewById(R.id.cancel);

        if (resId != -1) loading_gif.setBackgroundResource(resId);
        loading_txt.setText(loadingTxt);
        loading_txt.setVisibility(TextUtils.isEmpty(loadingTxt) ? View.GONE : View.VISIBLE);
        cancel.setVisibility(onCancelListener == null ? View.GONE : View.VISIBLE);
        if (onCancelListener != null) cancel.setOnClickListener(onCancelListener);

        return inflate;
    }
}
