package com.juxin.predestinate.ui.discover.cardimage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;


/**
 * 自定义的ViewPager对象
 * Created by zm on 2016/8/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModeViewPager extends ViewPager {
    //静态常量
    public static final int UNABLE_MOVE = 0;//viewpager不能滑动
    public static final int ENABLE_MOVE = 1;//viewpager可以滑动
    //控件相关
    public CardInforsView ma1;//心动控件对象
    /**
     * 自定义的用户信息控件
     */
    private CardInforView uinv;
    /**
     * 动画（用于缩放和平移）
     */
    private ObjectAnimator oa;
    //相关变量
    private VelocityTracker mVelocityTracker;//检测Move的滑动速度
    /**
     * 判断控件是否是第一次被点击
     */
    private boolean isOne = true;
    /**
     * 判断动画是否完成（卡片划出或卡片回到原位）
     */
    private static boolean isfinash = true;
    /**
     * 动画响应时不进行事件的响应，为false时说明动画正在进行中和事件正在处理
     */
    private boolean isMove = false;
    /**
     * 控件自身的高度
     */
    public float height;
    /**
     * 属性动画中y方向上的移动距离
     */
    private float translateY;   //可能需要修改
    private float translateY1;
//    private float translateY2;
    /**
     * 为UNABLE_MOVE = 0不响应viewPager的滑动事件为ENABLE_MOVE = 1则体现为viewPager的滑动效果：图片间切换
     */
    public int mode = 0;
    /**
     * 保存控件的旋转角度
     */
    private int totalDegree = 0;
    /**
     * 用于设置控件的透明度
     */
    private int setAlpha = 0;
    /**
     * 相当于屏幕总宽度
     */
    private int totalLength;
    /**
     * 旋转时的目标角度
     */
    private int goalDegree;
    public float scale;//第一层的缩放
    public float scaley;//用于缩放值计算
    public float scale1;//第二层的缩放
//    public float scale2;//第二层的缩放
    private float pointx;//记录手指的位置x
    private float pointy;//记录手指的位置y
    private float tspointx;//记录手指按下时的初始坐标x
    private float tspointy;//记录手指按下时的初始坐标y
    private long lastTime;//手指按下时的时间

    public ModeViewPager(Context context) {
        super(context);
    }

    public ModeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUInV(CardInforView uinv) {
        this.uinv = uinv;
    }

    /**
     * 设置控件的mode值为UNABLE_MOVE = 0不响应viewPager的滑动事件为ENABLE_MOVE = 1则体现为viewPager的滑动效果：图片间切换
     */
    public void setMode(int boo) {
        this.mode = boo;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMa(CardInforsView ma) {
        this.ma1 = ma;
        //        Log.d("_test", "ma == " + ma);
    }
    public CardInforsView getMa(){
        return this.ma1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("_test", "ma1 == " + ma1.uIV_list);
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();//获取VelocityTracker实例
        }
        mVelocityTracker.addMovement(ev);//将当前的触摸事件传递给VelocityTracker对象
        if (CardInforsView.isok && this == ma1.uIV_list.get(0).getModeViewPager()) {//判断滑动的是否是最上面的卡片
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTime = System.currentTimeMillis();
                    pointx = ev.getRawX();//手指按下时X坐标
                    pointy = ev.getRawY();//手指按下时Y坐标
                    tspointx = ev.getRawX();
                    tspointy = ev.getRawY();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    int velocityXLimit = 1000;//Move事件x方向的速度临界值
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000);
                    int velocityX = (int) velocityTracker.getXVelocity();
                    int velocityY = (int) velocityTracker.getYVelocity();
                    float pointx1 = ev.getRawX();//手指抬起时X坐标
                    float pointy1 = ev.getRawY();//手指抬起时Y坐标
                    int[] ints = new int[2];//用于存储手指抬起的位置
                    uinv.getLocationOnScreen(ints);
                    int totaldsx = (int) (ints[0] - CardInforsView.startLeft);//X轴方向的滑动距离
                    //判断是否为单击事件
                    if (Math.abs(pointx1 - pointx) < 8 && Math.abs(pointy1 - pointy) < 8 && isfinash) {//像素小于8认为是单击事件
                        //                        if (ModeViewPager.this.getMode() == ModeViewPager.ENABLE_MOVE) {//根据ModeViewPager的现有模式来切换模式
                        //                            ModeViewPager.this.setMode(ModeViewPager.UNABLE_MOVE);//将ModeViewPager的模式设置为不可滑动状态
                        ////                            UserInforsView.isok = false;
                        ////                            ma1.upRemoveView();
                        //                        } else {
                        //                            //2016.8.11 将注释取消后Viewpager可滑动
                        ////                            ModeViewPager.this.setMode(ModeViewPager.ENABLE_MOVE);
                        ////                            UserInforsView.isok = false;
                        //                            //                            ma1.upAddView();
                        //                        }
                        ma1.click();
                    }else if (Math.abs(velocityX) > velocityXLimit || (velocityX == 0 && velocityY == 0 && (System.currentTimeMillis() - lastTime) < 200)){
                        //快速滑动时走次分支（依据时间及水平位移判断是否将最上面的控件移除）按下与抬起小于100ms则执行一下逻辑
                        if (isfinash){//动画是否完成
                            float y = pointy1 - pointy;//y轴上的偏移量
                            if (y > ma1.viewWidth ){//限制Y方向上的偏移
                                y = ma1.viewWidth;
                            }
                            if (pointy == 0 || pointy1 == 0){//限制判断
                                y = CardInforsView.tdesy;
                            }else{
                                CardInforsView.tdesy = y;
                            }
                            if (pointx == 0 || pointx1 == 0){//限制判断

                            }else{
                                CardInforsView.tdesx = pointx1 - pointx;
                            }
                            if (totaldsx < 0 || (totaldsx == 0 && velocityX < 0 ) || CardInforsView.tdesx < 0){//向左划出
                                fastOut(-ma1.totalWidthAnim, y, CardInforsView.VANISH_TYPE_LEFT);
                            } else if (totaldsx > 0 || (totaldsx == 0 && velocityX > 0 ) || CardInforsView.tdesx > 0){//向右滑出
                                fastOut(ma1.totalWidthAnim,y,CardInforsView.VANISH_TYPE_RIGHT);
                            }
                            break;
                        }
                    } else if (isMove) {//判断手指抬起事件是否是在MOVE事件后抬起的，并且不是快速滑动
                        if (Math.abs(totaldsx)<((float)totalLength/5*2)){//卡片返回原来位置
                            //控件返回使用属性动画中的旋转动画和平移动画
                            upPropertyBack();
                        } else {
                            //处理控件滑出去的效果分为两种左滑和右滑
                            if (totaldsx < 0) {
                                moveOut(-ma1.totalWidthAnim, CardInforsView.VANISH_TYPE_LEFT);
                            }else if (totaldsx>0){
                                moveOut(ma1.totalWidthAnim,CardInforsView.VANISH_TYPE_RIGHT);
                            }
                        }
                        isMove = false;
                    }
                    if(mVelocityTracker != null){
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (ma1.isok) {
                        if (ModeViewPager.this.mode == UNABLE_MOVE) {
                            //控件第一次被点击时有处理的逻辑获取Left，Top，Right及width和height的值
                            if (isOne) {
                                float width = getWidth();
                                totalLength = (int) (width / 2 + CardInforsView.startLeft*2);
                                isOne = false;
                            }
                            //获取控件相对与屏幕的坐标
                            if (tspointx != 0 && tspointy != 0) {
                                float ttpointx = ev.getRawX();//手指抬起时的x坐标
                                float ttpointy = ev.getRawY();//手指抬起时的y坐标
                                float dsx = ttpointx - tspointx;
                                float dsy = ttpointy - tspointy;
                                //处理UserInforView的拖动事件
                                if ((Math.abs(dsx) > 6 || Math.abs(dsy) > 6)) {
                                    uinv.getUiv().getIv_del().setVisibility(View.VISIBLE);
                                    uinv.getUiv().getIv_love().setVisibility(View.VISIBLE);
                                    isMove = true;
                                    tspointx = ttpointx;
                                    tspointy = ttpointy;
                                    uinv.setX(uinv.getX() + dsx);
                                    uinv.setY(uinv.getY() + dsy);
                                    changeAlpha();//改变控件的透明度（喜欢与不喜欢）
                                    changeRotate();//改变控件的旋转角度
                                    changeScale();//改变控件的缩放比例
                                    uinv.iv.invalidate();
                                }
                            }
                        }
                    }
                    break;
            }
            if (ModeViewPager.this.mode == UNABLE_MOVE && ev.getAction() == MotionEvent.ACTION_MOVE) {
                return true;
            } else if (ModeViewPager.this.mode == ENABLE_MOVE && ev.getAction() == MotionEvent.ACTION_MOVE) {
                //响应ViewPager自身的滑动事件
                return super.onTouchEvent(ev);
            }
        }
        if (ModeViewPager.this.mode == ENABLE_MOVE) {//让ModeViewPager相应自己的滑动事件
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    //根据控件在屏幕上的移动位置设置控件的透明渐变效果
    public void changeAlpha() {
//        setAlpha = Math.abs((int) (((double) (CardInforsView.startLeft - uinv.getX()) / (double) totalLength) * 255));
//        if (setAlpha < 0) {
//            setAlpha = 0;
//        } else if (setAlpha > 255) {
//            setAlpha = 255;
//        }
//        if (CardInforsView.startLeft - uinv.getX() > 0){
//            uinv.getUiv().getIv_del().getBackground().setAlpha(setAlpha);
//            uinv.getUiv().getIv_love().getBackground().setAlpha(0);
//        }else if (CardInforsView.startLeft - uinv.getX() < 0){
//            uinv.getUiv().getIv_love().getBackground().setAlpha(setAlpha);
//            uinv.getUiv().getIv_del().getBackground().setAlpha(0);
//        }
    }

    /**
     * 手指抬起后卡片回归原位
     */
    private void upPropertyBack(){
        PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat("rotation", totalDegree, 0f);
        PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat("translationX", 0);
        PropertyValuesHolder a3 = PropertyValuesHolder.ofFloat("translationY", 0);
        ObjectAnimator oa=ObjectAnimator.ofPropertyValuesHolder(uinv, a1, a2,a3);
        oa.setInterpolator(new OvershootInterpolator());
        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                changeAlpha();
            }
        });
        oa.setDuration(300).addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                CardInforsView.isok = false;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                uinv.getUiv().getIv_del().getBackground().setAlpha(0);
//                uinv.getUiv().getIv_love().getBackground().setAlpha(0);
                tspointx = 0;
                tspointy = 0;
                CardInforsView.isok = true;
                PropertyValuesHolder a5 = PropertyValuesHolder.ofFloat("rotation",0f);
                ObjectAnimator oa1=ObjectAnimator.ofPropertyValuesHolder(uinv, a5);
                oa1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        uinv.getUiv().getIv_del().getBackground().setAlpha(0);
//                        uinv.getUiv().getIv_love().getBackground().setAlpha(0);
                        uinv.getUiv().getIv_del().setVisibility(View.INVISIBLE);
                        uinv.getUiv().getIv_love().setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                oa1.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ma1.fromMoveBack();
        oa.start();
    }
    /**
     * view快速移除时的动画
     * @param transX x轴的偏移
     * @param y 轴的偏移
     * @param type 标记向左还是向右
     */
    private void fastOut(int transX , float y,final int type){
        PropertyValuesHolder a14 = PropertyValuesHolder.ofFloat("translationX",transX);//设置X方向的偏移距离
        PropertyValuesHolder a15 = PropertyValuesHolder.ofFloat("translationY",y);//设置Y方向的偏移距离
        ObjectAnimator oa4=ObjectAnimator.ofPropertyValuesHolder(uinv,a14,a15);//设置动画
        oa4.setDuration(90);
        oa4.setInterpolator(new LinearInterpolator());//设置动画效果
        oa4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isfinash = false;
                CardInforsView.isok = false;
                ma1.onCardVanish(type);//设置卡片飞出的方向
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                changeBack();
                ma1.viewOut();
                CardInforsView.isok = true;
                ma1.onAnimFinish();
                ma1.noData();
                isfinash = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animation1();
        oa4.start();
    }
    /**
     * view移除时的动画
     * @param transX x轴的偏移
     * @param type 标记向左还是向右
     */
    private void moveOut(int transX , final int type){
        PropertyValuesHolder a14 = PropertyValuesHolder.ofFloat("translationX",transX);
        PropertyValuesHolder a15 = PropertyValuesHolder.ofFloat("translationY",(uinv.getY()-CardInforsView.startTop)*3/2);
        ObjectAnimator oa4=ObjectAnimator.ofPropertyValuesHolder(uinv,a14,a15);
        oa4.setDuration(240);
        //设置动画效果
        oa4.setInterpolator(new LinearInterpolator());
        oa4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                CardInforsView.isok = false;
                ma1.onCardVanish(type);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                changeBack();
                ma1.viewOut();
                CardInforsView.isok = true;
                ma1.onAnimFinish();
                ma1.noData();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animation2();
        oa4.start();
    }

    //此方法用于根据控件的滑动位置设置控件的旋转角度
    public void changeRotate() {
        goalDegree = (int) (((double) (uinv.getX() - CardInforsView.startLeft) / (double) totalLength) * 10);
        if (goalDegree >= 20) {
            goalDegree = 20;
        } else if (goalDegree <= -20) {
            goalDegree = -20;
        }
        uinv.setRotation(goalDegree);
        totalDegree = goalDegree;
    }

    //此方法用于根据控件的滑动位置设置控件的旋转角度
    public void changeScale(){
        float tepScale = (float) Math.abs(((double)(CardInforsView.startLeft - uinv.getX())/(double) totalLength));//根据水平距离计算x方向上的缩放
        scaley = (float) Math.abs(((double)(CardInforsView.startTop - uinv.getY())/(double) totalLength));//根据竖直距离计算y方向上的缩放
        if (tepScale < scaley) {
            tepScale = scaley;
        }
        if (tepScale > 1.2f){
            tepScale = 1.2f;
        }
        if (tepScale >= 1) {
            translateY = 0;
            translateY1 = ma1.transY;
//            translateY2 = ma1.transY1;
        } else {
            translateY = ma1.transY - ma1.transY * tepScale;
            translateY1 = ma1.transY * 2 - ma1.transY * tepScale;
//            translateY2 = ma1.transY * 3 - ma1.transY * tepScale;
        }
        scale = ma1.dstance + (1-ma1.dstance) * tepScale;//第二个CardInforView的缩放（自上而下）
        scale1 = ma1.dstance1 + (1-ma1.dstance) * tepScale;//第三个CardInforView的缩放（自上而下）
//        scale2 = ma1.dstance2 + (1-ma1.dstance) * tepScale;//第三个CardInforView的缩放（自上而下）
        ma1.uIV_list.get(1).setScaleX(scale);
        ma1.uIV_list.get(1).setScaleY(scale);
        ma1.uIV_list.get(1).setTranslationY(translateY);
        ma1.uIV_list.get(2).setScaleX(scale1);
        ma1.uIV_list.get(2).setScaleY(scale1);
        ma1.uIV_list.get(2).setTranslationY(translateY1);
//        ma1.uIV_list.get(3).setScaleX(scale2);
//        ma1.uIV_list.get(3).setScaleY(scale2);
//        ma1.uIV_list.get(3).setTranslationY(translateY2);
    }

    /**
     * 动画让滑出去View的属性回复的初始值
     */
    public void changeBack() {
//        uinv.setTranslationX(0);
//        uinv.setTranslationY(0);
//        uinv.setRotation(0);


        PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat("rotation", totalDegree, 0f);
        PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat("translationX", 0);
        PropertyValuesHolder a3 = PropertyValuesHolder.ofFloat("translationY", 0);
        final ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(uinv, a1, a2, a3);
        oa.setInterpolator(new OvershootInterpolator());
        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                changeAlpha();
            }
        });
        oa.setDuration(0).addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                uinv.getUiv().getIv_del().getBackground().setAlpha(0);
//                uinv.getUiv().getIv_love().getBackground().setAlpha(0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        oa.start();
    }

    public void animation1(){//第二个CardInforView的还原（自上而下），当在上层的CardInforView划出时
        PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat("scaleX",1f,1f);
        PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat("scaleY",1f,1f);
        PropertyValuesHolder a3 = PropertyValuesHolder.ofFloat("translationY", -10, 0f);
        oa=ObjectAnimator.ofPropertyValuesHolder(ma1.uIV_list.get(1), a1, a2,a3);
        oa.setDuration(50);
        ma1.fromButtonDelete();
        oa.start();
    }

    public void animation2(){//第二个CardInforView的还原（自上而下），当在上层的CardInforView划出时
        PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat("scaleX",1f,1f);
        PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat("scaleY",1f,1f);
        PropertyValuesHolder a3 = PropertyValuesHolder.ofFloat("translationY",-10,0f);
        oa=ObjectAnimator.ofPropertyValuesHolder(ma1.uIV_list.get(1), a1, a2,a3);
        oa.setDuration(180);
        ma1.fromButtonDelete();
        oa.start();
    }

    @Override
    public void scrollTo(int x, int y) {

    }
    /**
     * 手指抬起后卡片回归原位（卡片属性恢复到初始状态）
     */
    public void setPropertyBack(){
        uinv.getUiv().getIv_del().setVisibility(View.INVISIBLE);
        uinv.getUiv().getIv_love().setVisibility(View.INVISIBLE);
//        uinv.getUiv().getIv_del().getBackground().setAlpha(0);
//        uinv.getUiv().getIv_love().getBackground().setAlpha(0);
        uinv.setRotation(0);
        uinv.setTranslationY(0);
        uinv.setTranslationX(0);
    }
}
