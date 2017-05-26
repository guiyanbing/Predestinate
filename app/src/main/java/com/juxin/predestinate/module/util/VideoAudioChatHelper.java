package com.juxin.predestinate.module.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.utils.JniUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.config.CommonConfig;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.module.local.common.CommonMgr;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.model.impl.HttpMgrImpl;
import com.juxin.predestinate.module.logic.model.mgr.HttpMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.setting.SettingAct;
import com.juxin.predestinate.ui.utils.Common;
import com.juxin.predestinate.ui.utils.DownloadPluginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import static com.juxin.predestinate.module.logic.application.App.context;
import static com.juxin.predestinate.module.logic.application.App.uid;


/**
 * 音视频通话相关功能类
 */

public class VideoAudioChatHelper{
    //音视频插件的包名
    public static final String PACKAGE_PLUGIN_VIDEO = "com.juxin.predestinate.assist";
    /**
     * 视频聊天
     */
    public static final int TYPE_VIDEO_CHAT = 1;
    /**
     * 语音聊天
     */
    public static final int TYPE_AUDIO_CHAT = 2;
    private static final int OPT_INVITE = 1;
    private static final int OPT_TOGGLE = 2;
    private static VideoAudioChatHelper instance;
    private VaCallback vaCallback;
    private DownloadPluginFragment downloadPluginFragment = new DownloadPluginFragment();
    private boolean isDownloading = false;
    private int lastOpt = 0;
    private int chatType;
    private static final String TEST_URL = "http://123.59.187.32/plugin/ai_plugin_videotel.apk";
    private long lastOptTime = 0;

    private VideoAudioChatHelper() {

    }

    public static VideoAudioChatHelper getInstance() {
        if (instance == null) {
            synchronized (VideoAudioChatHelper.class) {
                if (instance == null) {
                    instance = new VideoAudioChatHelper();
                }
            }
        }
        return instance;
    }


    public void getSelfVaConfig(Context context) {
//        AsyncTaskUtil.getInstance(context).setUrl(AppCfg.FATE_GO_GET_SELF_VA_CONFIG).setWhat(TASK_WHAT_GET_SELF_VA_CONFIG).setOnAsyncCallback(this).executeTask();
    }

    /**
     * 邀请对方音频或视频聊天
     *
     * @param context Activity
     * @param dstUid  对方UID
     * @param type    {@link TYPE_VIDEO_CHAT,TYPE_AUDIO_CHAT}   1为视频，2为音频
     */
    public void inviteVAChat(final Activity context, long dstUid, int type) {
        lastOpt = OPT_INVITE;
        if (!ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO) || ApkUnit.getInstallAppVer(context, PACKAGE_PLUGIN_VIDEO) < ModuleMgr.getCommonMgr().getCommonConfig().getPlugin_version()) {
            downloadVideoPlugin(context);
            return;
        }

        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        if(userDetail.getGender() == 1){
            boolean isTip = false;
            switch (type) {
                case TYPE_VIDEO_CHAT:
                    isTip = ModuleMgr.getCommonMgr().getCommonConfig().isVideoCallNeedVip() && !userDetail.isVip();
                    break;
                case TYPE_AUDIO_CHAT:
                    isTip = ModuleMgr.getCommonMgr().getCommonConfig().isAudioCallNeedVip() && !userDetail.isVip();
                    break;
                default:
                    break;
            }

            if (isTip) {
                PickerDialogUtil.showSimpleTipDialogExt((FragmentActivity) context, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        UIShow.showOpenVipActivity(context);
                    }
                }, context.getResources().getString(R.string.dlg_need_vip), "", context.getResources().getString(R.string.cancel), context.getResources().getString(R.string.dal_vip_open), true, R.color.text_zhuyao_black);
                return;
            }

        }
        executeInviteChat(context, dstUid, type);
    }
//    private void createTipsVipDialog(final Activity activity,String title, String btnok, String btncancel,final int go) {
//        final Dialog dialog = new Dialog(activity, R.style.dialog);
//        View view = LayoutInflater.from(activity).inflate(
//                R.layout.dialog_picker, null);
//        TextView txt_title = (TextView) view.findViewById(R.id.dialog_title);
//        txt_title.setTextColor(Color.parseColor("#000000"));
//        Button btn_ok = (Button) view.findViewById(R.id.dialog_submit);
//        Button btn_cancel = (Button) view.findViewById(R.id.dialog_cancel);
//
//        txt_title.setText(title);
//        btn_ok.setText(btnok);
//        btn_cancel.setText(btncancel);
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Statistics.clickViewAlbum(Long.valueOf(uid), false, "cancel");
//                dialog.dismiss();
//            }
//        });
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Statistics.clickViewAlbum(Long.valueOf(uid), false, "buy_vip");
//                if(go == 1) {
//                    UIHelper.showVipMemberAct(activity);
//                }else if (go == 2){
//                    UIHelper.showMyAuthenticationVideoAct(activity,0);
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
//                return true;
//            }
//        });
//        dialog.setContentView(view);
//        dialog.show();
//    }

    /**
     * 切换音视频开关配置
     *
     * @param context
     * @param type
     */
    public void toggleVaConfig(Activity context, int type) {
        if(System.currentTimeMillis() - lastOptTime < 1000)
            return;
        lastOptTime = System.currentTimeMillis();
        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        //开启音、视频通话时，男性用户判断是否VIP
//        if(userDetail.getGender() == 1
//                && isOpenConfig(type)
//                && !userDetail.isMonthMail()){
//            createTipsVipDialog(context,"您非VIP会员，无法开启此功能","去开通","取消",1);
//            return;
//        }
        //开启音、视频通话时，女性用户判断是否视频认证
//        if(userDetail.getGender() == 2 && isOpenConfig(type)){
//            if(VideoVerifyBean.getStatus() == 0 || VideoVerifyBean.getStatus() == 2){
//                createTipsVipDialog(context,"开启视频、音频功能，需要通过视频认证","去认证","取消",2);
//                return;
//            }else if(VideoVerifyBean.getStatus() == 1){
//                T.showShort(context,"审核中，请稍后再试");
//                return;
//            }
//        }
        chatType = type;
        lastOpt = OPT_TOGGLE;
        if (ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO) && ApkUnit.getInstallAppVer(context, PACKAGE_PLUGIN_VIDEO) >= ModuleMgr.getCommonMgr().getCommonConfig().getPlugin_version()) {
            executeVaConfig(context, type);
        } else {
            downloadVideoPlugin(context);
        }
    }

    public void closeVaChatConfig(Context context){
        if(ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO))
            return;
        executeVaConfig(context,0);
    }

    /**
     * 检测是否需要下载视频插件
     */
    public void checkDownloadPlugin(FragmentActivity activity){
        VideoVerifyBean verifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
        if((verifyBean.getBooleanAudiochat() || verifyBean.getBooleanVideochat()) && !ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO))
            downloadVideoPlugin(activity);
    }

    private boolean isOpenConfig(int type){
//        if(type == TYPE_VIDEO_CHAT && !AppCtx.getBPreference(AppCtx.SETTING_VIDEO_CHAT, false))
//            return true;
//        if(type == TYPE_AUDIO_CHAT && !AppCtx.getBPreference(AppCtx.SETTING_AUDIO_CHAT, false))
//            return true;
        return false;
    }

    /**
     * 打开被邀请时的页面
     *
     * @param vcId     通话频道ID
     * @param dstUid   对方UID
     * @param chatType 1视频，2音频
     */
    public void openInvitedActivity(Activity activity, int vcId, long dstUid, int chatType) {
        startRtcInitActivity(activity, newBundle(vcId, dstUid, 2, chatType));
    }


    public void setVaCallback(VaCallback vaCallback) {
        this.vaCallback = vaCallback;
    }

    /**
     * 下载视频插件
     *
     * @param context
     */
    public void downloadVideoPlugin(final Context context) {
        if(!downloadPluginFragment.isAdded())
            downloadPluginFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "download");
        if (isDownloading) {
            return;
        }
        isDownloading = true;
        HttpMgr httpMgr = new HttpMgrImpl();
        String apkFile = Common.getCahceDir("apk") + Long.toString(new Date().getTime()) + ".apk";//AppCfg.ASet.getVideo_chat_apk_url()
        String downUrl = TextUtils.isEmpty(ModuleMgr.getCommonMgr().getCommonConfig().getVideo_chat_apk_url()) ? TEST_URL : ModuleMgr.getCommonMgr().getCommonConfig().getVideo_chat_apk_url();
        httpMgr.download(downUrl, apkFile, new DownloadListener() {
            @Override
            public void onStart(String url, String filePath) {
                isDownloading = true;
            }

            @Override
            public void onProcess(String url, int process, long size) {
                if(downloadPluginFragment.isAdded() && downloadPluginFragment.isVisible())
                    downloadPluginFragment.updateProgress(process);
            }

            @Override
            public void onSuccess(String url, String filePath) {
                isDownloading = false;
                downloadPluginFragment.dismiss();
                ApkUnit.ExecApkFile(context, filePath);
            }

            @Override
            public void onFail(String url, Throwable throwable) {
                isDownloading = false;
                downloadPluginFragment.dismiss();
            }
        });
    }

    public void executeLastOpt() {
        if (lastOpt == OPT_TOGGLE) {
//            executeVaConfig(context, chatType);
        }
    }

    private void executeInviteChat(final Context context, final long dstUid, final int type) {
        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("tuid", dstUid);
        postParams.put("vtype", type);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.inviteVideoChat, postParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                handleInviteChat(context, response.getResponseString(), dstUid, type);
            }
        });
    }

    /**
     * 音视频聊天配置开关
     *
     * @param context
     * @param mediaType 0全关，1toggle视频，2toggle音频
     */
    private void executeVaConfig(final Context context, int mediaType) {
//        int videoChat = 0, audioChat = 0;
//        switch (mediaType) {
//            case 1:
//                videoChat = AppCtx.getBPreference(AppCtx.SETTING_VIDEO_CHAT, false) ? 0 : 1;
//                audioChat = AppCtx.getBPreference(AppCtx.SETTING_AUDIO_CHAT) ? 1 : 0;
//                break;
//            case 2:
//                videoChat = AppCtx.getBPreference(AppCtx.SETTING_VIDEO_CHAT, false) ? 1 : 0;
//                audioChat = AppCtx.getBPreference(AppCtx.SETTING_AUDIO_CHAT) ? 0 : 1;
//                break;
//        }
//        final HashMap<String, Object> postParams = new HashMap<>();
//        postParams.put("videochat", videoChat);
//        postParams.put("audiochat", audioChat);
//        AsyncTaskUtil.getInstance(context)
//                .setUrl(AppCfg.FATE_GO_SET_VA_CONFIG).isDes(true).setMethod(BaseAsyncTask.METHOD_POST)
//                .setPostParams(postParams).setWhat(TASK_WHAT_VA_CONFIG).setOnAsyncCallback(new OnAsyncCallback() {
//            @Override
//            public void requestSuccess(BaseAsyncTask task) {
//                handleVaConfig(task.getJsonResult(true), Integer.parseInt(postParams.get("videochat").toString()), Integer.parseInt(postParams.get("audiochat").toString()));
//            }
//
//            @Override
//            public void requestFail(BaseAsyncTask task, Exception e) {
//                T.showShort(context, "请求失败");
//            }
//        }).executeTask();
    }


    private void handleInviteChat(final Context context, String jsonStr, long dstUid, int type) {
        try {
            //特殊错误码: 3001 用户正在视频聊天中 3002 该用户无法视频聊天 3003 钻石余额不足
            JSONObject jo = new JSONObject(jsonStr);
            if ("ok".equals(jo.optString("status"))) {
                JSONObject resJo = jo.getJSONObject("res");

//                AppCtx.add_VC_ID(resJo.getInt("vc_id"));
                Bundle bundle = newBundle(resJo.getInt("vc_id"), dstUid, 1, type);
                startRtcInitActivity(context, bundle);
            } else {
                int code = jo.optInt("code");
                if(code == 3003) {
                    UIShow.showGoodsDiamondDialog(context);
//                    int leastDiamond = type == TYPE_AUDIO_CHAT ? AppCfg.ASet.getVoice_cost_per_minute() : AppCfg.ASet.getVideo_cost_per_minute();
//                    if (AppModel.getInstance().getUserDetail().getDiamondsSum() < leastDiamond) {
//                        UIHelper.showDiamondsDlg((Activity) context, String.valueOf(dstUid), "1", leastDiamond - AppModel.getInstance().getUserDetail().getDiamondsSum(), true, true);
//                    }
                }
                PToast.showShort(jo.optString("msg"));
            }
        } catch (JSONException e) {
            PToast.showShort("数据异常");
            e.printStackTrace();
        }

    }

    /**
     * 创建bundle传输参数
     *
     * @param vcId       视频通道
     * @param dstUid     对方UID
     * @param inviteType 1邀请，2受邀
     * @param chatType   1视频，2音频
     * @return
     */
    private Bundle newBundle(int vcId, long dstUid, int inviteType, int chatType) {
        Bundle bundle = new Bundle();
        bundle.putString("vc_get_user_url", UrlParam.reqUserInfoSummary.getFinalUrl());
        bundle.putString("vc_cookie", "auth="+ModuleMgr.getLoginMgr().getCookieVerCode());
        bundle.putInt("vc_chat_type", chatType);
        bundle.putInt("vc_invite_type", inviteType);
        bundle.putInt("vc_id", vcId);
        bundle.putInt("vc_project", 1);
        bundle.putString("vc_channel", JniUtil.GetEncryptString("juxin_live_" + vcId));
        bundle.putString("vc_uid", ModuleMgr.getCenterMgr().getMyInfo().getUid() + "");
        bundle.putLong("vc_check_yellow",ModuleMgr.getCommonMgr().getCommonConfig().getCheckYellow() * 1000);
        bundle.putLong("vc_dst_uid,",dstUid);
        return bundle;
    }

    //调起视频插件
    private void startRtcInitActivity(Context context,Bundle bundle) {
        if (ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO)) {
            Intent intent = new Intent();
            intent.setClassName("com.juxin.predestinate.assist", "com.juxin.predestinate.assist.ui.RtcInitActivity");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }else{
            downloadVideoPlugin(context);
        }
    }

    private void handleVaConfig(String jsonStr, int videoChat, int audioChat) {
        try {
            JSONObject jo = new JSONObject(jsonStr);
            if ("ok".equals(jo.getString("status"))) {
//                AppCtx.putPreference(AppCtx.SETTING_VIDEO_CHAT, videoChat == 0 ? false : true);
//                AppCtx.putPreference(AppCtx.SETTING_AUDIO_CHAT, audioChat == 0 ? false : true);
                vaCallback(0, "ok");
            } else {
                vaCallback(1, jo.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void vaCallback(int code, String msg) {
        if (vaCallback != null) {
            vaCallback.onComplete(code, msg);
            vaCallback = null;
        }
    }

    private void handleGetSelfConfig(String jsonStr) {
        try {
//            AppJsonParser.selfVideochatConfig(jsonStr);
            vaCallback(0,"ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface VaCallback {
        /**
         * 网络请求回调
         *
         * @param code 0为成功
         * @param msg  服务器返回的msg
         */
        void onComplete(int code, String msg);
    }
}
