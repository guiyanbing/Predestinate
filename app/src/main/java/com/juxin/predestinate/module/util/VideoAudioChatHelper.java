package com.juxin.predestinate.module.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.utils.JniUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.model.impl.HttpMgrImpl;
import com.juxin.predestinate.module.logic.model.mgr.HttpMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.utils.Common;
import com.juxin.predestinate.ui.utils.DownloadPluginFragment;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import static com.juxin.predestinate.module.local.center.CenterMgr.INFO_SAVE_KEY;
import static com.juxin.predestinate.module.logic.application.App.context;

/**
 * 音视频通话相关功能类
 */
public class VideoAudioChatHelper {

    //音视频插件的包名
    public static final String PACKAGE_PLUGIN_VIDEO = "com.juxin.predestinate.assist";
    //备用下载地址
    private static final String TEST_URL = "http://123.59.187.32/plugin/ai_plugin_videotel.apk";

    /**
     * 视频聊天
     */
    public static final int TYPE_VIDEO_CHAT = 1;
    /**
     * 语音聊天
     */
    public static final int TYPE_AUDIO_CHAT = 2;
    private static VideoAudioChatHelper instance;
    private DownloadPluginFragment downloadPluginFragment = new DownloadPluginFragment();
    private boolean isDownloading = false;

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

    private boolean isSend = false;

    /**
     * 重置发起方uid，每次收到音视频挂断消息时进行调用
     */
    public void resetSend() {
        this.isSend = false;
    }

    public boolean isSend() {
        return isSend;
    }

    /**
     * 邀请对方音频或视频聊天
     * @param context
     * @param dstUid
     * @param type
     * @param flag  判断是否显示进场dlg
     */
    public void inviteVAChat(final Activity context, long dstUid, int type, boolean flag) {
        if(flag && PSP.getInstance().getInt(ModuleMgr.getCommonMgr().getPrivateKey(Constant.APPEAR_TYPE), 0) == 0) {
            UIShow.showLookAtHerDlg(context, dstUid);
            return;
        }
        inviteVAChat(context, dstUid, type);
    }

    /**
     * 邀请对方音频或视频聊天
     *
     * @param context Activity
     * @param dstUid  对方UID
     * @param type    1为视频{@link #TYPE_VIDEO_CHAT TYPE_VIDEO_CHAT}，2为音频{@link #TYPE_AUDIO_CHAT TYPE_AUDIO_CHAT}
     */
    public void inviteVAChat(final Activity context, long dstUid, int type) {
        isSend = true;

        if (!ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO) || ApkUnit.getInstallAppVer(context, PACKAGE_PLUGIN_VIDEO) < ModuleMgr.getCommonMgr().getCommonConfig().getPlugin_version()) {
            downloadVideoPlugin(context);
            return;
        }

        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        if (userDetail.getGender() == 1) {
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

    /**
     * 检测是否需要下载视频插件
     */
    public void checkDownloadPlugin(FragmentActivity activity) {
        VideoVerifyBean verifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
        if ((verifyBean.getBooleanAudiochat() || verifyBean.getBooleanVideochat()) && !ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO))
            downloadVideoPlugin(activity);
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

    /**
     * 下载视频插件
     *
     * @param context 上下文
     */
    public void downloadVideoPlugin(final Context context) {
        if (!downloadPluginFragment.isAdded())
            downloadPluginFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "download");
        if (isDownloading) return;

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
                if (downloadPluginFragment.isAdded() && downloadPluginFragment.isVisible())
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

    /**
     * 发起音视频聊天，生成聊天通道id
     *
     * @param context 上下文
     * @param dstUid  被邀请方uid
     * @param type    音视频类型
     */
    private void executeInviteChat(final Context context, final long dstUid, final int type) {
        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("tuid", dstUid);
        postParams.put("vtype", type);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.inviteVideoChat, postParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                handleInviteChat(context, response, dstUid, type);
            }
        });
    }

    private void handleInviteChat(final Context context, HttpResponse response, long dstUid, int type) {
        //特殊错误码: 3001 用户正在视频聊天中 3002 该用户无法视频聊天 3003 钻石余额不足
        JSONObject jo = response.getResponseJson();
        if (response.isOk()) {
            JSONObject resJo = jo.optJSONObject("res");
            int vcID = resJo.optInt("vc_id");
            ModuleMgr.getChatMgr().sendVideoMsgLocalSimulation(String.valueOf(dstUid), type, vcID);
            Bundle bundle = newBundle(vcID, dstUid, 1, type);
            startRtcInitActivity(context, bundle);
            return;
        }

        int code = jo.optInt("code");
        if (code == 3003) UIShow.showGoodsDiamondDialog(context);
        PToast.showShort(TextUtils.isEmpty(jo.optString("msg")) ? "数据异常" : jo.optString("msg"));
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
        bundle.putString("vc_cookie", ModuleMgr.getLoginMgr().getCookieVerCode());
        bundle.putInt("vc_chat_type", chatType);
        bundle.putInt("vc_invite_type", inviteType);
        bundle.putInt("vc_id", vcId);
        bundle.putInt("vc_project", 1);
        bundle.putString("vc_channel", JniUtil.GetEncryptString("juxin_live_" + vcId));
        bundle.putString("vc_uid", ModuleMgr.getCenterMgr().getMyInfo().getUid() + "");
        bundle.putLong("vc_check_yellow", ModuleMgr.getCommonMgr().getCommonConfig().getCheckYellow() * 1000);
        bundle.putLong("vc_check_yellow_first", ModuleMgr.getCommonMgr().getCommonConfig().getCheckYellowFirst() * 1000);
        bundle.putLong("vc_dst_uid", dstUid);
        bundle.putString("vc_self_info", PSP.getInstance().getString(INFO_SAVE_KEY, ""));
        bundle.putString("vc_gift_list",ModuleMgr.getCommonMgr().getGiftLists().getStrGiftConfig());
        bundle.putInt("vc_own",PSP.getInstance().getInt(Constant.APPEAR_TYPE,1));//1看自己，2不看自己
        return bundle;
    }

    //调起视频插件
    private void startRtcInitActivity(Context context, Bundle bundle) {
        if (ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO)) {
            Intent intent = new Intent();
            intent.setClassName("com.juxin.predestinate.assist", "com.juxin.predestinate.assist.ui.RtcInitActivity");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } else {
            downloadVideoPlugin(context);
        }
    }
}
