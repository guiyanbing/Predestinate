package com.juxin.predestinate.module.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.utils.FileUtil;
import com.juxin.library.utils.JniUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.model.impl.HttpMgrImpl;
import com.juxin.predestinate.module.logic.model.mgr.HttpMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.utils.DownloadPluginFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private int singleType;
    private static boolean isGroupInvite = false;  // 用户是否处于群发状态

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

    private List<Long> getLongList() {
        try {
            String tmpStr = PSP.getInstance().getString("VCID" + App.uid, "");
            if (!TextUtils.isEmpty(tmpStr)) {
                return JSON.parseObject(tmpStr, new TypeReference<List<Long>>() {
                });
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public boolean isContain(long vcID) {
        List<Long> longList = getLongList();
        if (longList.size() <= 0) {
            return false;
        }

        for (long tmp : longList) {
            if (vcID == tmp) {
                return true;
            }
        }
        return false;
    }

    public void remove(long vcID) {
        List<Long> longList = getLongList();
        if (longList.size() > 0) {
            longList.remove(vcID);
            PSP.getInstance().put("VCID" + App.uid, JSON.toJSONString(longList));
        }
    }

    public void addvcID(long vcID) {
        List<Long> longList = getLongList();
        if (longList == null) {
            longList = new ArrayList<>();
        }

        longList.add(vcID);
        PSP.getInstance().put("VCID" + App.uid, JSON.toJSONString(longList));
    }

    /**
     * 设置是否处于群发状态
     */
    public void setGroupInviteStatus(boolean isGroupInvite) {
        this.isGroupInvite = isGroupInvite;
    }
    public boolean getGroupInviteStatus() {
        return isGroupInvite;
    }

    /**
     * 邀请对方音频或视频聊天
     *
     * @param context
     * @param dstUid
     * @param type
     * @param flag       判断是否显示进场dlg
     * @param singleType 非默认情况值, 0:还没选择,1:自己露脸，2:自己不露脸
     * @param isInvate   是否来自邀请他按钮，只有女号有。布局和出场方式 singleType 不同
     */
    public void inviteVAChat(final Activity context, long dstUid, int type, boolean flag, int singleType, String channel_uid, boolean isInvate) {
        if (flag && PSP.getInstance().getInt(ModuleMgr.getCommonMgr().getPrivateKey(Constant.APPEAR_FOREVER_TYPE), 0) == 0) {
            UIShow.showLookAtHerDlg(context, dstUid, channel_uid, isInvate);
            return;
        }
        this.singleType = singleType;
        inviteVAChat(context, dstUid, type, channel_uid);
    }

    /**
     * 邀请对方音频或视频聊天
     *
     * @param inviteId   邀请id,即为邀请流水号，接受邀请并发起视频的时候使用
     */
    public void acceptInviteVAChat(long inviteId) {
        LoadingDialog.show((FragmentActivity) App.activity, "加入中...");
        ModuleMgr.getCommonMgr().reqAcceptVideoChat(inviteId, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    PSP.getInstance().put("ISINVITE", true);
                    MsgMgr.getInstance().delay(new Runnable() {
                        @Override
                        public void run() {
                            PSP.getInstance().put("ISINVITE", false);
                            LoadingDialog.closeLoadingDialog();
                        }
                    },20000);
                } else {
                    LoadingDialog.closeLoadingDialog();
                    PToast.showShort(TextUtils.isEmpty(response.getMsg()) ? App.getContext().getString(R.string.chat_join_fail_tips) : response.getMsg());
                }
            }
        });
    }

    /**
     * 邀请对方音频或视频聊天
     *
     * @param context Activity
     * @param dstUid  对方UID
     * @param type    1为视频{@link #TYPE_VIDEO_CHAT TYPE_VIDEO_CHAT}，2为音频{@link #TYPE_AUDIO_CHAT TYPE_AUDIO_CHAT}
     */
    public void inviteVAChat(final Activity context, long dstUid, int type, String channel_uid) {
        if (!ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO) || ApkUnit.getInstallAppVer(context, PACKAGE_PLUGIN_VIDEO) < ModuleMgr.getCommonMgr().getCommonConfig().getPlugin_version()) {
            downloadVideoPlugin(context);
            return;
        }

        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        if (userDetail.isMan()) {
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
        executeInviteChat(context, dstUid, type, channel_uid);
    }

    public void girlSingleInvite(final Activity activity, final long dstUid, final int type) {
        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("tuid", dstUid);
        postParams.put("vtype", type);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.girlSingleInviteVa, postParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                handleSingleInvite(activity, response, dstUid, type);
            }
        });
    }

    private void handleSingleInvite(final Context context, HttpResponse response, final long dstUid, final int type) {
        JSONObject jo = response.getResponseJson();
        if (response.isOk()) {
            JSONObject resJo = jo.optJSONObject("res");
            final long vcID = resJo.optLong("vc_id");
            int msgVer = resJo.optInt("confer_msgver");
            Bundle bundle = newBundle(vcID, dstUid, 1, type, msgVer);
            bundle.putInt("vc_girl_type", 1);
            startGroupInviteAct(context, bundle);
            return;
        }

        int code = jo.optInt("code");
        if (code == 3003)
            UIShow.showGoodsDiamondDialogAndTag(context, Constant.OPEN_FROM_CHAT_PLUGIN, dstUid, "");
        PToast.showShort(TextUtils.isEmpty(jo.optString("msg")) ? "数据异常" : jo.optString("msg"));
    }

    public void girlGroupInvite(final Activity activity, final int type) {
        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("vtype", type);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.girlGroupInviteVa, postParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                handleGroupInvite(response, activity, type);
            }
        });
    }

    private void handleGroupInvite(HttpResponse response, Activity activity, int type) {
        int price = ModuleMgr.getCenterMgr().getMyInfo().getChatInfo().getAudioPrice();
        if (type == TYPE_VIDEO_CHAT) {
            price = ModuleMgr.getCenterMgr().getMyInfo().getChatInfo().getVideoPrice();
        }

        JSONObject jo = response.getResponseJson();
        if (response.isOk()) {
            JSONObject resJo = jo.optJSONObject("res");
            long inviteId = resJo.optLong("invite_id");
            Bundle bundle = newBundle(0, 0, 1, type, 20);  // 此时无vcId返回
            bundle.putInt("vc_girl_type", 2);
            bundle.putLong("vc_girl_price", price);
            bundle.putLong("vc_girl_invite_id", inviteId);
            startGroupInviteAct(activity, bundle);
            return;
        }
        PToast.showShort(TextUtils.isEmpty(jo.optString("msg")) ? "数据异常" : jo.optString("msg"));
    }

    /**
     * 普通： 打开被邀请时的页面
     *
     * @param vcId     通话频道ID
     * @param dstUid   对方UID
     * @param chatType 1视频，2音频
     */
    public void openInvitedActivity(Activity activity, long vcId, long dstUid, int chatType, long price) {
        Bundle bundle = newBundle(vcId, dstUid, 2, chatType, 20);
        bundle.putLong("vc_girl_price", price);
        startRtcInitActivity(activity, bundle);
    }

    /**
     * 直接开启聊天页
     *
     * @param vcId     通话频道ID
     * @param dstUid   对方UID
     * @param chatType 1视频，2音频
     */
    public void openInvitedDirect(Activity activity, long vcId, long dstUid, int chatType,String vc_channel_key) {
        Bundle bundle = newBundle(vcId, dstUid, 2, chatType, 20);
        bundle.putInt("vc_chat_from", 2);
        bundle.putString("vc_channel_key", vc_channel_key);
        startRtcInitActivity(activity, bundle);
    }


    /**
     * 检测是否需要下载视频插件
     */
    public void checkDownloadPlugin(FragmentActivity activity) {
        VideoVerifyBean verifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
        if ((verifyBean.getBooleanAudiochat() || verifyBean.getBooleanVideochat())
                && !ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO)) {
            downloadVideoPlugin(activity);
            return;
        }

        // 女号，检测版本升级
        if (!ModuleMgr.getCenterMgr().getMyInfo().isMan()
                && ApkUnit.getInstallAppVer(context, PACKAGE_PLUGIN_VIDEO) < ModuleMgr.getCommonMgr().getCommonConfig().getPlugin_version()) {
            downloadVideoPlugin(activity);
        }
    }

    /**
     * 下载视频插件
     *
     * @param context 上下文
     */
    public void downloadVideoPlugin(final Context context) {
        if (!downloadPluginFragment.isAdded())
            downloadPluginFragment.show((FragmentActivity) context);
        if (isDownloading) return;

        isDownloading = true;
        HttpMgr httpMgr = new HttpMgrImpl();
        String downUrl = TextUtils.isEmpty(ModuleMgr.getCommonMgr().getCommonConfig().getVideo_chat_apk_url()) ? TEST_URL : ModuleMgr.getCommonMgr().getCommonConfig().getVideo_chat_apk_url();
        String apkFile = DirType.getApkDir() + FileUtil.getFileNameFromUrl(downUrl);//AppCfg.ASet.getVideo_chat_apk_url()
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
                downloadPluginFragment.dismissAllowingStateLoss();
                ApkUnit.ExecApkFile(context, filePath);
            }

            @Override
            public void onFail(String url, Throwable throwable) {
                isDownloading = false;
                downloadPluginFragment.dismissAllowingStateLoss();
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
    private void executeInviteChat(final Context context, final long dstUid, final int type, final String channel_uid) {
        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("tuid", dstUid);
        postParams.put("vtype", type);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.inviteVideoChat, postParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                handleInviteChat(context, response, dstUid, type, channel_uid);
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
    public void executeGInviteChat(final Context context, final long dstUid, final int type, final String channel_uid) {
        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("tuid", dstUid);
        postParams.put("vtype", type);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.inviteVideoChat, postParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                //特殊错误码: 3001 用户正在视频聊天中 3002 该用户无法视频聊天 3003 钻石余额不足
                JSONObject jo = response.getResponseJson();
                if (response.isOk()) {
                    JSONObject resJo = jo.optJSONObject("res");
                    final long vcID = resJo.optLong("vc_id");
                    addvcID(vcID);
                    int msgVer = resJo.optInt("confer_msgver");
                    MsgMgr.getInstance().delay(new Runnable() {
                        @Override
                        public void run() {
                            ModuleMgr.getChatMgr().sendVideoMsgLocalSimulation(String.valueOf(dstUid), type, vcID);
                        }
                    }, 500);
                    return;
                }

                int code = jo.optInt("code");
                if (code == 3003)
                    UIShow.showGoodsDiamondDialogAndTag(context, Constant.OPEN_FROM_CHAT_PLUGIN, dstUid, channel_uid);
                PToast.showShort(TextUtils.isEmpty(jo.optString("msg")) ? "数据异常" : jo.optString("msg"));
            }
        });
    }

    private void handleInviteChat(final Context context, HttpResponse response, final long dstUid, final int type, String channel_uid) {
        //特殊错误码: 3001 用户正在视频聊天中 3002 该用户无法视频聊天 3003 钻石余额不足
        JSONObject jo = response.getResponseJson();
        if (response.isOk()) {
            JSONObject resJo = jo.optJSONObject("res");
            final long vcID = resJo.optLong("vc_id");
            addvcID(vcID);
            int msgVer = resJo.optInt("confer_msgver");
            MsgMgr.getInstance().delay(new Runnable() {
                @Override
                public void run() {
                    ModuleMgr.getChatMgr().sendVideoMsgLocalSimulation(String.valueOf(dstUid), type, vcID);
                }
            }, 500);

            Bundle bundle = newBundle(vcID, dstUid, 1, type, msgVer);
            startRtcInitActivity(context, bundle);
            return;
        }

        int code = jo.optInt("code");
        if (code == 3003)
            UIShow.showGoodsDiamondDialogAndTag(context, Constant.OPEN_FROM_CHAT_PLUGIN, dstUid, channel_uid);
        PToast.showShort(TextUtils.isEmpty(jo.optString("msg")) ? "数据异常" : jo.optString("msg"));
    }

    /**
     * 创建bundle传输参数
     *
     * @param vcId       视频通道
     * @param dstUid     对方UID
     * @param inviteType 1邀请，2受邀
     * @param chatType   1视频，2音频
     * @param msgVer     程序版本号
     * @return
     */
    private Bundle newBundle(long vcId, long dstUid, int inviteType, int chatType, int msgVer) {
        int foreverType = PSP.getInstance().getInt(ModuleMgr.getCommonMgr().getPrivateKey(Constant.APPEAR_FOREVER_TYPE), 0);
        Bundle bundle = new Bundle();
        bundle.putString("vc_get_user_url", UrlParam.reqUserInfoSummary.getFinalUrl());
        bundle.putString("vc_cookie", ModuleMgr.getLoginMgr().getCookieVerCode());
        bundle.putInt("vc_chat_type", chatType);
        bundle.putInt("vc_invite_type", inviteType);
        bundle.putLong("vc_id", vcId);
        bundle.putInt("vc_project", 1);
        bundle.putString("vc_channel", JniUtil.GetEncryptString("juxin_live_" + vcId));
        bundle.putString("vc_uid", ModuleMgr.getCenterMgr().getMyInfo().getUid() + "");
        bundle.putLong("vc_check_yellow", ModuleMgr.getCommonMgr().getCommonConfig().getCheckYellow() * 1000);
        bundle.putLong("vc_check_yellow_first", ModuleMgr.getCommonMgr().getCommonConfig().getCheckYellowFirst() * 1000);
        bundle.putLong("vc_dst_uid", dstUid);
        bundle.putString("vc_self_info", PSP.getInstance().getString(INFO_SAVE_KEY, ""));
        bundle.putString("vc_gift_list", ModuleMgr.getCommonMgr().getGiftLists().getStrGiftConfig());
        bundle.putInt("vc_own", singleType == 0 ? (foreverType == 0 ? 2 : foreverType) : singleType);//1看自己，2不看自己
        bundle.putInt("vc_msg_ver", msgVer);
        return bundle;
    }

    /**
     * 调起视频插件： 普通
     */
    private void startRtcInitActivity(Context context, Bundle bundle) {
        if (ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO)) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.juxin.predestinate.assist", "com.juxin.predestinate.assist.ui.RtcInitActivity");
                intent.putExtras(bundle);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            downloadVideoPlugin(context);
        }
    }

    /**
     * 调起视频插件： 单聊, 群发
     */
    private void startGroupInviteAct(Context context, Bundle bundle) {
        if (ApkUnit.getAppIsInstall(context, PACKAGE_PLUGIN_VIDEO)) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.juxin.predestinate.assist", "com.juxin.predestinate.assist.ui.RtcGroupInitAct");
                intent.putExtras(bundle);
                context.startActivity(intent);
                setGroupInviteStatus(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            downloadVideoPlugin(context);
        }
    }
}
