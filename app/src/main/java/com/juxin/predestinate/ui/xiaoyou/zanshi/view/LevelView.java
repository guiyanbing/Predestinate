package com.juxin.predestinate.ui.xiaoyou.zanshi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.util.UIUtil;


/**
 * 等级控件
 * Created by zm on 2016/8/23
 */
public class LevelView extends View {

    private final Context context;
    private Paint paint;
    private Bitmap mBitmap;
    private int[] levels = new int[]{R.drawable.p1_lv00b, R.drawable.p1_lv01b, R.drawable.p1_lv02b, R.drawable.p1_lv03b, R.drawable.p1_lv04b, R.drawable.p1_lv05b, R.drawable.p1_lv06b};

    public LevelView(Context context) {
        this(context, null);
    }

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(UIUtil.dp2px(2));
        paint.setColor(getResources().getColor(R.color.blue));
        paint.setStyle(Paint.Style.STROKE);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.p1_lv03b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLevel(canvas);
    }

    private void drawLevel(Canvas canvas) {
        RectF oval = new RectF(0, 0, UIUtil.dp2px(43.3f), UIUtil.dp2px(43.3f));
        canvas.drawCircle(UIUtil.dp2px(21.65f), UIUtil.dp2px(21.65f), UIUtil.dp2px(21.65f), paint);
        canvas.drawArc(oval, -180, 0, false, paint);
        Rect df = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF def = new RectF(UIUtil.dp2px(9.3f), UIUtil.dp2px(5.6f), UIUtil.dp2px(35f), UIUtil.dp2px(36.3f));
        canvas.drawBitmap(mBitmap, df, def, null);
    }
}