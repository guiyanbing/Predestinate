package com.juxin.predestinate.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.MailFragment;
import com.juxin.predestinate.ui.user.fragment.UserFragment;
import com.juxin.predestinate.ui.web.WebFragment;
import com.juxin.predestinate.ui.xiaoyou.XiaoyouFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private MailFragment mailFragment;
    private XiaoyouFragment xiaoyouFragment;
    private WebFragment rankFragment, webFragment;
    private UserFragment userFragment;

    private BaseFragment current;  // 当前的fragment
    private View[] views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        initViews();
        initFragment();
        initData();
    }

    private void initData() {
        ModuleMgr.getCommonMgr().checkUpdate(this, false);//检查应用升级
        UIShow.showWebPushDialog(this);//内部根据在线配置判断是否展示活动推送弹窗
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        mailFragment = new MailFragment();
        xiaoyouFragment = new XiaoyouFragment();
        rankFragment = new WebFragment(getResources().getString(R.string.main_btn_plaza),
                ModuleMgr.getCommonMgr().getCommonConfig().getEntrance_url());
        webFragment = new WebFragment(getResources().getString(R.string.main_btn_web),
                ModuleMgr.getCommonMgr().getCommonConfig().getEntrance_url());
        userFragment = new UserFragment();

        switchContent(mailFragment);
    }

    private void initViews() {
        View mail_layout = findViewById(R.id.mail_layout);
        View xiaoyou_layout = findViewById(R.id.xiaoyou_layout);
        View plaza_layout = findViewById(R.id.plaza_layout);
        View web_layout = findViewById(R.id.web_layout);
        View user_layout = findViewById(R.id.user_layout);

        views = new View[]{mail_layout, xiaoyou_layout, plaza_layout, web_layout, user_layout};

        mail_layout.setOnClickListener(this);
        xiaoyou_layout.setOnClickListener(this);
        plaza_layout.setOnClickListener(this);
        web_layout.setOnClickListener(this);
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
        if (fragment == mailFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.mail_layout);
        } else if (fragment == xiaoyouFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.xiaoyou_layout);
        } else if (fragment == rankFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.plaza_layout);
        } else if (fragment == webFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.web_layout);
        } else if (fragment == userFragment) {
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
                switchContent(rankFragment);
                break;
            case R.id.web_layout:
                switchContent(webFragment);
                break;
            case R.id.user_layout:
                switchContent(userFragment);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            UIShow.showNavUserAct(this);
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeTab(intent.getIntExtra(FinalKey.HOME_TAB_TYPE, -1), intent);
    }

    /**
     * 切换首页tab
     *
     * @param tab_type tab类型
     * @param intent   跳转intent，默认传null
     */
    public void changeTab(int tab_type, Intent intent) {
        PLogger.d("---changeTab--->tab_type：" + tab_type);
        if (FinalKey.MAIN_TAB_1 == tab_type) {//跳转到发现tab
            switchContent(mailFragment);
        } else if (FinalKey.MAIN_TAB_2 == tab_type) {//跳转到消息tab
            switchContent(xiaoyouFragment);
        } else if (FinalKey.MAIN_TAB_3 == tab_type) {//跳转到风云榜tab
            switchContent(rankFragment);
        } else if (FinalKey.MAIN_TAB_4 == tab_type) {//跳转到广场tab
            switchContent(webFragment);
        } else if (FinalKey.MAIN_TAB_5 == tab_type) {//跳转到我的tab
            switchContent(userFragment);
        }
    }

    private long firstExitTime = 0;// 用于判断双击退出时间间隔

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long doubleExitTime = System.currentTimeMillis();
            if (doubleExitTime - firstExitTime < 2000) {
                finish();//假退出，只关闭当前页面
            } else {
                firstExitTime = doubleExitTime;
                PToast.showShort(getResources().getString(R.string.tip_quit));
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
