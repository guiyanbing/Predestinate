package com.xiaochen.android.fate_it.module.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xiaochen.android.fate_it.R;

/**
 * 应用中所有activity的基类，便于进行数据的统计等
 * Created by ZRP on 2016/9/18.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initState();
    }

    /**
     * 沉浸式状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏

            // create our manager instance after the content view is set
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
            tintManager.setNavigationBarTintEnabled(true);
            // set a custom tint color for all system bars
            tintManager.setTintColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    // ==========================ToolBar处理 start==========================

    /**
     * @return 获取activity布局中的Toolbar
     */
    public Toolbar getToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);//toolbar支持
        return toolbar;
    }

    private View.OnClickListener onNavigationClickListener;//导航图标点击事件

    /**
     * 设置导航图标点击监听
     *
     * @param onClickListener 点击事件
     */
    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        this.onNavigationClickListener = onClickListener;
    }

    /**
     * 设置导航图标
     *
     * @param res             导航图标资源文件
     * @param onClickListener 导航图标点击事件
     */
    public void setNavigationButton(@DrawableRes int res, View.OnClickListener onClickListener) {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            if (res != -1) toolbar.setNavigationIcon(res);
            setNavigationOnClickListener(onClickListener);
        }
    }

    /**
     * 设置返回按钮
     */
    public void setBackView() {
        setNavigationButton(R.drawable.ico_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    /**
     * 设置标题
     *
     * @param tittle 标题文字
     */
    public void setTittle(String tittle) {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(tittle);
            toolbar.setTitleTextColor(Color.WHITE);//标题颜色
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (onNavigationClickListener != null) {
                onNavigationClickListener.onClick(item.getActionView());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ==========================ToolBar处理 end==========================

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
}
