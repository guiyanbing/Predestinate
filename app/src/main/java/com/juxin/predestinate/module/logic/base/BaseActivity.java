package com.juxin.predestinate.module.logic.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 应用中所有activity的基类，便于进行数据的统计等
 * Created by ZRP on 2016/9/18.
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
