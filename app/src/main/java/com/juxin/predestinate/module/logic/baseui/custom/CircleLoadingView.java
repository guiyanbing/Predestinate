package com.juxin.predestinate.module.logic.baseui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形下载进度条
 * Created by zrp on 16-7-11.
 */
public class CircleLoadingView extends View {

    /* view绘制的颜色 */
    private int viewColor = 0x66ffffff;

    /* max progress */
    private int max = -1;

    /* current progress */
    private int progress = -1;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getVisibility() == VISIBLE) {
            Paint paint = new Paint();
            paint.setAntiAlias(true); //設置畫筆為無鋸齒
            paint.setColor(viewColor);

            //画圆环
            paint.setStyle(Paint.Style.STROKE);//绘制空心圆
            paint.setStrokeWidth(getWidth() / 16);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - getWidth() / 16, paint);

            //画扇形loading区域
            float angle = 0;
            if (max > 0 && progress >= 0) {
                angle = (float) progress / (float) max * 360f;
            }
            paint.setStyle(Paint.Style.FILL);
            RectF sector = new RectF(getWidth() / 7, getHeight() / 7,
                    getWidth() - getWidth() / 7, getHeight() - getHeight() / 7);//绘制扇形的区域
            canvas.drawArc(sector, 270, angle, true, paint);
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            invalidate();
        }
    }

    /**
     * 设置绘制view的颜色
     *
     * @param viewColor 绘制view的颜色
     */
    public void setViewColor(@ColorInt int viewColor) {
        this.viewColor = viewColor;
    }

    /**
     * set max progress value
     *
     * @param max max value of download
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * set progress value
     *
     * @param progress progress of downloading
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
