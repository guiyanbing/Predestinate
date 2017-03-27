package com.juxin.predestinate.ui.start;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.R;

/**
 * 注册页头部背景
 * Created YAO on 2017/3/27.
 */

public class RegHeadBg extends View {
    private float height;
    private float offset=400;

    public RegHeadBg(Context context) {
        super(context);
    }

    public RegHeadBg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RegHeadBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);
        Shader mShader = new LinearGradient(0, 0, getWidth(), getHeight(),
                new int[]{getResources().getColor(R.color.bg_reghead_start), getResources().getColor(R.color.bg_reghead_end)}, null, Shader.TileMode.CLAMP); // 一个材质,打造出一个线性梯度沿著一条线。
        p.setShader(mShader);
        RectF oval2 = new RectF(-(getWidth() / 2+offset), -getHeight(), getWidth() + getWidth() / 2+offset, height);
        canvas.drawArc(oval2, 0, 180, false, p);
    }
}
