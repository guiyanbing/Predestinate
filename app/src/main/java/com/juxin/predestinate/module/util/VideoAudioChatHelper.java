package com.juxin.predestinate.module.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


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
    private static final int TASK_WHAT_INVITE_VA_CHAT = 100;
    private static final int TASK_WHAT_VA_CONFIG = 101;
    private static final int TASK_WHAT_GET_SELF_VA_CONFIG = 102;
    private static VideoAudioChatHelper instance;
    private VaCallback vaCallback;
//    private DownLoadDialog downLoadDialog = new DownLoadDialog();
    private boolean isDownloading = false;
    private int lastOpt = 0;
    private int chatType;
    private static final String TEST_URL = "http://jast.djp123.com/starter_platform/test_update/ai_plugin_videotel3.apk";

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



    /**
     * 邀请对方音频或视频聊天
     *
     * @param context Activity
     * @param dstUid  对方UID
     * @param type    {@link TYPE_VIDEO_CHAT,TYPE_AUDIO_CHAT}   1为视频，2为音频
     */
    public void inviteVAChat(Activity context, int dstUid, int type) {
        lastOpt = OPT_INVITE;
//        if (ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO) && ApkUnit.getInstallAppVer(context, PACKAGE_PLUGIN_VIDEO) == AppCfg.ASet.getVideoPluginVer()) {
//            if(AppModel.getInstance().getUserDetail().getGender() == 1 && !AppModel.getInstance().getUserDetail().isMonthMail()){
//                createTipsVipDialog(context,"您非VIP会员，不可以直接发起视频和语音通话","去开通","取消",1);
//                return;
//            }
//            int leastDiamond = type == TYPE_AUDIO_CHAT ? AppCfg.ASet.getVoice_cost_per_minute():AppCfg.ASet.getVideo_cost_per_minute();
//            if(AppModel.getInstance().getUserDetail().getDiamondsSum() < leastDiamond){
//                T.showShort(context,"钻石余额不足");
//                UIHelper.showDiamondsDlg(context, String.valueOf(dstUid), "1", leastDiamond - AppModel.getInstance().getUserDetail().getDiamondsSum(),true,true);
//                return;
//            }
//            executeInviteChat(context, dstUid, type);
//        } else {
//            downloadVideoPlugin(context);
//        }
    }





    /**
     * 打开被邀请时的页面
     *
     * @param vcId     通话频道ID
     * @param dstUid   对方UID
     * @param chatType 1视频，2音频
     */
    public void openInvitedActivity(Activity activity, int vcId, int dstUid, int chatType) {
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
    private void downloadVideoPlugin(final Context context) {
//        downLoadDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "download");
//        if (isDownloading) {
//            return;
//        }
//        isDownloading = true;
//        HttpMgr httpMgr = new HttpMgr();
//        String apkFile = Common.getCahceDir("apk") + Long.toString(new Date().getTime()) + ".apk";//AppCfg.ASet.getVideo_chat_apk_url()
//        httpMgr.downloadAndCacheFile(AppCfg.ASet.getVideo_chat_apk_url(), apkFile, apkFile, new NetInterface.OnTransmissionListener() {
//            @Override
//            public void onProcess(String s, int i, long l) {
//                if (downLoadDialog.isAdded() && downLoadDialog.isVisible())
//                    downLoadDialog.updateProgress(i);
//            }
//
//            @Override
//            public void onStart(String s, String s1) {
//                isDownloading = true;
//            }
//
//            @Override
//            public void onStop(String s, String s1, int i) {
//                isDownloading = false;
//                downLoadDialog.dismiss();
//                ApkUnit.ExecApkFile(context, s1);
//            }
//        });
    }



    private void executeInviteChat(final Context context, final int dstUid, final int type) {
//        HashMap<String, Object> postParams = new HashMap<>();
//        postParams.put("tuid", dstUid);
//        postParams.put("vtype", type);
//        AsyncTaskUtil.getInstance(context)
//                .setUrl(AppCfg.FATE_GO_INVITE_VA_CHAT).isDes(true).setMethod(BaseAsyncTask.METHOD_POST)
//                .setPostParams(postParams).setWhat(TASK_WHAT_INVITE_VA_CHAT).setOnAsyncCallback(new OnAsyncCallback() {
//            @Override
//            public void requestSuccess(BaseAsyncTask task) {
//                handleInviteChat(context, task.getJsonResult(true), dstUid, type);
//            }
//
//            @Override
//            public void requestFail(BaseAsyncTask task, Exception e) {
//                Log.e("video", e.getMessage(), e);
//                T.showShort(context, "请求失败");
//            }
//        }).executeTask();
    }



//    @Override
//    public void requestSuccess(BaseAsyncTask task) {
//        switch (task.getWhat()) {
//            case TASK_WHAT_GET_SELF_VA_CONFIG:
//                handleGetSelfConfig(task.getJsonResult(true));
//                break;
//        }
//    }
//
//    @Override
//    public void requestFail(BaseAsyncTask task, Exception e) {
//
//    }

    private void handleInviteChat(final Context context, String jsonStr, int dstUid, int type) {
//        try {
//            //特殊错误码: 3001 用户正在视频聊天中 3002 该用户无法视频聊天 3003 钻石余额不足
//            JSONObject jo = new JSONObject(jsonStr);
//            if ("ok".equals(jo.optString("status"))) {
//                JSONObject resJo = jo.getJSONObject("res");
//
//                AppCtx.add_VC_ID(resJo.getInt("vc_id"));
//                Bundle bundle = newBundle(resJo.getInt("vc_id"), dstUid, 1, type);
//                startRtcInitActivity(context, bundle);
//            } else {
//                int code = jo.optInt("code");
//                if(code == 3003) {
//                    int leastDiamond = type == TYPE_AUDIO_CHAT ? AppCfg.ASet.getVoice_cost_per_minute() : AppCfg.ASet.getVideo_cost_per_minute();
//                    if (AppModel.getInstance().getUserDetail().getDiamondsSum() < leastDiamond) {
//                        UIHelper.showDiamondsDlg((Activity) context, String.valueOf(dstUid), "1", leastDiamond - AppModel.getInstance().getUserDetail().getDiamondsSum(), true, true);
//                    }
//                }
//                T.showShort(context, jo.optString("msg"));
//
//            }
//        } catch (JSONException e) {
//            T.showShort(context, "数据异常");
//            e.printStackTrace();
//        }

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
    private Bundle newBundle(int vcId, int dstUid, int inviteType, int chatType) {
        Bundle bundle = new Bundle();
//        bundle.putString("vc_get_user_url", GetLittleUserInfoTask.getGetLittleUserInfoTaskUrl(dstUid + ""));
//        bundle.putString("vc_cookie", "auth=" + AppCtx.getPreference("auth") + ";" + "v=" + AppCtx.VersionCode);
//        bundle.putInt("vc_chat_type", chatType);
//        bundle.putInt("vc_invite_type", inviteType);
//        bundle.putInt("vc_id", vcId);
//        bundle.putInt("vc_project", 0);
//        bundle.putString("vc_channel", JniUtil.GetEncryptString("juxin_live_" + vcId));
//        bundle.putString("vc_uid", AppModel.getInstance().getUserDetail().getUid() + "");
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
        }
    }

    private void handleVaConfig(String jsonStr, int videoChat, int audioChat) {
//        try {
//            JSONObject jo = new JSONObject(jsonStr);
//            if ("ok".equals(jo.getString("status"))) {
//                AppCtx.putPreference(AppCtx.SETTING_VIDEO_CHAT, videoChat == 0 ? false : true);
//                AppCtx.putPreference(AppCtx.SETTING_AUDIO_CHAT, audioChat == 0 ? false : true);
//                vaCallback(0, "ok");
//            } else {
//                vaCallback(1, jo.optString("msg"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void vaCallback(int code, String msg) {
        if (vaCallback != null) {
            vaCallback.onComplete(code, msg);
            vaCallback = null;
        }
    }

    private void handleGetSelfConfig(String jsonStr) {
//        try {
//            AppJsonParser.selfVideochatConfig(jsonStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
