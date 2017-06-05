package com.juxin.predestinate.ui.discover.cardimage;

import android.content.Context;
import android.view.View;

/**
 * Created by zm on 2017/6/2
 */
public abstract class CardAssist {
    /**
     *
     * @param context 上下文
     * @param width   控件宽度
     * @return
     */
    abstract View cardImageView(Context context,int width);

    /**
     *
     * @param context  上下文
     * @param width    控件宽度
     * @return
     */
    abstract View cardInforView(Context context,int width);

    /**
     * 设置需要显示的数据
     * @param data  要显示的数据
     */
    public abstract void setData(Object data);

    /**
     * 设置需要显示的数据
     * @param data  要显示的数据
     */
    public abstract void onImgClick(Object data);
}
