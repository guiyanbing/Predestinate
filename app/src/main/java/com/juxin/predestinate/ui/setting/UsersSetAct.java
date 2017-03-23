package com.juxin.predestinate.ui.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * 设置
 * Created YAO on 2016/5/27.
 */
public class UsersSetAct extends BaseActivity implements View.OnClickListener {
    private TextView desc_clean;
    private static String cache;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r1_users_set);
        setBackView(R.id.back_view, "设置");
        initView();
    }


    static class MyHandler extends Handler {
        final WeakReference<UsersSetAct> mActivity;

        MyHandler(UsersSetAct act) {
            mActivity = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            UsersSetAct act = mActivity.get();
            switch (msg.what) {
                case 1:
                    act.desc_clean.setText(cache);
                    break;
                case 2:
                    act.desc_clean.setText("缓存状态良好");
                    if (cache.equals("缓存状态良好")) {
                        PToast.showShort("缓存状态良好，不需要清理");
                    } else {
                        PToast.showShort("缓存已清除");
                    }
                    break;
            }
        }
    }

    private void initView() {
        handler = new MyHandler(this);
        findViewById(R.id.rl_exit).setOnClickListener(this);
        findViewById(R.id.rl_clean).setOnClickListener(this);
        desc_clean = (TextView) findViewById(R.id.desc_clean);
        findViewById(R.id.rl_feedback).setOnClickListener(this);
        LinearLayout users_content_head = (LinearLayout) findViewById(R.id.users_content_head);
        SetNoticePanel noticePanel = new SetNoticePanel(UsersSetAct.this);
//        users_content_head.addView(noticePanel.getContentView());

        new Thread(new Runnable() {
            @Override
            public void run() {
//                cache = DirUtils.getFormtCacheSize();
                handler.sendEmptyMessage(1);
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rl_exit://退出登录
//                PickerDialogUtil.showSimpleAlertDialog(UsersSetAct.this, new SimpleTipDialog.ConfirmListener() {
//                    @Override
//                    public void onCancel() {
//                    }
//
//                    @Override
//                    public void onSubmit() {
//                        ModuleMgr.getLoginMgr().removeCookie(UsersSetAct.this);
//                        NotificationsUtils.cancelAll();//如果还有通知栏提示，在退出帐号的时候全部清掉
//                        ModuleMgr.getLoginMgr().logout();
//                        setResult(200);
//                        finish();
//                    }
//                }, "确定退出登录吗？",R.color.text_zhuyao_black, "");
//                break;
//            case R.id.rl_clean://  清除缓存
//                PickerDialogUtil.showSimpleTipDialogExt(UsersSetAct.this, new SimpleTipDialog.ConfirmListener() {
//                    @Override
//                    public void onCancel() {
//                    }
//
//                    @Override
//                    public void onSubmit() {
//                        new Thread() {
//                            public void run() {
//                                Message msg = new Message();
//                                try {
//                                    DirUtils.clearCache();
//                                    msg.what = 2;
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    msg.what = -1;
//                                }
//                                handler.sendMessage(msg);
//                            }
//                        }.start();
//                    }
//                }, "根据缓存文件的大小，清理时间将持续几秒至几分钟不等，请耐心等待", "清除缓存", "取消", "清理", true, R.color.text_zhuyao_black);
//                break;
//            case R.id.rl_feedback://意见反馈客服
////                UIShow.showPrivateChatAct(this, MailSpecialID.customerService.getSpecialID(), MailSpecialID.customerService.getSpecialIDName());
////                UIShow.showFeedBackAct(this);
//                UIShow.showAboutAct(this);
//                break;
//        }
    }
}
