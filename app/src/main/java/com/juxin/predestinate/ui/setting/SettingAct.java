package com.juxin.predestinate.ui.setting;

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
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.ApkUnit;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;


/**
 * 设置页面
 *
 * @author xy
 */
public class SettingAct extends BaseActivity implements OnClickListener, RequestComplete {

    private TextView setting_clear_cache_tv, setting_account;
    private ToggleButton setting_message_iv, setting_vibration_iv, setting_voice_iv, setting_quit_message_iv, settingVideoIv, settingAudioIv;

    // false是开true是开
    private Boolean Message_Status, Vibration_Status, Voice_Status, Quit_Message_Status, videoStatus, audioStatus;

    private VideoVerifyBean videoVerifyBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_settings_layout);
        setBackView(getResources().getString(R.string.title_set));
        initView();
        InitPreference();
    }

    private void initView() {
        ModuleMgr.getCommonMgr().requestVideochatConfig();
        findViewById(R.id.setting_modifypwd).setOnClickListener(this);
        findViewById(R.id.setting_message).setOnClickListener(this);
        findViewById(R.id.setting_vibration).setOnClickListener(this);
        findViewById(R.id.setting_voice).setOnClickListener(this);
        findViewById(R.id.setting_feedback).setOnClickListener(this);
        findViewById(R.id.setting_update).setOnClickListener(this);
        findViewById(R.id.setting_about).setOnClickListener(this);
        findViewById(R.id.setting_clear_cache).setOnClickListener(this);
        findViewById(R.id.setting_logoff).setOnClickListener(this);
        findViewById(R.id.setting_action).setOnClickListener(this);
        findViewById(R.id.setting_quit_message).setOnClickListener(this);
        findViewById(R.id.setting_video_switch).setOnClickListener(this);
        findViewById(R.id.setting_audio_switch).setOnClickListener(this);

        setting_clear_cache_tv = (TextView) findViewById(R.id.setting_clear_cache_tv);
        setting_account = (TextView) findViewById(R.id.setting_account);

        setting_message_iv = (ToggleButton) findViewById(R.id.setting_message_iv);
        setting_vibration_iv = (ToggleButton) findViewById(R.id.setting_vibration_iv);
        setting_voice_iv = (ToggleButton) findViewById(R.id.setting_voice_iv);
        setting_quit_message_iv = (ToggleButton) findViewById(R.id.setting_quit_message_iv);
        settingVideoIv = (ToggleButton) findViewById(R.id.setting_video_switch_iv);
        settingAudioIv = (ToggleButton) findViewById(R.id.setting_audio_switch_iv);

        long uid = ModuleMgr.getLoginMgr().getUserList().get(0).getUid();
        if (uid != 0) {
            setting_account.setText(getResources().getString(R.string.txt_set_topuid_desc) + uid);
            setting_account.setVisibility(View.VISIBLE);
        }

        String cacheSize = DirType.getCacheSize();
        setting_clear_cache_tv.setText(cacheSize);
    }

    private void InitPreference() {
        videoVerifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
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

        setVideoAndAudioStatus();
    }

    private void setVideoAndAudioStatus(){
        if (videoVerifyBean.getBooleanVideochat()) {
            videoStatus = true;
            settingVideoIv.setBackgroundResource(R.drawable.f1_setting_ok);
        } else {
            videoStatus = false;
            settingVideoIv.setBackgroundResource(0);
        }

        if (videoVerifyBean.getBooleanAudiochat()) {
            audioStatus = true;
            settingAudioIv.setBackgroundResource(R.drawable.f1_setting_ok);
        } else {
            audioStatus = false;
            settingAudioIv.setBackgroundResource(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_message:// 消息
                Statistics.userBehavior(SendPoint.menu_me_setting_newmsgalert);

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
                Statistics.userBehavior(SendPoint.menu_me_setting_shockalert);

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
                Statistics.userBehavior(SendPoint.menu_me_setting_soundalert);

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
                Statistics.userBehavior(SendPoint.menu_me_setting_exitmsgalert);
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
                Statistics.userBehavior(SendPoint.menu_me_setting_modifypassword);
                UIShow.showModifyAct(this);
                break;
            case R.id.setting_feedback:// 意见反馈
                Statistics.userBehavior(SendPoint.menu_me_setting_feedback);
                UIShow.showSuggestAct(this);
                break;
            case R.id.setting_update:// 软件更新
                Statistics.userBehavior(SendPoint.menu_me_setting_checkupdates);
                ModuleMgr.getCommonMgr().checkUpdate(this, true);//检查应用升级
                break;

            case R.id.setting_action:// 活动相关
                Statistics.userBehavior(SendPoint.menu_me_setting_huodong);
                UIShow.showActionActivity(this);
                break;
            case R.id.setting_about:// 关于
                Statistics.userBehavior(SendPoint.menu_me_setting_about);
                UIShow.showAboutAct(SettingAct.this);
                break;
            case R.id.setting_clear_cache:// 清除缓存
                Statistics.userBehavior(SendPoint.menu_me_setting_clearcache);
                clearAppCache();
                break;
            case R.id.setting_logoff:// 退出登录
                Statistics.userBehavior(SendPoint.menu_me_setting_signout);
                PickerDialogUtil.showSimpleTipDialog(this, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        exitLogin();
                    }
                }, getResources().getString(R.string.dal_exit_content), getResources().getString(R.string.dal_exit_title), getResources().getString(R.string.cancel), getResources().getString(R.string.ok), true);
                break;
            case R.id.setting_video_switch: {//视频通话开关
                Statistics.userBehavior(SendPoint.menu_me_setting_enablevideo);

                if (validChange()) {
                    if (videoStatus) {
                        videoStatus = false;
                        settingVideoIv.setBackgroundResource(0);
                        videoVerifyBean.setVideochat(0);
                    } else {
                        videoStatus = true;
                        settingVideoIv.setBackgroundResource(R.drawable.f1_setting_ok);
                        videoVerifyBean.setVideochat(1);
                    }
                    ModuleMgr.getCommonMgr().setVideochatConfig(videoStatus, audioStatus,this);
                }
                break;
            }
            case R.id.setting_audio_switch: {//语音通话开关
                Statistics.userBehavior(SendPoint.menu_me_setting_enablevoice);

                if (validChange()) {
                    if (audioStatus) {
                        audioStatus = false;
                        settingAudioIv.setBackgroundResource(0);
                        videoVerifyBean.setAudiochat(0);
                    } else {
                        audioStatus = true;
                        settingAudioIv.setBackgroundResource(R.drawable.f1_setting_ok);
                        videoVerifyBean.setAudiochat(1);
                    }
                    ModuleMgr.getCommonMgr().setVideochatConfig(videoStatus, audioStatus, this);
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 切换音视频开关配置
     */
    public boolean validChange() {
        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        //开启音、视频通话时，男性用户判断是否VIP
        if (userDetail.getGender() == 1 && !userDetail.isVip()) {

            PickerDialogUtil.showSimpleTipDialogExt(SettingAct.this, new SimpleTipDialog.ConfirmListener() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onSubmit() {
                    UIShow.showOpenVipActivity(SettingAct.this);
                }
            }, getResources().getString(R.string.dal_vip_content), "", getResources().getString(R.string.cancel), getResources().getString(R.string.dal_vip_open), true, R.color.text_zhuyao_black);
            return false;
        }
        //开启音、视频通话时，女性用户判断是否视频认证
        if (userDetail.getGender() == 2) {
            if (videoVerifyBean.getStatus() == 0 || videoVerifyBean.getStatus() == 2) {
                PickerDialogUtil.showSimpleTipDialogExt(SettingAct.this, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onSubmit() {
                        UIShow.showMyAuthenticationVideoAct(SettingAct.this, 0);
                    }
                }, getResources().getString(R.string.dal_auth_content), "", getResources().getString(R.string.cancel), getResources().getString(R.string.dal_auth_open), true, R.color.text_zhuyao_black);
                return false;
            } else if (videoVerifyBean.getStatus() == 1) {
                PToast.showShort(getResources().getString(R.string.toast_under_review));
                return false;
            }
        }
        if (ApkUnit.getAppIsInstall(SettingAct.this, VideoAudioChatHelper.PACKAGE_PLUGIN_VIDEO)
                && ApkUnit.getInstallAppVer(SettingAct.this, VideoAudioChatHelper.PACKAGE_PLUGIN_VIDEO) >= ModuleMgr.getCommonMgr().getCommonConfig().getPlugin_version()) {
            return true;
        } else {
            VideoAudioChatHelper.getInstance().downloadVideoPlugin(SettingAct.this);
            return false;
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
        ModuleMgr.getCenterMgr().clearUserInfo();
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
                    DirType.clearCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()){
            if (settingVideoIv != null && settingAudioIv != null && SettingAct.this != null){
                videoVerifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
                setVideoAndAudioStatus();
            }
        }
    }
}