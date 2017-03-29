package com.juxin.predestinate.module.logic.baseui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.custom.RightSlidLinearLayout;

/**
 * 应用中所有activity的基类，便于进行数据的统计等
 * Created by ZRP on 2016/9/18.
 */
public class BaseActivity extends FragmentActivity {

    private boolean canNotify = true; //是否能弹出悬浮提示
    private boolean canBack = true;   //是否能右滑退出
    private RightSlidLinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (canBack) {
            initSlideBack();
        }
    }

    /**
     * 顶层触摸控制
     */
    private void initSlideBack() {
        linearLayout = (RightSlidLinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_slideback_container, null);
        linearLayout.attachToActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getWindow().setBackgroundDrawableResource(R.color.transparent);  // 去除Theme设置的默认背景，减少绘图层级
    }

    /**
     * 设置当前页面是否支持滑动退出，需要写在继承该类的子类onCreate中super.onCreate();的前面
     *
     * @param canBack 能否支持右滑退出
     */
    public void isCanBack(boolean canBack) {
        this.canBack = canBack;
    }

    /**
     * 设置当前页面是否支持悬浮窗消息通知，默认为可以弹出
     *
     * @param canNotify 能否支持悬浮窗消息通知
     */
    public void setCanNotify(boolean canNotify) {
        this.canNotify = canNotify;
    }

    /**
     * @return 获取当前activity实例能否弹出悬浮窗消息状态
     */
    public boolean isCanNotify() {
        return canNotify;
    }

    /**
     * 返回
     */
    public void back() {
        //关闭键盘并finish当前页面
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        //关闭键盘
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置返回组件
     *
     * @param view
     */
    public void setBackView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    /**
     * 设置返回按钮
     *
     * @param resourcesId 返回按钮id
     */
    public void setBackView(int resourcesId) {
        View view = this.findViewById(resourcesId);
        view.setVisibility(View.VISIBLE);
        setBackView(view);
    }

    /**
     * 设置返回组件
     */
    public void setBackView() {
        setBackView(R.id.base_title_back);
    }

    /**
     * 设置返回按钮，同时设置标题
     *
     * @param title 标题文字
     */
    public void setBackView(String title) {
        setBackView();
        setTitle(title);
    }

    /**
     * 设置返回按钮，同时设置标题
     *
     * @param resourcesId 返回按钮id
     * @param showLeftTip 是否显示返回按钮文字
     * @param title       标题文字
     * @param titleColor  标题颜色
     */
    public void setBackView(int resourcesId, boolean showLeftTip, String title, int titleColor) {
        View view = this.findViewById(resourcesId);
        view.setVisibility(View.VISIBLE);
        view.findViewById(R.id.base_title_view_tip).setVisibility(showLeftTip ? View.VISIBLE : View.INVISIBLE);
        setBackView(view);

        setTitle(title, titleColor);
    }

    /**
     * 设置返回按钮
     *
     * @param resourcesId 资源ID
     * @param title       标题
     * @param listener    资源点击事件
     */
    public void setBackView(int resourcesId, String title, View.OnClickListener listener) {
        View view = this.findViewById(resourcesId);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(listener);

        setTitle(title);
    }

    /**
     * 设置标题
     *
     * @param txt
     */
    public void setTitle(String txt) {
        TextView textView = (TextView) this.findViewById(R.id.base_title_title);
        textView.setVisibility(View.VISIBLE);
        textView.setText(txt);
    }

    /**
     * 设置标题
     *
     * @param txt
     */
    public void setTitle(String txt, int color) {
        TextView textView = (TextView) this.findViewById(R.id.base_title_title);
        textView.setVisibility(View.VISIBLE);
        textView.setText(txt);
        textView.setTextColor(color);
    }

    /**
     * 设置标题背景颜色
     *
     * @param color 标题颜色,下面的线默认是不显示的
     */
    public void setTitleBackground(int color) {
        setTitleBackground(color, false);
    }

    /**
     * 设置标题背景颜色
     *
     * @param color      背景颜色
     * @param isShowLine 下面的线是否显示
     */
    public void setTitleBackground(int color, boolean isShowLine) {
        View titleBg = this.findViewById(R.id.base_title_tab);
        titleBg.setBackgroundResource(color);
        this.findViewById(R.id.cut_line).setVisibility(isShowLine ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题 带角标的标题
     *
     * @param txt    标题内容
     * @param number 角标显示数量
     */
    public void setTitleWithBadge(String txt, String number) {
        TextView textView = (TextView) this.findViewById(R.id.title);
        TextView badge = (TextView) this.findViewById(R.id.base_title_badge_view);
        textView.setVisibility(View.VISIBLE);
        badge.setVisibility(View.VISIBLE);
        textView.setText(txt);
        badge.setText(number);
    }

    /**
     * 添加title中标题中间的填充view
     *
     * @param container 填充的view
     */
    public void setTitleCenterContainer(View container) {
        LinearLayout title_center_container = (LinearLayout) this.findViewById(R.id.base_title_center_container);
        title_center_container.removeAllViews();
        title_center_container.addView(container);
    }

    /**
     * 设置标题右侧文字
     *
     * @param txt      标题右侧显示文字
     * @param listener 文字点击事件回调
     */
    public void setTitleRight(String txt, View.OnClickListener listener) {
        setTitleRight(txt, -1, listener);
    }

    /**
     * 设置标题右侧文字
     *
     * @param txt      标题右侧显示文字
     * @param color    文字颜色资源id
     * @param listener 文字点击事件回调
     */
    public void setTitleRight(String txt, int color, View.OnClickListener listener) {
        this.findViewById(R.id.base_title_right_img_container).setVisibility(View.GONE);
        TextView textView = (TextView) this.findViewById(R.id.base_title_right_txt);
        textView.setVisibility(View.VISIBLE);
        if (color != -1) textView.setTextColor(getResources().getColor(color));
        textView.setText(txt);
        textView.setOnClickListener(listener);
        titleRightText = textView;
    }

    private TextView titleLeftText, titleRightText;
    private ImageView titleRightImg;

    /**
     * @return 获取标题栏view对象
     */
    public View getTitleView() {
        return findViewById(R.id.base_title);
    }

    /**
     * 设置标题右侧图片
     */
    public void setTitleRightImg(int resId, View.OnClickListener listener) {
        this.findViewById(R.id.base_title_right_txt).setVisibility(View.GONE);
        View view = this.findViewById(R.id.base_title_right_img_container);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(listener);
        ImageView imageView = (ImageView) view.findViewById(R.id.base_title_right_img);
        imageView.setImageResource(resId);
        titleRightImg = imageView;
    }

    /**
     * 设置标题左侧文字
     *
     * @param txt      标题左侧显示文字
     * @param listener 文字点击事件回调
     */
    public void setTitleLeft(String txt, View.OnClickListener listener) {
        TextView textView = (TextView) this.findViewById(R.id.base_title_left_txt);
        textView.setVisibility(View.VISIBLE);
        textView.setText(txt);
        textView.setOnClickListener(listener);
        titleLeftText = textView;
    }

    /**
     * 设置标题左侧文字
     *
     * @param txt      标题左侧显示文字
     * @param color    文字颜色资源id
     * @param listener 文字点击事件回调
     */
    public void setTitleLeft(String txt, int color, View.OnClickListener listener) {
        TextView textView = (TextView) this.findViewById(R.id.base_title_left_txt);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(getResources().getColor(color));
        textView.setText(txt);
        textView.setOnClickListener(listener);
        titleLeftText = textView;
    }

    /**
     * 设置标题左侧图片
     */
    public void setTitleLeftImg(int resId, View.OnClickListener listener) {
        ImageView imageView = (ImageView) this.findViewById(R.id.base_title_left_img);
        imageView.setImageResource(resId);
        View ll_left_view = this.findViewById(R.id.base_title_left_view);
        ll_left_view.setVisibility(View.VISIBLE);
        ll_left_view.setOnClickListener(listener);
    }

    /**
     * 设置右侧带图片带文字的功能按钮
     *
     * @param titleRighttext 按钮文字
     * @param resId          图片id
     * @param listener       点击监听
     */
    public void setRightImgTextBtn(String titleRighttext, int resId, View.OnClickListener listener) {
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.base_title_right_img_text_btn);
        layout.setVisibility(View.VISIBLE);
        TextView textView = (TextView) this.findViewById(R.id.base_title_right_img_text);
        ImageView imageView = (ImageView) this.findViewById(R.id.base_title_right_img_view);
        textView.setText(titleRighttext);
        imageView.setImageResource(resId);
        layout.setOnClickListener(listener);
    }
}
