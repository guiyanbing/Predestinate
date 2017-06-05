package com.juxin.predestinate.ui.discover.cardimage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.util.ShadowProperty;
import com.juxin.predestinate.module.util.ShadowViewHelper;
import com.juxin.predestinate.module.util.UIUtil;

/**
 * 心动模块中的卡片控件
 * Created by zm on 2016/8/2.
 */
public class CardInforView extends LinearLayout implements View.OnTouchListener {
    //控件相关
    private Context context;
    private LinearLayout rela_car;
    public ImageView iv;
    private CardImageView uiv;

    private CardAssist mCardAssist;
//    private Bitmap dot_select,dot_unselect;//暂未使用
    //相关变量
//    private List<ImageView> iv_list = new ArrayList<>();//用于存储头像（以后可能还用于存储相册）
    private int default_head;
//    public TextView textView;//暂未使用
//    private int imagesCount;////暂未使用

    public CardInforView(Context context) {
        this(context,null);
    }

    public CardInforView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.f1_userinfor_layout, this, true);
        rela_car = (LinearLayout) findViewById(R.id.rela_car);
        uiv = (CardImageView) findViewById(R.id.view2);
        uiv.getModeViewPager().setUInV(CardInforView.this);
        rela_car.setOnTouchListener(this);
//        uiv.getIv_love().getBackground().setAlpha(0);
//        uiv.getIv_del().getBackground().setAlpha(0);

//        default_head = ModuleMgr.getCenterMgr().isMan() ? R.drawable.y2_hd_woman : R.drawable.y2_hd_man;

        float r = UIUtil.dp2px(4);
        ShadowViewHelper.bindShadowHelper(//创建阴影
                new ShadowProperty()
                        .setShadowColor(0xcc000000)
                        .setShadowRadius(UIUtil.dp2px(2))
                , rela_car, r, r);
    }

    public CardInforView setCardAssist(CardAssist mCardAssist){
        this.mCardAssist = mCardAssist;
        uiv.setCardAssist(mCardAssist);
        rela_car.addView(mCardAssist.cardInforView(context, CardInforsView.totalWidth));
        return this;
    }

    public CardAssist getCardAssist(){
        return mCardAssist;
    }

    //获取UserInforView上的ModeViewPager控件
    public ModeViewPager getModeViewPager() {
        return uiv.getModeViewPager();
    }
//暂未使用
//    //用于设置UserInforView上的UserImageView控件（用于UserInforView点击事件中）
//    public void setUiv(CardImageView uiv) {
//        this.uiv = uiv;
//        this.addView(uiv, 0);
//    }

    //获取UserInforView上的UserImageView控件
    public CardImageView getUiv() {
        return this.uiv;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 设置卡片的数据
     *
     * @param uif 数据
     */
    public void setUserMeasess(final Object uif) {
//        tv_name.setText(uif.getNickname() + " ");
//        tv_age.setText(ModuleMgr.getCenterMgr().getHandledAge(uif.getAge()) + "岁");
//        String city = AreaConfig.getInstance().getCity(uif.getProvince(), uif.getCity()).getCity();
//        if (!TextUtils.isEmpty(city)) {
//            tv_message.setText(city);
//        } else {
//            tv_message.setText("北京市");
//        }
        getModeViewPager().setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            //每次滑动的时候生成的组件
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                iv = getModeViewPager().getMa().getmRoundImageView();
                if (iv == null){
                    iv = new ImageView(context);
                }
//                iv.setBorderRadius(3);
//                iv.setType(1);
                iv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                // TODO: 2017/6/1 请求图片
//                iv.setImageResource(ModuleMgr.getCenterMgr().isMan() ? R.drawable.y2_hd_man : R.drawable.y2_hd_woman);
//                ModuleMgr.httpMgr.reqKingUserHeadImage(iv, uif.getAvatar(), default_head);
                ImageLoader.loadRound(context, "http://dynamic-image.yesky.com/1080x-/uploadImages/2015/099/21/8L96H6B6G764.jpg",iv
                , 20, com.juxin.library.R.drawable.default_pic, com.juxin.library.R.drawable.default_pic);
                container.addView(iv);
                return iv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                for (int i = 0;i<container.getChildCount();i++){
                    getModeViewPager().getMa().setmRoundImageView((ImageView) container.getChildAt(i));
                }
                container.removeAllViews();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return uiv.getModeViewPager().onTouchEvent(motionEvent);
    }
}
