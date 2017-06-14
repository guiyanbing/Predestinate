package com.juxin.library.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.juxin.library.image.ImageLoader;

/**
 * 用于状态切换的布局，只显示1个状态
 *
 * @author zijunna
 */
public class CustomFrameLayout extends FrameLayout {

    private int[] list;

    private OnShowChangeListener showChangeListener;
    private OnShowOfIndexChangeListener showOfIndexChangeListener;

    public CustomFrameLayout(Context context) {
        this(context, null);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void setShowChangeListener(OnShowChangeListener showChangeListener) {
        this.showChangeListener = showChangeListener;
    }

    public void setShowOfIndexChangeListener(OnShowOfIndexChangeListener showOfIndexChangeListener) {
        this.showOfIndexChangeListener = showOfIndexChangeListener;
    }

    /**
     * 设置子面板id数组
     *
     * @param list 包含的子view的id数组
     */
    public void setList(int[] list) {
        this.list = list;
        show(0);
    }

    /**
     * 显示某个面板
     *
     * @param id 要显示view的id
     */
    public void show(int id) {
        if (list == null) {
            for (int i = 0; i < getChildCount(); ++i) {
                View view = getChildAt(i);
                view.setVisibility(id == view.getId() ? View.VISIBLE : View.GONE);
            }
        } else {
            for (int aList : list) {
                View item = findViewById(aList);
                if (item == null) continue;

                item.setVisibility(aList == id ? View.VISIBLE : View.GONE);
            }
        }
        if (showChangeListener != null)
            showChangeListener.onChange(this, id);
    }

    /**
     * 隐藏所有面板
     */
    public void GoneAll() {
        if (list == null) {
            for (int i = 0; i < getChildCount(); ++i) {
                View view = getChildAt(i);
                view.setVisibility(View.GONE);
            }
        } else {
            for (int aList : list) {
                View item = findViewById(aList);
                if (item == null) continue;
                item.setVisibility(View.GONE);
            }
        }
        if (showChangeListener != null)
            showChangeListener.onChange(this, 0);
    }

    /**
     * 切换显示面板
     *
     * @param index 子view在父view中的index
     */
    public void showOfIndex(int index) {
        for (int i = 0; i < getChildCount(); ++i) {
            View view = getChildAt(i);
            view.setVisibility(index == i ? View.VISIBLE : View.GONE);
        }
        if (showOfIndexChangeListener != null)
            showOfIndexChangeListener.onChange(this, index);
    }

    /**
     * 获取正在显示的id
     *
     * @return 正在显示view的id
     */
    public int getVisibleId() {
        if (list == null) {
            for (int i = 0; i < getChildCount(); ++i) {
                View view = getChildAt(i);
                if (view.getVisibility() == View.VISIBLE) return view.getId();
            }
            return 0;
        } else {
            for (int aList : list) {
                View item = findViewById(aList);
                if (item == null) continue;
                if (item.getVisibility() == View.VISIBLE) return aList;
            }
            return 0;
        }
    }

    /**
     * 预留方法
     */
    public void initView() {
    }

    /**
     * 显示Loading面板
     *
     * @param layoutId
     * @param imgId
     * @param imgRes
     */
    public void showLoading(int layoutId, int imgId, int imgRes) {
        show(layoutId);

        View img = findViewById(imgId);
        if (img == null || !(img instanceof ImageView))
            return;
        ImageLoader.loadFitCenter(img.getContext(), imgRes, (ImageView) img, 0, 0);
    }

    /**
     * 停止Gif
     *
     * @param imgId
     */
    public void stopLoading(int imgId) {
        View img = findViewById(imgId);
        if (img == null || !(img instanceof ImageView))
            return;
        Drawable drawable = ((ImageView) img).getDrawable();
        if (drawable == null || !(drawable instanceof GifDrawable))
            return;
        ((GifDrawable) drawable).stop();
        ((ImageView) img).setImageDrawable(null);
    }

    public interface OnShowChangeListener {
        void onChange(CustomFrameLayout view, int id);
    }

    public interface OnShowOfIndexChangeListener {
        void onChange(CustomFrameLayout view, int id);
    }
}
