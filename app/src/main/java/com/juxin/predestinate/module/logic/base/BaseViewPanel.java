package com.juxin.predestinate.module.logic.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Kind on 16/2/27.
 */
public class BaseViewPanel {
    public Context context;
    public int layoutId;
    public View contentView;

    public BaseViewPanel() {
        super();
    }

    public BaseViewPanel(Context context) {
        this.context = context;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 获取context
     *
     * @return
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * 查找组件
     *
     * @param id
     * @return
     */
    public View findViewById(int id) {
        return contentView.findViewById(id);
    }

    /**
     * 设置内容
     *
     * @param layoutId
     */
    public void setContentView(int layoutId) {
        this.layoutId = layoutId;
        contentView = createView(layoutId);
        onGlobalLayoutListener();
    }

    /**
     * 设置内容
     *
     * @param
     */
    public void setContentView(View contentView) {
        this.contentView = contentView;
        onGlobalLayoutListener();
    }


    /**
     * 监听被添加到屏幕的事件，可获取到高度和宽度
     */
    public void onGlobalLayoutListener() {
        ViewTreeObserver viewTreeObserver = getContentView().getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
    }


    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            if (onSizeListener != null) {
                onSizeListener.call(BaseViewPanel.this, getContentView().getWidth(), getContentView().getHeight());
                //只调用1次
                onSizeListener = null;
                getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
            }
        }
    };


    /**
     * 外部监听获取宽高
     */
    private OnSizeListener onSizeListener;

    public interface OnSizeListener {
        void call(BaseViewPanel baseViewPanel, int width, int height);
    }

    public void getSize(OnSizeListener onSizeListener) {
        this.onSizeListener = onSizeListener;

        //添加事件
        if (getContentView() != null) {
            onGlobalLayoutListener();
        }
    }


    /**
     * 获取内容
     *
     * @return
     */
    public View getContentView() {
        return this.contentView;
    }

    /**
     * 创建View
     *
     * @param resource
     * @return
     */
    public View createView(int resource) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);
        return view;
    }

    /**
     * 设置显示
     *
     * @param visibility
     */
    public void setVisibility(int visibility) {
        if (contentView != null) {
            contentView.setVisibility(visibility);
        }
    }


    public void setOnClick(int id, View.OnClickListener listener) {
        contentView.findViewById(id).setOnClickListener(listener);
    }

}
