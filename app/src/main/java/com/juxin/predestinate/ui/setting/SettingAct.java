package com.juxin.predestinate.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.WebUtil;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.SDCardUtil;
import com.juxin.predestinate.module.util.UIShow;

import java.io.File;


/**
 * 设置页面`
 *
 * @author Kind
 */
public class SettingAct extends BaseActivity implements OnClickListener {

    private TextView setting_clear_cache_tv, setting_account;
    private ToggleButton setting_message_iv, setting_vibration_iv, setting_voice_iv, setting_quit_message_iv;

    // false是开true是开
    private Boolean Message_Status, Vibration_Status, Voice_Status, Quit_Message_Status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_settings_layout);
        setBackView(getResources().getString(R.string.title_set));
        initView();
        InitPreference();
    }


    private void initView() {
        findViewById(R.id.setting_modifypwd).setOnClickListener(this);
        findViewById(R.id.setting_message).setOnClickListener(this);
        findViewById(R.id.setting_vibration).setOnClickListener(this);
        findViewById(R.id.setting_voice).setOnClickListener(this);
        findViewById(R.id.setting_feedback).setOnClickListener(this);
        findViewById(R.id.setting_update).setOnClickListener(this);
        findViewById(R.id.setting_recommend).setOnClickListener(this);
        findViewById(R.id.setting_about).setOnClickListener(this);
        findViewById(R.id.setting_clear_cache).setOnClickListener(this);
        findViewById(R.id.setting_logoff).setOnClickListener(this);
        findViewById(R.id.setting_action).setOnClickListener(this);
        findViewById(R.id.setting_quit_message).setOnClickListener(this);

        setting_clear_cache_tv = (TextView) findViewById(R.id.setting_clear_cache_tv);
        setting_account = (TextView) findViewById(R.id.setting_account);

        setting_message_iv = (ToggleButton) findViewById(R.id.setting_message_iv);
        setting_vibration_iv = (ToggleButton) findViewById(R.id.setting_vibration_iv);
        setting_voice_iv = (ToggleButton) findViewById(R.id.setting_voice_iv);
        setting_quit_message_iv = (ToggleButton) findViewById(R.id.setting_quit_message_iv);

        long uid = ModuleMgr.getLoginMgr().getUserList().get(0).getUid();
        if (uid != 0) {
            setting_account.setText(getResources().getString(R.string.txt_set_topuid_desc) + uid);
            setting_account.setVisibility(View.VISIBLE);
        }

        String cacheSize = Show_Cache();
        setting_clear_cache_tv.setText(cacheSize);
    }

    private void InitPreference() {
        if (PSP.getInstance().getBoolean(Constant.SETTING_MESSAGE, Constant.SETTING_MESSAGE_DEFAULT)) {
            Message_Status = true;
            setting_message_iv.setBackgroundResource(R.drawable.f1_setting_ok);
        } else {
            Message_Status = false;
            setting_message_iv.setBackgroundResource(0);
        }

        if (PSP.getInstance().getBoolean(Constant.SETTING_VIBRATION, Constant.SETTING_VIBRATION_DEFAULT)) {
            Vibration_Status = true;
            setting_vibration_iv.setBackgroundResource(R.drawable.f1_setting_ok);
        } else {
            Vibration_Status = false;
            setting_vibration_iv.setBackgroundResource(0);
        }

        if (PSP.getInstance().getBoolean(Constant.SETTING_VOICE, Constant.SETTING_VOICE_DEFAULT)) {
            Voice_Status = true;
            setting_voice_iv.setBackgroundResource(R.drawable.f1_setting_ok);
        } else {
            Voice_Status = false;
            setting_voice_iv.setBackgroundResource(0);
        }

        if (PSP.getInstance().getBoolean(Constant.SETTING_QUIT_MESSAGE, Constant.SETTING_QUIT_MESSAGE_DEFAULT)) {
            Quit_Message_Status = true;
            setting_quit_message_iv.setBackgroundResource(R.drawable.f1_setting_ok);
        } else {
            Quit_Message_Status = false;
            setting_quit_message_iv.setBackgroundResource(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_message:// 消息
                if (Message_Status) {
                    Message_Status = false;
                    PSP.getInstance().put(Constant.SETTING_MESSAGE, Message_Status);
                    setting_message_iv.setBackgroundResource(0);
                } else {
                    Message_Status = true;
                    PSP.getInstance().put(Constant.SETTING_MESSAGE, Message_Status);
                    setting_message_iv.setBackgroundResource(R.drawable.f1_setting_ok);
                }
                break;
            case R.id.setting_vibration:// 震动
                if (Vibration_Status) {
                    Vibration_Status = false;
                    PSP.getInstance().put(Constant.SETTING_VIBRATION, Vibration_Status);
                    setting_vibration_iv.setBackgroundResource(0);
                } else {
                    Vibration_Status = true;
                    PSP.getInstance().put(Constant.SETTING_VIBRATION, Vibration_Status);
                    setting_vibration_iv.setBackgroundResource(R.drawable.f1_setting_ok);
                }
                break;
            case R.id.setting_voice:// 声音
                if (Voice_Status) {
                    Voice_Status = false;
                    PSP.getInstance().put(Constant.SETTING_VOICE, Voice_Status);
                    setting_voice_iv.setBackgroundResource(0);
                } else {
                    Voice_Status = true;
                    PSP.getInstance().put(Constant.SETTING_VOICE, Voice_Status);
                    setting_voice_iv.setBackgroundResource(R.drawable.f1_setting_ok);
                }
                break;
            case R.id.setting_quit_message:// 退出消息
                if (Quit_Message_Status) {
                    Quit_Message_Status = false;
                    PSP.getInstance().put(Constant.SETTING_QUIT_MESSAGE, Quit_Message_Status);
                    setting_quit_message_iv.setBackgroundResource(0);
                } else {
                    Quit_Message_Status = true;
                    PSP.getInstance().put(Constant.SETTING_QUIT_MESSAGE, Quit_Message_Status);
                    setting_quit_message_iv.setBackgroundResource(R.drawable.f1_setting_ok);
                }
                break;
            case R.id.setting_modifypwd: // 修改密码
                UIShow.showModifyAct(this);
                break;
            case R.id.setting_feedback:// 意见反馈
                UIShow.showSuggestAct(this);
                break;
            case R.id.setting_update:// 软件更新
                ModuleMgr.getCommonMgr().checkUpdate(this, true);//检查应用升级
                break;
            case R.id.setting_recommend:// 积分墙
                PToast.showShort("努力开发中...");
                break;
            case R.id.setting_action:// 活动相关
                UIShow.showWebActivity(this, WebUtil.jointUrl("http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/setting/activity.html"));
                // TODO: 2017/5/3
                break;
            case R.id.setting_about:// 关于
                UIShow.showAboutAct(SettingAct.this);
                break;
            case R.id.setting_clear_cache:// 清除缓存
                clearAppCache();
                break;
            case R.id.setting_logoff:// 退出登录
                PickerDialogUtil.showSimpleTipDialog(this, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        exitLogin();
                    }
                }, getResources().getString(R.string.dal_exit_content), getResources().getString(R.string.dal_exit_title), getResources().getString(R.string.dal_cancle), getResources().getString(R.string.dal_submit), true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            exitLogin();
        }
    }

    public void exitLogin() {
        clearUserInfo();
        setResult(200);
        finish();
    }

    public static void clearUserInfo() {
        // 清除当前登录的用户信息
        ModuleMgr.getLoginMgr().logout();

        PSP.getInstance().put("addMsgToUserDate", null);
        PSP.getInstance().put("recommendDate", null);
    }

    public void clearAppCache() {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    PToast.showLong(getResources().getString(R.string.toast_clearcache_ok));
                    setting_clear_cache_tv.setText("0KB");
                } else {
                    PToast.showLong(getResources().getString(R.string.toast_clearcache_error));
                }
            }
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    clearCache();
                    clearDB();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    private String Show_Cache() {
        // 计算缓存大小
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = App.context.getFilesDir();
        File cacheDir = App.context.getCacheDir();

        fileSize += SDCardUtil.getDirSize(filesDir);
        fileSize += SDCardUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = getExternalCacheDir(App.context);
            fileSize += SDCardUtil.getDirSize(externalCacheDir);
        }
        if (fileSize > 0)
            cacheSize = SDCardUtil.formatFileSize(fileSize);
        return cacheSize;
    }

    private void clearCache() {
        SDCardUtil.clearCacheFolder(App.context.getFilesDir(), System.currentTimeMillis());
        SDCardUtil.clearCacheFolder(App.context.getCacheDir(), System.currentTimeMillis());
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            SDCardUtil.clearCacheFolder(getExternalCacheDir(App.context), System.currentTimeMillis());
        }
    }


    private void clearDB() {
//        DataCenter.getInstance().delete_User_Message_List_MoreThanNHour_2(AppCtx.getUid(), 48);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     */
    public boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }


}