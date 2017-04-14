package com.juxin.predestinate.ui.xiaoyou.zanshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by zm on 2017/4/11.
 */
public class SwipeLayout  extends RelativeLayout {
    public View mCenterView;
    public View mRightView;
    public int mRightWidth;
    public int mCenterWidth;

    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
       super.onLayout(changed,l,t,r,b);
       if(mRightView != null){
           mRightWidth = mRightView.getMeasuredWidth();
           int height = mRightView.getHeight();
           mRightView.layout(r - mRightWidth,0,r,height);
       }
        if(mCenterView != null){
            mCenterWidth = mCenterView.getMeasuredWidth();
            int height = mCenterView.getMeasuredHeight();
            mCenterView.layout(l,0,r,height);
        }
    }

    public boolean isOpen(){
        if(mCenterView.getScrollX() > 0){
            return true;
        } else {
            return  false;
        }
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRightView = getChildAt(0);
        mCenterView = getChildAt(1);
    }
}
