package com.juxin.predestinate.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.unread.BadgeView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.ChatSpecialMgr;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.discover.DiscoverFragment;
import com.juxin.predestinate.ui.mail.MailFragment;
import com.juxin.predestinate.ui.user.auth.MyAuthenticationAct;
import com.juxin.predestinate.ui.user.fragment.UserFragment;
import com.juxin.predestinate.ui.web.RankFragment;
import com.juxin.predestinate.ui.web.WebFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, ChatMsgInterface.WhisperMsgListener, PObserver {

    private FragmentManager fragmentManager;
    private DiscoverFragment discoverFragment;
    private MailFragment mailFragment;
    private RankFragment rankFragment;
    private WebFragment plazaFragment;
    private UserFragment userFragment;

    private BaseFragment current;  // 当前的fragment
    private View[] views;
    private View layout_main_bottom;

    private BadgeView mail_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        initViews();
        initFragment();
        initData();
        initListenerAndRequest();
    }

    private void initData() {
        ModuleMgr.getCommonMgr().requestVideochatConfig();//获取设置音视频配置
        ModuleMgr.getCommonMgr().checkUpdate(this, false);//检查应用升级
        UIShow.showWebPushDialog(this);//内部根据在线配置判断是否展示活动推送弹窗
        ModuleMgr.getCommonMgr().showSayHelloDialog(this);
    }

    /**
     * 初始化监听与请求
     */
    private void initListenerAndRequest() {
        MsgMgr.getInstance().attach(this);
        onMsgNum(ModuleMgr.getChatListMgr().getUnreadNumber());
        ChatSpecialMgr.getChatSpecialMgr().attachWhisperListener(this);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        discoverFragment = new DiscoverFragment();
        mailFragment = new MailFragment();
        rankFragment = new RankFragment();
        plazaFragment = new WebFragment(getResources().getString(R.string.main_btn_plaza),
                "http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/square/square.html");// TODO: 2017/5/3
        userFragment = new UserFragment();

        switchContent(discoverFragment);
    }

    private void initViews() {
        View discovery_layout = findViewById(R.id.discovery_layout);
        View mail_layout = findViewById(R.id.mail_layout);
        View rank_layout = findViewById(R.id.rank_layout);
        View plaza_layout = findViewById(R.id.plaza_layout);
        View user_layout = findViewById(R.id.user_layout);

        views = new View[]{discovery_layout, mail_layout, rank_layout, plaza_layout, user_layout};

        discovery_layout.setOnClickListener(this);
        mail_layout.setOnClickListener(this);
        rank_layout.setOnClickListener(this);
        plaza_layout.setOnClickListener(this);
        user_layout.setOnClickListener(this);

        mail_num = (BadgeView) findViewById(R.id.mail_num);
        layout_main_bottom = findViewById(R.id.layout_main_bottom);
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
        if (fragment == discoverFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.discovery_layout);
        } else if (fragment == mailFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.mail_layout);
        } else if (fragment == rankFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.rank_layout);
        } else if (fragment == plazaFragment) {
            tabSwitchHandler.sendEmptyMessage(R.id.plaza_layout);
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
            case R.id.discovery_layout:
                switchContent(discoverFragment);
                break;
            case R.id.mail_layout:
                switchContent(mailFragment);
                break;
            case R.id.rank_layout:
                switchContent(rankFragment);
                break;
            case R.id.plaza_layout:
                switchContent(plazaFragment);
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
        if (requestCode == MyAuthenticationAct.AUTHENTICSTION_REQUESTCODE && resultCode == 200) {//手机绑定成功,跳转到登录页
            UIShow.showUserLoginExtAct(this);
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
            switchContent(discoverFragment);
        } else if (FinalKey.MAIN_TAB_2 == tab_type) {//跳转到消息tab
            switchContent(mailFragment);
        } else if (FinalKey.MAIN_TAB_3 == tab_type) {//跳转到风云榜tab
            switchContent(rankFragment);
        } else if (FinalKey.MAIN_TAB_4 == tab_type) {//跳转到广场tab
            switchContent(plazaFragment);
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

    public void onGoneBottom(boolean isGone) {
        layout_main_bottom.setVisibility(isGone ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onUpdateWhisper(BaseMessage message) {
        if (!TextUtils.isEmpty(message.getWhisperID())) {
            PLogger.printObject("message====" + message);
            ModuleMgr.getChatListMgr().getWhisperList();
        }
    }

    private void onMsgNum(int num) {
        mail_num.setText(ModuleMgr.getChatListMgr().getUnreadTotalNum(num));
        mail_num.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_User_List_Msg_Change:
                TimerUtil.beginTime(new TimerUtil.CallBack() {
                    @Override
                    public void call() {
                        onMsgNum(ModuleMgr.getChatListMgr().getUnreadNumber());
                    }
                }, 200);
                break;
            default:
                break;
        }
    }
}
