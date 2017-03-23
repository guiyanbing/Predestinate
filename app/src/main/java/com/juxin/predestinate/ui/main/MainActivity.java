package com.juxin.predestinate.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.ui.mail.MailFragment;
import com.juxin.predestinate.ui.plaza.PlazaFragment;
import com.juxin.predestinate.ui.user.UserFragment;
import com.juxin.predestinate.ui.xiaoyou.XiaoyouFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private MailFragment mailFragment;
    private XiaoyouFragment xiaoyouFragment;
    private PlazaFragment plazaFragment;
    private UserFragment userFragment;

    private BaseFragment current;  // 当前的fragment
    private View[] views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        initViews();
        initFragment();
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        mailFragment = new MailFragment();
        xiaoyouFragment = new XiaoyouFragment();
        plazaFragment = new PlazaFragment();
        userFragment = new UserFragment();

        switchContent(mailFragment);
    }

    private void initViews() {
        View mail_layout = findViewById(R.id.mail_layout);
        View xiaoyou_layout = findViewById(R.id.xiaoyou_layout);
        View plaza_layout = findViewById(R.id.plaza_layout);
        View user_layout = findViewById(R.id.user_layout);

        views = new View[]{mail_layout, xiaoyou_layout, plaza_layout, user_layout};

        mail_layout.setOnClickListener(this);
        xiaoyou_layout.setOnClickListener(this);
        plaza_layout.setOnClickListener(this);
        user_layout.setOnClickListener(this);
    }

    /**
     * 切换当前显示的fragment
     */
    private void switchContent(BaseFragment fragment) {
        tabSwitchStatus(fragment);

        if (current != fragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (current != null) {
                transaction.hide(current);
            }
            if (!fragment.isAdded()) { //先判断是否被add过
                transaction.add(R.id.content, fragment).commitAllowingStateLoss();
            } else {
                transaction.show(fragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            current = fragment;
        }
    }

    /**
     * fragment切换的时候的状态判断以及请求
     *
     * @param fragment 切换的fragment
     */
    private void tabSwitchStatus(BaseFragment fragment) {
        if(fragment == mailFragment){
            tabSwitchHandler.sendEmptyMessage(R.id.mail_layout);
        }else if(fragment == xiaoyouFragment){
            tabSwitchHandler.sendEmptyMessage(R.id.xiaoyou_layout);
        }else if(fragment == plazaFragment){
            tabSwitchHandler.sendEmptyMessage(R.id.plaza_layout);
        }else if(fragment == userFragment){
            tabSwitchHandler.sendEmptyMessage(R.id.user_layout);
        }
    }

    private Handler tabSwitchHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            for (View view : views) {
                // 底部tab选中效果
                view.setSelected(msg.what == view.getId());
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mail_layout:
                switchContent(mailFragment);
                break;
            case R.id.xiaoyou_layout:
                switchContent(xiaoyouFragment);
                break;
            case R.id.plaza_layout:
                switchContent(plazaFragment);
                break;
            case R.id.user_layout:
                switchContent(userFragment);
                break;
        }
    }
}
