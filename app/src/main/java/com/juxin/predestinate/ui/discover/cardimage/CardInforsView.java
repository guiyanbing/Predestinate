package com.juxin.predestinate.ui.discover.cardimage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体的心动模块控件
 * Created by zm on 2016/8/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CardInforsView extends FrameLayout {
    //相关静态变量
    /**
     * 表示控件正在向左滑动
     */
    public static final int VANISH_TYPE_LEFT = 0;
    /**
     * 表示控件正在向右滑动
     */
    public static final int VANISH_TYPE_RIGHT = 1;
    //控件相关
    private FrameLayout fl_layout;
    public LinearLayout line_uiv;
    private CardInforView uiv;
    private ModeViewPager mvp;
//    private CardImageView uImgV;//暂未使用
//    private ScrollView sv;
    private Context context;
    private LayoutParams flp;
//    private LinearLayout.LayoutParams lp;//暂时未使用
//    private LayoutParams lp2;
    //有关动画
    private ObjectAnimator oa;//由上到下第二个控件的属性动画
    private ObjectAnimator oa1;//由上到下第三个控件的属性动画
    private ObjectAnimator oa2;//由上到下第四个控件的属性动画
    private ObjectAnimator oa3;//恢复初始的动画
    private ObjectAnimator oa4;
    private ObjectAnimator oa5;
    private ObjectAnimator oa6;
    //监听
    private UserInforClickListener mUserInforClickListener;
    public UserInforsListener userInforsListener;
    //变量
    public ArrayList<CardInforView> uIV_list = new ArrayList<>();
    public List<Object> uif_list;
    private ArrayList<ImageView> mRoundImageViews = new ArrayList<>();
    private boolean isReady = true;//动画是否结束
    public static boolean click = true;//是否响应单击事件
    /**
     * 判断动画是否结束
     */
    public static boolean isok = true;
    public static float trueSpace;//用于计算属性动画Y方向上的偏移值
    public static float space;//卡片顶层的空白区
    public static float downAndUpdistance;//判断move事件是否执行的距离（大于此距离则认为move事件执行）
    public static float startTop;//卡片距离屏幕上面的距离
    public static float startLeft;//卡片距离屏幕左边的距离
    public static float tdesy;//记录最后一次y方向上的距离
    public static float tdesx;//记录最后一次x方向上的距离
    public float dstance;//属性动画的缩放值(第一级)
    public float dstance1;//属性动画的缩放值(第二级)
    public float dstance2;//属性动画的缩放值(第三级)
    public float transY;//属性动画Y方向上移动的值(第一级)
    public float transY1;//属性动画Y方向上移动的值(第二级)
    public float transY2;//属性动画Y方向上移动的值(第三级)
    private static int index = 2;//标记当前数据最后一条的下标
    public static int totalWidth;//手机屏幕的总宽度
    public static int totalHeight;//手机屏幕高度
    public int totalWidthAnim;//动画水平方向上的偏移值
    public int viewWidth;//控件的宽度
    public int tabLengh = 0;//下一次数据请求完成前的数据剩余条数

    public CardInforsView(Context context) {
        this(context,null);
    }

    public CardInforsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    /**
     * 设置卡片操作回调
     * @param userInforsListener UserInforsListener的回调接口
     */
    public void setUserInforsListener(UserInforsListener userInforsListener) {
        this.userInforsListener = userInforsListener;
    }

    public void initView(Context context) {
        space = context.getResources().getDisplayMetrics().density;
        downAndUpdistance = 10 * space;
        totalWidth = context.getResources().getDisplayMetrics().widthPixels;
        totalHeight = context.getResources().getDisplayMetrics().heightPixels;
        fl_layout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.f1_userinfors_layout, null);
        this.addView(fl_layout);
        //初始化view
        initView();
        //界面首次加载时初始化动画
        initAnimation();
    }

    /**
     * 初始化View
     */
    private void initView() {
        line_uiv = (LinearLayout) fl_layout.findViewById(R.id.line_uiv);
        uIV_list.add(((CardInforView) fl_layout.findViewById(R.id.view)).setCardAssist(new CardViewInfo()));
        uIV_list.add(((CardInforView) fl_layout.findViewById(R.id.uiv3)).setCardAssist(new CardViewInfo()));
        uIV_list.add(((CardInforView) fl_layout.findViewById(R.id.uiv2)).setCardAssist(new CardViewInfo()));
        uIV_list.add(((CardInforView) fl_layout.findViewById(R.id.uiv1)).setCardAssist(new CardViewInfo()));
//        sv = (ScrollView) fl_layout.findViewById(R.id.scrollView);//暂未开启
        flp = (LayoutParams) uIV_list.get(0).getLayoutParams();
        flp.leftMargin = (int) space * 20;
        flp.rightMargin = (int) space * 20;
        flp.topMargin = (int) space * 20;
        flp.width = (int) (totalWidth - space * 40);
        ViewTreeObserver mViewTreeObserver = CardInforsView.this.getViewTreeObserver();
        mViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {//监听获取距离左侧和上部的距离
            @Override
            public void onGlobalLayout() {
                int[] ints = new int[2];
                uIV_list.get(0).getLocationOnScreen(ints);
//                Log.e("DDDDDDDDD",uIV_list.get(0).getLeft()+";"+ints[1]+";"+uIV_list.get(0).getX()+";" +uIV_list.get(0).getY());
                if (uIV_list.get(0).getLeft() > 0 && uIV_list.get(0).getTop() > 0){
                    startLeft = uIV_list.get(0).getLeft();
                    startTop = ints[1];
                    if (Build.VERSION.SDK_INT < 16){
                        CardInforsView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }else{
                        CardInforsView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }

    /**
     * view移除时的处理逻辑
     */
    public void viewOut() {
        //初始化新控件
        fl_layout.removeView(uiv);
        fl_layout.addView(uiv, 0);
        uiv.getModeViewPager().setPropertyBack();
        //为新控件设置动画
        oa2.setTarget(uiv);
        oa2.setDuration(0);
        oa2.start();
        //将新控件添加到uIV_list集合中
        uIV_list.add(uIV_list.remove(0));
        //从集合中移除旧的控件
        uiv = uIV_list.get(0);
        mvp = uiv.getModeViewPager();
        mvp.setMa(this);
        //说明当前移除的数据
        if (userInforsListener != null && uif_list.size() > 0) {
            userInforsListener.dataListChanged(uif_list.size() - 1, uif_list.get(0));
        }
        //数据设置完成后此数据从集合中移除
        uif_list.remove(0);
        //为控件添加数据
        if (uif_list.size() >= 3) {
            uIV_list.get(2).setUserMeasess(uif_list.get(2));
            uIV_list.get(2).getCardAssist().setData(uif_list.get(2));
        } else if (uif_list.size() < 3) {
            uIV_list.get(3).setVisibility(View.INVISIBLE);
            uIV_list.get(uif_list.size()).setVisibility(View.INVISIBLE);
        }
        if (userInforsListener != null && uif_list.size() > 0) {
            userInforsListener.onSHow(uif_list.get(0));
        }
    }

    /**
     * 用于界面初始化时view的初始化动画
     */
    public void initAnimation() {
        PropertyValuesHolder a10 = PropertyValuesHolder.ofFloat("scaleX", 1f);
        PropertyValuesHolder a11 = PropertyValuesHolder.ofFloat("scaleY", 1f);
        PropertyValuesHolder a12 = PropertyValuesHolder.ofFloat("translationY", 0f);
        oa3 = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(0), a10, a11, a12);
        //与布局中的dp同步
        trueSpace = space * 40;
        totalWidthAnim = (int)(trueSpace*5 + totalWidth);
        //控件的总宽度（旧的计算方法）
        float width = (float) (totalWidth - trueSpace * 2);
        transY = trueSpace / 3f * 2;
        transY1 = trueSpace / 3f * 4;
        transY2 = trueSpace / 3f * 5;
        //设置缩放比例 不用变
        dstance = (float) (width - transY) / (float) width;
        dstance1 = (float) (width - transY * 2) / (float) width;
        dstance2 = (float) (width - transY * 3) / (float) width;

        PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat("scaleX", dstance);
        PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat("scaleY", dstance);
        PropertyValuesHolder a3 = PropertyValuesHolder.ofFloat("translationY", transY);
        oa = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(1), a1, a2, a3);

        PropertyValuesHolder a4 = PropertyValuesHolder.ofFloat("scaleX", dstance1);
        PropertyValuesHolder a5 = PropertyValuesHolder.ofFloat("scaleY", dstance1);
        PropertyValuesHolder a6 = PropertyValuesHolder.ofFloat("translationY", transY1);
        oa1 = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(2), a4, a5, a6);

        PropertyValuesHolder a7 = PropertyValuesHolder.ofFloat("scaleX", dstance1);
        PropertyValuesHolder a8 = PropertyValuesHolder.ofFloat("scaleY", dstance1);
        PropertyValuesHolder a9 = PropertyValuesHolder.ofFloat("translationY", transY1-2);

        PropertyValuesHolder a13 = PropertyValuesHolder.ofFloat("scaleX", dstance, 1f);
        PropertyValuesHolder a14 = PropertyValuesHolder.ofFloat("scaleY", dstance, 1f);
        PropertyValuesHolder a15 = PropertyValuesHolder.ofFloat("translationY", transY, 0f);
        oa5 = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(1), a13, a14, a15);
        PropertyValuesHolder a16 = PropertyValuesHolder.ofFloat("scaleX", dstance1, dstance);
        PropertyValuesHolder a17 = PropertyValuesHolder.ofFloat("scaleY", dstance1, dstance);
        PropertyValuesHolder a18 = PropertyValuesHolder.ofFloat("translationY", transY1, transY);
        oa6 = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(2), a16, a17, a18);
        oa2 = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(3), a7, a8, a9);
        oa.setDuration(0);
        oa1.setDuration(0);
        oa2.setDuration(0);
        oa.start();
        oa1.start();
        oa2.start();
    }

    //手动移除View时的动画
    public void fromButtonDelete() {
        oa.setTarget(uIV_list.get(2));
        oa.setDuration(0);
        oa.start();
        oa1.setTarget(uIV_list.get(3));
        oa1.setDuration(0);
        oa1.start();
    }

    //使用Button按钮移除View时的动画
    private void fromButtonDelete1() {
        oa5.setTarget(uIV_list.get(1));
        oa5.setDuration(200);
        oa6.setTarget(uIV_list.get(2));
        oa6.setDuration(200);
        oa5.start();
        oa6.start();
    }

    //在控件移动过程中删除View时的动画效果
    public void fromMoveBack() {
        oa.setTarget(uIV_list.get(1));
        oa.setDuration(150);
        oa.start();
        uIV_list.get(2).setScaleX(dstance1);
        uIV_list.get(2).setScaleY(dstance1);
        uIV_list.get(2).setTranslationY(transY1);
    }

    public void addView() {
        TextView textview = new TextView(getContext());
        textview.setText("这是新添加的子控件");
        textview.setBackgroundColor(Color.parseColor("#aaffffff"));
        textview.setHeight(1920);
        line_uiv.addView(textview);
    }

    /**
     * 从左边滑出
     */
    public void fromLeftOut() {
        if (isReady) {
            if (userInforsListener != null && uif_list.size() == 0) {
                userInforsListener.noData();
                return;
            }
            isReady = false;
            CardInforsView.isok = false;
            PropertyValuesHolder a13 = PropertyValuesHolder.ofFloat("rotation", -10f);
            PropertyValuesHolder a14 = PropertyValuesHolder.ofFloat("translationX", -totalWidthAnim);
            PropertyValuesHolder a15 = PropertyValuesHolder.ofFloat("translationY", totalWidth/4);
            oa4 = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(0), a13, a14, a15);
            oa4.setDuration(300);
            oa4.setInterpolator(new AnticipateInterpolator(1f));
            oa4.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if (userInforsListener != null) {
                        userInforsListener.onCardVanish(uif_list.get(0), CardInforsView.VANISH_TYPE_LEFT);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    viewOut();
                    CardInforsView.isok = true;
                    isReady = true;
                    if (userInforsListener != null) {
                        userInforsListener.onAnimFinish();
                    }
                    if (userInforsListener != null && uif_list.size() == 0) {
                        userInforsListener.noData();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            fromButtonDelete1();
            oa4.start();
        }
    }

    /**
     * 从右边滑出
     */
    public void fromRightOut() {
        if (isReady) {
            if (userInforsListener != null && uif_list.size() == 0) {
                userInforsListener.noData();
                return;
            }
            isReady = false;
            CardInforsView.isok = false;
            PropertyValuesHolder a13 = PropertyValuesHolder.ofFloat("rotation", 10f);
            PropertyValuesHolder a14 = PropertyValuesHolder.ofFloat("translationX", totalWidthAnim);
            PropertyValuesHolder a15 = PropertyValuesHolder.ofFloat("translationY", totalWidth/4);
            oa4 = ObjectAnimator.ofPropertyValuesHolder(uIV_list.get(0), a13, a14, a15);
            oa4.setDuration(300);
            oa4.setInterpolator(new AnticipateInterpolator(1f));
            oa4.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if (userInforsListener != null) {
                        userInforsListener.onCardVanish(uif_list.get(0), CardInforsView.VANISH_TYPE_RIGHT);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    viewOut();
                    CardInforsView.isok = true;
                    isReady = true;
                    if (userInforsListener != null) {
                        userInforsListener.onAnimFinish();
                    }
                    if (userInforsListener != null && uif_list.size() == 0) {
                        userInforsListener.noData();
                        return;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            fromButtonDelete1();
            oa4.start();
        }
    }

    /**
     * 从左边滑出
     */
    public void setOnUserInforClickListener(UserInforClickListener userInforClickListener) {
        this.mUserInforClickListener = userInforClickListener;
    }

    public void setIsoncl(boolean isoncl) {
        this.isoncl = isoncl;
    }

    private boolean isoncl = true;//防止连续点击启动多次个人信息activity

    public void click() {
        if (mUserInforClickListener != null && isoncl) {
//            HeartViewPanel.LogList(uif_list, " click ");
//            setIsoncl(false);
//            mUserInforClickListener.onUserInforClickListener(uif_list.get(0).getUid());
        }
    }

    /**
     * view事件的点击监听接口
     */
    public interface UserInforClickListener {
        /**
         * view事件的单击需要实现的方法
         */
        void onUserInforClickListener(long uid);
    }

    /**
     * 初始添加数据时调用此方法来添加数据
     * @param uif_list 数据集合
     */
    public void setData(List<Object> uif_list) {
        this.uif_list = uif_list;
        for (int i = 0 ;i < uIV_list.size();i++){
            uIV_list.get(i).getModeViewPager().setMa(this);
        }
//        HeartViewPanel.LogList(this.uif_list, " setData  ");
        uiv = uIV_list.get(0);
        mvp = uiv.getModeViewPager();
        for (int i = 0; i < uIV_list.size() && i < this.uif_list.size() && i < 3; i++) {//为控件设置心动数据
            uIV_list.get(i).setUserMeasess(this.uif_list.get(i));
            uIV_list.get(i).getCardAssist().setData(this.uif_list.get(i));
            uIV_list.get(i).setVisibility(View.VISIBLE);
        }
        if (this.uif_list.size() >= 4){
            uIV_list.get(3).setVisibility(View.VISIBLE);//数据大于等于4条设置最底层卡片显示
        }
    }

    /**
     * 当数据不足时调用此方法来添加数据
     * @param uif_list 数据集合
     */
    public void resetData(List<Object> uif_list, String fromTag) {
//        if (tabLengh == 0) {
//            setData(uif_list);
//        } else {
//            if (tabLengh < 3) {
//                for (int i = 0; i < uIV_list.size(); i++) {//控制要显示的卡片
//                    uIV_list.get(i).setVisibility(View.VISIBLE);
//                }
//                index = tabLengh;
//            }
//            this.uif_list = uif_list;
//            for (int i = index; i < this.uif_list.size() && i < 3; i++) {
//                uIV_list.get(i).setUserMeasess(this.uif_list.get(i));
//            }
//            HeartViewPanel.LogList(this.uif_list, " resetData  " + fromTag);
//        }
    }

    public ImageView getmRoundImageView(){
        if (mRoundImageViews.size()>0){
         return mRoundImageViews.remove(0);
        }
        return null;
    }
    public void setmRoundImageView(ImageView roundImageView){
        mRoundImageViews.add(roundImageView);
    }
    /**
     * UserInforsView的接口回调
     */
    public interface UserInforsListener {
        /**
         * 当前list数据的长度
         * @param currListSize
         * @param userInfor    最顶层显示的卡片的数据
         */
        void dataListChanged(int currListSize, Object userInfor);

        /**
         * 卡片飞向两侧回调
         * @param userInfor 飞向两侧的卡片数据
         * @param type      飞向哪一侧{@link #VANISH_TYPE_LEFT}或{@link #VANISH_TYPE_RIGHT}
         */
        void onCardVanish(Object userInfor, int type);

        /**
         * 没有数据了
         */
        void noData();

        /**
         * 动画结束时回调
         */
        void onAnimFinish();

        /**
         * 当前显示的是哪一个 更新 界面心动人数
         * @param tabHeart
         */
        void onSHow(Object tabHeart);
    }

    public void onAnimFinish() {
        if (userInforsListener != null) {
            userInforsListener.onAnimFinish();
        }
    }

    public void onCardVanish(int type) {
        if (userInforsListener != null && uif_list != null && uif_list.size() != 0) {
            userInforsListener.onCardVanish(uif_list.get(0), type);
        }
    }

    public void noData() {
        if (userInforsListener != null && uif_list.size() == 0) {
            userInforsListener.noData();
        }
    }

    /**
     * 设置控件在屏幕上的显示比例（0—1）
     * @param proportionWidth  控件的宽度比例
     * @param proportionHeight 空间的height比例
     */
    public void setProportionWidthAndHeight(float proportionWidth, float proportionHeight, float limitHeight) {
        flp.width = (int) (totalWidth * proportionWidth);
        viewWidth = flp.width;
//        if(limitHeight > flp.width+trueSpace/3f*5){
//            flp.height = flp.width*2;
//        }else{
//            flp.height = (int)(limitHeight - (trueSpace/3f*5))*2;
//            viewWidth = flp.height;
//        }
//        if (limitHeight > totalHeight*proportionHeight){
//            flp.height = (int)(totalHeight*proportionHeight);
//        }else {
//            flp.height = (int)limitHeight;
//        }
        flp.leftMargin = (int) ((totalWidth - flp.width) / 2);
        flp.rightMargin = flp.leftMargin;
        for (int i = 0; i < uIV_list.size(); i++) {
            uIV_list.get(i).setLayoutParams(flp);
            uIV_list.get(i).getUiv().setHeight();
        }
    }
}
