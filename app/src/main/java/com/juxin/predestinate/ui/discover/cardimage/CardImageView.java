package com.juxin.predestinate.ui.discover.cardimage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;

/**
 * 心动模块中的图片控件
 * Created by zm on 2016/8/2.
 */
public class CardImageView extends LinearLayout {
    private ModeViewPager vp;
    /**
     * delete图标
     */
    private ImageView iv_del;
    /**
     * love图标
     */
    private ImageView iv_love;
    private FrameLayout fl;

    private CardAssist mCardAssist;
    private Context mContext;

    public CardImageView(Context context) {
        this(context,null);
    }

    public CardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.f1_userimage_layout, this, true);
        fl = (FrameLayout) findViewById(R.id.userimage_fl);
        vp = (ModeViewPager)findViewById(R.id.mvp);
        iv_del = (ImageView)findViewById(R.id.iv_del);
        iv_love = (ImageView)findViewById(R.id.iv_love);
    }

    public void setCardAssist(CardAssist mCardAssist){
        fl.addView(mCardAssist.cardImageView(mContext, CardInforsView.totalWidth));
    }

    public void setHeight(){
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)vp.getLayoutParams();
        lp.height = vp.ma1.viewWidth;
        vp.setLayoutParams(lp);
        LinearLayout.LayoutParams llp = (LayoutParams) fl.getLayoutParams();
        llp.height = vp.ma1.viewWidth;
        fl.setLayoutParams(llp);
    }

    //获取控件中ModeViewPager对象（同时他也是ViewPager子类的一个对象）
    public ModeViewPager getModeViewPager(){
        return  vp;
    }

    //获取控件中的delete控件，他是一个ImageView
    public ImageView getIv_del(){
        return iv_del;
    }

    //获取控件中的love控件，他是一个ImageView
    public ImageView getIv_love(){
        return iv_love;
    }
}
