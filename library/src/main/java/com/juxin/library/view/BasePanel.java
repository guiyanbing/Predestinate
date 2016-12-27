package com.juxin.library.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 模拟activity方法的view处理父类
 * Created by ZRP on 2016/12/2.
 */
public abstract class BasePanel {

    private Context context;
    private View contentView;

    public BasePanel(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setContentView(@LayoutRes int layoutID) {
        contentView = LayoutInflater.from(context).inflate(layoutID, null);
    }

    public View getContentView() {
        return contentView;
    }

    public View findViewById(@IdRes int id) {
        if (id < 0 || contentView == null) return null;
        return contentView.findViewById(id);
    }
}
