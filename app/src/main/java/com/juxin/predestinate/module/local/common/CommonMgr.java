package com.juxin.predestinate.module.local.common;

import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.utils.EncryptUtil;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.config.CommonConfig;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.bean.my.IdCardVerifyStatusInfo;
import com.juxin.predestinate.bean.settting.ContactBean;
import com.juxin.predestinate.module.local.location.LocationMgr;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.request.RequestParam;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.my.AttentionUtil;
import com.juxin.predestinate.module.util.my.GiftHelper;
import com.juxin.predestinate.ui.discover.SayHelloDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用，新版本
 * Created by Xy on 17/3/22.
 */
public class CommonMgr implements ModuleBase {

    private CommonConfig commonConfig;//服务器静态配置
    private GiftsList giftLists;//礼物信息
    private VideoVerifyBean videoVerify;//视频聊天配置
    private IdCardVerifyStatusInfo mIdCardVerifyStatusInfo;
    private ContactBean contactBean;//客服信息

    @Override
    public void init() {
    }

    @Override
    public void release() {
    }

    public ContactBean getContactBean() {
        return contactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        this.contactBean = contactBean;
    }

    /**
     * 游戏交互-请求转发。接口回调data为String类型。[注意]：加密请求添加了新的header，若服务器无法处理，谨慎调用该方法
     *
     * @param requestType   请求方式（GET/POST）
     * @param requestUrl    请求完整地址，不会进行再次拼接
     * @param isEnc         是否为加密请求
     * @param requestObject 请求体参数
     * @param complete      请求回调
     */
    public void CMDRequest(String requestType, boolean isEnc, String requestUrl, Map<String, Object> requestObject, RequestComplete complete) {
        RequestParam requestParam = new RequestParam();
        if ("POST".equalsIgnoreCase(requestType)) {
            requestParam.setRequestType(RequestParam.RequestType.POST);
            if (requestObject != null) requestParam.setPost_param(requestObject);
        } else if ("GET".equalsIgnoreCase(requestType)) {
            requestParam.setRequestType(RequestParam.RequestType.GET);
            if (requestObject != null) requestParam.setGet_param(requestObject);
        }
        requestParam.setNeedEncrypt(isEnc);
        if (isEnc) requestParam.setHead_param(getEncHeaderMap());

        UrlParam urlParam = UrlParam.CMDRequest;
        urlParam.resetHost(requestUrl);
        requestParam.setUrlParam(urlParam);

        requestParam.setRequestCallback(complete);
        ModuleMgr.getHttpMgr().request(requestParam);
    }

    /**
     * @return 获取游戏调用时自定义的请求头：safe-request
     */
    public Map<String, String> getEncHeaderMap() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("Accept", "application/jx-json");
        headerParams.put("Content-Type", "application/jx-json");
        return headerParams;
    }

    /**
     * 请求一些在线配置信息
     */
    public void requestStaticConfig() {
        requestConfig();
        requestGiftList(null);
        getVerifyStatus(null);
        AttentionUtil.initUserDetails();
    }

    /**
     * 请求服务器在线配置
     */
    private void requestConfig() {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("ver", Constant.SUB_VERSION);//静态配置内容的版本本号(整数)
        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.staticConfig, requestParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PLogger.d("---StaticConfig--->isCache：" + response.isCache() + "，" + response.getResponseString());
                commonConfig = new CommonConfig();
                commonConfig.parseJson(response.getResponseString());
            }
        });
    }

    /**
     * @return 获取服务器静态配置对象
     */
    public CommonConfig getCommonConfig() {
        return commonConfig == null ? new CommonConfig() : commonConfig;
    }

    /**
     * 获取离线消息
     */
    public void reqOfflineMsg(RequestComplete complete) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("uid", App.uid);
        getParams.put("count", 50);

        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.reqOfflineMsg, getParams, complete);
    }

    /**
     * 获取自己的音频、视频开关配置
     */
    public void requestVideochatConfig() {
        ModuleMgr.getHttpMgr().reqGet(UrlParam.reqMyVideochatConfig, null, null, RequestParam.CacheType.CT_Cache_No, true, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk())
                    videoVerify = (VideoVerifyBean) response.getBaseData();
            }
        });
    }

    /**
     * 获取自己的音频、视频开关配置
     */
    public void requestVideochatConfigSendUI(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqGet(UrlParam.reqMyVideochatConfig, null, null, RequestParam.CacheType.CT_Cache_No, true, complete);
    }

    /**
     * 修改自己的音频、视频开关配置
     */
    public void setVideochatConfig(boolean videoStatus, boolean audioStatus) {
        HashMap<String, Object> post_param = new HashMap<>();
        post_param.put("videochat", videoStatus ? 1 : 0);
        post_param.put("audiochat", audioStatus ? 1 : 0);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.setVideochatConfig, post_param, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    requestVideochatConfig();
                    return;
                }
                PToast.showShort(response.getMsg());
            }
        });
    }

    /**
     * 上传视频认证配置
     */
    public void addVideoVerify(String imgUrl, String videoUrl, RequestComplete complete) {
        HashMap<String, Object> post_param = new HashMap<>();
        post_param.put("imgurl", imgUrl);
        post_param.put("videourl", videoUrl);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.addVideoVerify, post_param, complete);
    }


    /**
     * 获取客服信息
     */
    public void getCustomerserviceContact(final FragmentActivity context, final RequestComplete complete) {
        LoadingDialog.show(context);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.getserviceqq, null, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    contactBean = (ContactBean) response.getBaseData();
                }
                LoadingDialog.closeLoadingDialog(200, new TimerUtil.CallBack() {
                    @Override
                    public void call() {
                        complete.onRequestComplete(null);
                    }
                });

            }
        });

    }

    /**
     * 请求礼物列表
     */
    public void requestGiftList(final GiftHelper.OnRequestGiftListCallback callback) {
        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.getGiftLists, null, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PLogger.d("---GiftList--->isCache：" + response.isCache() + "，" + response.getResponseString());
                if (response.isOk() || response.isCache()) {
                    giftLists = new GiftsList();
                    giftLists.parseJson(response.getResponseString());
                    if (callback != null) {
                        callback.onRequestGiftListCallback(response.isOk());
                    }
                }
            }
        });
    }

    /**
     * @return 获取礼品信息
     */
    public GiftsList getGiftLists() {
        if (giftLists == null) {
            giftLists = new GiftsList();
        }
        return giftLists;
    }

    /**
     * @return 返回认证消息对象
     */
    public IdCardVerifyStatusInfo getIdCardVerifyStatusInfo() {
        if (mIdCardVerifyStatusInfo == null)
            mIdCardVerifyStatusInfo = new IdCardVerifyStatusInfo();
        return mIdCardVerifyStatusInfo;
    }

    public VideoVerifyBean getVideoVerify() {
        return videoVerify != null ? videoVerify : new VideoVerifyBean();
    }

    public void setVideoVerify(VideoVerifyBean videoVerify) {
        this.videoVerify = videoVerify;
    }

    /**
     * 判断是否到达第二天,并存储
     *
     * @return true 达到第二天 并存储
     */
    public boolean checkDateAndSave(String key) {
        String recommendDate = PSP.getInstance().getString(key, "-1");
        if (recommendDate != null) {
            if (!recommendDate.equals(TimeUtil.getCurrentData())) {
                PSP.getInstance().put(key, TimeUtil.getCurrentData() + "");
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否到达第二天
     *
     * @return true 达到第二天
     */
    public boolean checkDate(String key) {
        String recommendDate = PSP.getInstance().getString(key, "-1");
        if (key.equals(ModuleMgr.getCenterMgr().getGroupSayHiDayKey())) {
            PLogger.d("checkDate ==+>  recommendDate = " + recommendDate + " TimeUtil.getCurrentData() == " + TimeUtil.getCurrentData());
        }
        if (recommendDate != null) {
            if (!recommendDate.equals(TimeUtil.getCurrentData())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存达到第二天的状态
     *
     * @param key
     */
    public void saveDateState(String key) {
        PSP.getInstance().put(key, TimeUtil.getCurrentData() + "");
    }

    // ---------------------------- 软件升级 start ------------------------------

    /* 软件升级账号密码迁移临时文件路径 */
    private static final String UPDATE_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "xiaou" + File.separator;

    /**
     * 检查应用升级
     *
     * @param activity  FragmentActivity实例
     * @param isShowTip 是否展示界面提示
     */
    public void checkUpdate(final FragmentActivity activity, final boolean isShowTip) {
        if (isShowTip) LoadingDialog.show(activity, "检测中请等待...");
        HashMap<String, Object> getParams = new HashMap<>();
        getParams.put("c_uid", ModuleMgr.getAppMgr().getMainChannelID());// 渠道ID
        getParams.put("c_sid", ModuleMgr.getAppMgr().getSubChannelID());// 子渠道
        getParams.put("platform", "android");
        getParams.put("v", ModuleMgr.getAppMgr().getVerCode());
        getParams.put("app_key", EncryptUtil.sha1(ModuleMgr.getAppMgr().getSignature()));
        getParams.put("package_name", ModuleMgr.getAppMgr().getPackageName());
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.checkUpdate, getParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (isShowTip)
                    LoadingDialog.closeLoadingDialog(300);
                AppUpdate appUpdate = new AppUpdate();
                appUpdate.parseJson(response.getResponseString());
                UIShow.showUpdateDialog(activity, appUpdate, isShowTip);
            }
        });
    }

    /**
     * 将用户信息写入文件，跨软件升级使用
     *
     * @param packageName 需要升级到的包名
     * @param versionCode 需要升级到的软件版本号
     */
    public void updateSaveUP(String packageName, int versionCode) {
        Map<String, Object> upMap = new HashMap<>();
        upMap.put("packageName", packageName);
        upMap.put("versionCode", versionCode);
        upMap.put("user", PSP.getInstance().getString(FinalKey.LOGIN_USER_KEY, null));

        // 如果文件夹创建失败，直接跳出
        if (DirType.isFolderExists(UPDATE_CACHE_PATH)) return;

        // 文件写入
        File file = new File(UPDATE_CACHE_PATH + "user_cache");
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file));//根据文件创建文件的输出流
            out.write(new Gson().toJson(upMap));//向文件写入内容
            out.flush();
        } catch (Exception e) {
            PLogger.printThrowable(e);
        } finally {
            try {
                if (out != null) out.close();// 关闭输出流
            } catch (Exception e) {
                PLogger.printThrowable(e);
            }
        }
    }

    /**
     * 获取已登录过的所有用户ID和密码
     */
    public void updateUsers() {
        String key = FileUtil.fileRead(UPDATE_CACHE_PATH + "user_cache");
        if (!TextUtils.isEmpty(key)) {
            JSONObject cacheObject = JsonUtil.getJsonObject(key);
            String packageName = cacheObject.optString("packageName");
            int versionCode = cacheObject.optInt("versionCode");
            // 如果当前包名或版本号不等于目标包名或版本号，才进行处理
            if (!ModuleMgr.getAppMgr().getPackageName().equalsIgnoreCase(packageName) ||
                    ModuleMgr.getAppMgr().getVerCode() != versionCode) return;

            // 读取旧版本用户数据
            JSONArray jsonArray = cacheObject.optJSONArray("user");
            for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
                JSONObject upObject = JsonUtil.getJsonObject(jsonArray.optString(i));
                try {
                    ModuleMgr.getLoginMgr().addLoginUser(
                            Long.parseLong(EncryptUtil.encryptDES(upObject.optString("sUid"), FinalKey.UP_DES_KEY)),
                            EncryptUtil.encryptDES(upObject.optString("sPw"), FinalKey.UP_DES_KEY));
                } catch (Exception e) {
                    PLogger.printThrowable(e);
                }
            }
            FileUtil.deleteFile(UPDATE_CACHE_PATH + "user_cache");
        }
    }
    // ---------------------------- 软件升级 end ------------------------------

    /**
     * 获取每日推荐列表（一键打招呼列表）
     *
     * @param complete
     */
    public void getSayHiList(RequestComplete complete) {
        String ts = TimeUtil.getCurrentTimeMil();
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("ts", ts);
        postParams.put("simoperator", TextUtils.isEmpty(ModuleMgr.getAppMgr().getSimOperator()) ? "" : ModuleMgr.getAppMgr().getSimOperator());
        postParams.put("imsi", TextUtils.isEmpty(ModuleMgr.getAppMgr().getIMSI()) ? "" : ModuleMgr.getAppMgr().getIMSI());
        postParams.put("imei", TextUtils.isEmpty(ModuleMgr.getAppMgr().getIMEI()) ? "" : ModuleMgr.getAppMgr().getIMEI());

        postParams.put("ver", ModuleMgr.getAppMgr().getVerCode());
        postParams.put("c_uid", "");
        postParams.put("c_sid", "");

        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqSayHiList, postParams, complete);
    }

    /**
     * 一键打招呼的状态Key
     *
     * @return
     */
    public String getSayHelloKey() {
        return "Say_Hello_" + ModuleMgr.getCenterMgr().getMyInfo().getUid();
    }

    private SayHelloDialog sayHelloDialog = new SayHelloDialog();

    /**
     * 显示一键打招呼对话框
     *
     * @param context
     */
    public void showSayHelloDialog(final FragmentActivity context) {
        if (sayHelloDialog.isShowing()) {
            return;
        }
        PLogger.d("showSayHelloDialog === isVip = " + ModuleMgr.getCenterMgr().getMyInfo().isVip());
        if (checkDate(getSayHelloKey()) && ModuleMgr.getCenterMgr().getMyInfo().isMan() && !ModuleMgr.getCenterMgr().getMyInfo().isVip()) {
            getSayHiList(new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    PLogger.d("showSayHelloDialog ---- res = " + response.getResponseString());
                    if (response.isOk()) {
                        UserInfoLightweightList list = new UserInfoLightweightList();
                        list.parseJsonSayhi(response.getResponseString());
                        sayHelloDialog.showDialog(context);
                        sayHelloDialog.setData(list.getLightweightLists());
                    }
                }
            });
        }
    }


    //============================== 小友模块相关接口 =============================

    /**
     * 获取最近礼物列表
     *
     * @param complete 请求完成后回调
     */
    public void lastGiftList(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.lastGiftList, null, complete);
    }

    /**
     * 上传身份证照片
     *
     * @param url      图片本地地址
     * @param complete 上传完成回调
     */
    public void uploadIdCard(final String url, final RequestComplete complete) {
        if (FileUtil.isExist(url)) {
            Map<String, File> fileParams = new HashMap<>();
            fileParams.put("userfile", new File(url));

            long uid = ModuleMgr.getLoginMgr().getUserList().get(0).getUid();
            String password = ModuleMgr.getLoginMgr().getUserList().get(0).getPw().trim();

            Map<String, Object> postParams = new HashMap<>();
            postParams.put("uid", uid);
            postParams.put("code", EncryptUtil.md5(uid + EncryptUtil.md5(password)));

            ModuleMgr.getHttpMgr().uploadFile(UrlParam.uploadIdCard, null, fileParams, new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (complete != null)
                        complete.onRequestComplete(response);
                }
            });

        } else {
            LoadingDialog.closeLoadingDialog();
            PToast.showShort("图片地址无效");
        }
    }

    /**
     * 我关注的列表
     *
     * @param complete 请求完成后回调
     */
    public void getFollowing(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.getFollowing, null, complete);
    }

    /**
     * 关注我的列表
     *
     * @param complete 请求完成后回调
     */
    public void getFollowers(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.getFollowers, null, complete);
    }

    /**
     * 手机验证
     *
     * @param phoneNum 手机号
     * @param complete 请求完成后回调
     */
    public void bindCellPhone(String code, String phoneNum, RequestComplete complete) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("cellPhone", phoneNum);
        getParams.put("verifyCode", code);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.bindCellPhone, getParams, complete);
    }

    /**
     * 用户身份认证提交
     *
     * @param id_num       身份证号码
     * @param accountname  真实姓名
     * @param accountnum   银行账号/支付宝账户
     * @param bank         银行
     * @param subbank      支行
     * @param id_front_img 身份证正面照URL
     * @param id_back_img  身份证背面照URL
     * @param face_img     手持身份证照URL
     * @param paytype      支付类型 1银行 2支付宝 (默认支付宝)
     * @param complete     请求完成后回调
     */
    public void userVerify(String id_num, String accountname, String accountnum, String bank, String subbank, String id_front_img,
                           String id_back_img, String face_img, int paytype, RequestComplete complete) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id_num", id_num);
        getParams.put("accountname", accountname);
        getParams.put("accountnum", accountnum);
        getParams.put("bank", bank);
        getParams.put("subbank", subbank);
        getParams.put("id_front_img", id_front_img);
        getParams.put("id_back_img", id_back_img);
        getParams.put("face_img", face_img);
        getParams.put("paytype", paytype);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.userVerify, getParams, complete);
    }

    /**
     * 获取用户验证信息(密)
     *
     * @param complete 请求完成后回调
     */
    public void getVerifyStatus(final RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.getVerifyStatus, null, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    mIdCardVerifyStatusInfo = new IdCardVerifyStatusInfo();
                    mIdCardVerifyStatusInfo.parseJson(response.getResponseString());
                }
                if (complete != null)
                    complete.onRequestComplete(response);
            }
        });
    }

    /**
     * 手机验证
     *
     * @param phoneNum 手机号
     * @param complete 请求完成后回调
     */
    public void sendSMS(String phoneNum, RequestComplete complete) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("cellPhone", phoneNum);
        getParams.put("type", "1");
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.sendSMS, getParams, complete);
    }

    /**
     * 接收聊天红包
     *
     * @param red_log_id 聊天红包流水号
     */
    public void receiveChatRedBag(int red_log_id) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("rid", red_log_id);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.reqReceiveChatBag, getParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PToast.showShort(TextUtils.isEmpty(response.getMsg()) ?
                        App.getContext().getString(R.string.received_error) : response.getMsg());
            }
        });
    }

    /**
     * 索要礼物
     *
     * @param content  内容
     * @param complete 请求完成后回调
     */
    public void begGift(String content, RequestComplete complete) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("content", content);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.begGift, getParams, complete);
    }

    /**
     * 接收礼物
     *
     * @param rid      礼物红包ID
     * @param gname    礼物名称
     * @param gid      礼物ID
     * @param complete 请求完成后回调
     */
    public void receiveGift(long rid, String gname, int gid, RequestComplete complete) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("rid", rid);
        getParams.put("gname", gname);
        getParams.put("gid", gid);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.receiveGift, getParams, complete);
    }

    /**
     * 送礼物
     *
     * @param touid    赠送对象UId
     * @param giftid   礼物Id
     * @param giftnum  礼物数量（不填为1）
     * @param gtype    礼物来源类型 1 聊天列表 2 旧版索要 3 新版索要 4私密视频 （不填为1）
     *                 //     * @param begid     索要Id
     * @param complete 请求完成后回调
     */
    public void sendGift(String touid, String giftid, int giftnum, int gtype/*,int begid*/, RequestComplete complete) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("touid", touid);
        getParams.put("giftid", giftid);
        getParams.put("giftnum", giftnum);
        getParams.put("gtype", gtype);
        //        getParams.put("begid", begid);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.sendGift, getParams, complete);
    }

    /**
     * 客户端获得用户红包列表
     *
     * @param complete 请求完成后回调
     */
    public void getRedbagList(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqRedbagList, null, complete);
    }

    /**
     * 客户端用户红包入袋
     *
     * @param uid      用户Id
     * @param money    红包金额(分)
     * @param redbagid 红包ID
     * @param type     红包类型 1,2或为空为水果红包和水果排行红包 3为聊天红包 4聊天排名红包 5礼物红包
     * @param complete 请求完成后回调
     */
    public void reqAddredTotal(long uid, double money, long redbagid, int type, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uid", uid);
        postParams.put("money", money);
        postParams.put("redbagid", redbagid);
        postParams.put("type", type);
        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqAddredTotal, postParams, complete);
    }

    /**
     * 红包记录--红包入袋 -- 一键入袋(24不能提现)
     *
     * @param uid      用户Id
     * @param complete 请求完成后回调
     */
    public void reqAddredonekey(long uid, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uid", uid);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqAddredonekey, postParams, complete);
    }

    /**
     * 客户端请求用户提现列表
     *
     * @param complete 请求完成后回调
     */
    public void reqWithdrawlist(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqWithdrawlist, null, complete);
    }

    /**
     * 红包记录--提现申请
     *
     * @param money       提现金额(分)
     * @param paytype     支付方式(1或为空 银行卡, 2 支付宝)
     * @param accountname 帐户姓名
     * @param accountnum  银行卡号/支付宝账号
     * @param bank        开户行/支付类型
     * @param subbank     开户支行
     * @param complete    请求完成后回调
     */
    public void reqWithdraw(String money, int paytype, String accountname, String accountnum, String bank, String subbank, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("money", (Float.parseFloat(money) * 100) + "");
        postParams.put("paytype", paytype);
        postParams.put("accountname", accountname);
        postParams.put("accountnum", accountnum);
        postParams.put("bank", bank);
        postParams.put("subbank", subbank);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqWithdraw, postParams, complete);
    }

    /**
     * 红包记录--提现申请获取地址
     *
     * @param complete 请求完成后回调
     */
    public void reqWithdrawAddress(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqWithdrawAddress, null, complete);
    }

    /**
     * 红包记录--提现申请修改地址
     *
     * @param money       提现金额(分)
     * @param paytype     支付方式(1或为空 银行卡, 2 支付宝)
     * @param accountname 帐户姓名
     * @param accountnum  银行卡号/支付宝账号
     * @param bank        开户行/支付类型
     * @param subbank     开户支行
     * @param complete    请求完成后回调
     */
    public void reqWithdrawModify(String money, int paytype, String accountname, String accountnum, String bank, String subbank, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("money", (Float.parseFloat(money) * 100) + "");
        postParams.put("paytype", paytype);
        postParams.put("accountname", accountname);
        postParams.put("accountnum", accountnum);
        postParams.put("bank", bank);
        postParams.put("subbank", subbank);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqWithdrawModify, postParams, complete);
    }


    /**
     * 批量获取用户简略信息
     *
     * @param complete
     */
    public void getUserSimpleList(ArrayList<String> userLists, RequestComplete complete) {
        String[] uidlist = userLists.toArray(new String[userLists.size()]);
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uidlist", uidlist);// uids
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqUserSimpleList, postParams, complete);
    }

    /**
     * 支付商品价格List
     *
     * @param payType 充值类型：1—钻石， 2-vip
     */
    public void reqCommodityList(int payType, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("platform", 1); // 平台： 1 android 2 ios 3 公众号 4 web
        postParms.put("ctype", payType);
        ModuleMgr.getHttpMgr().reqPostAndCacheHttp(UrlParam.reqCommodityList, postParms, complete);
    }


    /**
     * 生成订单
     *
     * @param orderID
     * @param complete
     */
    public void reqGenerateOrders(int orderID, RequestComplete complete) {
        HashMap<String, Object> getParms = new HashMap<>();
        getParms.put("pid", orderID);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.reqCommodityList, getParms, complete);
    }

    /**
     * 微信支付方式
     *
     * @param complete
     */
    public void reqWXMethod(String name, int payID, int payMoney, int payCType, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("subject", name);
        postParms.put("body", name);
        postParms.put("productid", payID);
        postParms.put("total_fee", payMoney);

        //  postParms.put("total_fee", "0.01");// 钱
        if (payCType > -1) {
            postParms.put("payCType", payCType);
        }
        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqWX, postParms, complete);
    }

    /**
     * 银联或支付宝
     *
     * @param urlParam
     * @param out_trade_no
     * @param name
     * @param payID
     * @param payMoney
     * @param complete
     */
    public void reqCUPOrAlipayMethod(UrlParam urlParam, String out_trade_no, String name, int payID, int payMoney, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("out_trade_no", out_trade_no);// 订单号
        postParms.put("subject", name);// 标题
        postParms.put("body", "android-" + name);
        postParms.put("productid", payID);
        postParms.put("total_fee", payMoney);
        postParms.put("payCType", 1000);
        postParms.put("user_client_type", 2);
        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(urlParam, postParms, complete);
    }

    /**
     * 手机充值卡
     *
     * @param payID
     * @param orderNo
     * @param payMoney
     * @param cardMoney
     * @param cardType
     * @param sn
     * @param password
     * @param complete
     */
    public void reqPhoneCardMethod(int payID, String orderNo, int payMoney, int cardMoney,
                                   int cardType, String sn, String password, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("productid", payID);
        postParms.put("orderNo", orderNo);// 订单ID
        postParms.put("payMoney", payMoney);// 支付金额
        postParms.put("cardMoney", cardMoney);// 卡面额
        postParms.put("cardType", cardType);// 运营商类1是联通，2是电信，0移动
        postParms.put("sn", sn);// 充值卡号
        postParms.put("password", password);// 充值卡密码

        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqPhoneCard, postParms, complete);
    }

    public void reqSearchPhoneCardMethod(String orderNo, RequestComplete complete) {
        HashMap<String, Object> getParams = new HashMap<>();
        getParams.put("orderNo", orderNo);
        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqSearchPhoneCard, getParams, complete);
    }

    public void reqangelPayF(RequestComplete complete) {
        HashMap<String, Object> getParams = new HashMap<>();
        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqangelPayF, getParams, complete);
    }

    public void reqAngelPay(String name, int payID, int total_fee, String pan, String mobile, String idcard, String nickname, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("subject", name);//标题
        postParms.put("body", name);//
        postParms.put("productid", payID);// 订单ID
        postParms.put("total_fee", total_fee);// 钱
        postParms.put("pan", pan);//卡号
        postParms.put("mobile", mobile);//电话
        postParms.put("idcard", idcard);// 身份证
        postParms.put("realname", nickname);//名称

        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqangelPay, postParms, complete);
    }

    public void reqAngelPayB(String name, int payID, int total_fee, String mobile, String nickname, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("subject", name);//标题
        postParms.put("body", name);//
        postParms.put("productid", payID);// 订单ID
        postParms.put("total_fee", total_fee);// 钱
        postParms.put("mobile", mobile);//电话
        postParms.put("realname", nickname);//名称

        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqangelPayB, postParms, complete);
    }

    public void reqAnglePayQuery(RequestComplete complete) {
        HashMap<String, Object> getParms = new HashMap<>();
        ModuleMgr.getHttpMgr().reqPostNoCacheNoEncHttp(UrlParam.reqAnglePayQuery, getParms, complete);
    }

    //================ 发现 start =========================\\

    /**
     * 举报
     *
     * @param tuid     被举报人uid
     * @param content
     * @param detail
     * @param complete
     */
    public void complainBlack(long tuid, String content, String detail, RequestComplete complete) {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("tuid", tuid);
        parms.put("content", content);
        parms.put("detail", detail);

        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.complainBlack, parms, complete);
    }

    /**
     * "page":1,
     * "limit":10,
     * "reload":1,  //是否刷新缓存 1为刷新缓存 0为向下翻页
     * "x":104.55,  //[opt] 用户位置
     * "y":40.11	//[opt] 用户位置
     * "ver":5		//[opt]大版本号
     * "isnear":1	//[opt]是否搜索附近的人
     *
     * @param page
     * @param reload
     * @param complete
     */
    public void getMainPage(int page, int reload, RequestComplete complete) {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("page", page);
        parms.put("reload", reload);
        parms.put("limit", 10);
        parms.put("x", LocationMgr.getInstance().getPointD().longitude);  //经度
        parms.put("y", LocationMgr.getInstance().getPointD().latitude); //纬度
        parms.put("ver", Constant.SUB_VERSION);
        parms.put("isnear", 1);

        ModuleMgr.getHttpMgr().reqPostAndCacheHttp(UrlParam.getMainPage, parms, complete);
    }


    /**
     * "x":104.55,  //用户位置
     * "y":40.11	// 用户位置
     * "ver":5		//[opt]大版本号
     *
     * @param complete
     */
    public void getNearUsers2(RequestComplete complete) {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("x", LocationMgr.getInstance().getPointD().longitude);  //经度
        parms.put("y", LocationMgr.getInstance().getPointD().latitude); //纬度
        parms.put("ver", Constant.SUB_VERSION);

        ModuleMgr.getHttpMgr().reqPostAndCacheHttp(UrlParam.getNearUsers2, parms, complete);
    }

    /**
     * 获取我的好友列表
     *
     * @param page
     * @param complete
     */
    public void getMyFriends(int page, RequestComplete complete) {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("page", page);
        parms.put("limit", 10);
        ModuleMgr.getHttpMgr().reqPostAndCacheHttp(UrlParam.getMyFriends, parms, complete);
    }

    /**
     * 获取黑名单列表
     *
     * @param complete
     */
    public void getMyDefriends(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostAndCacheHttp(UrlParam.getMyDefriends, null, complete);
    }

    //================ 发现 end =========================\\

    /**
     * 获取自定义表情列表
     *
     * @param complete
     */
    public void reqCustomFace(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostAndCacheHttp(UrlParam.reqCustomFace, null, complete);
    }

    /**
     * add自定义表情
     *
     * @param url
     * @param complete
     */
    public void addCustomFace(String url, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("url", url);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.AddCustomFace, postParms, complete);
    }

    /**
     * del自定义表情
     *
     * @param url
     * @param complete
     */
    public void delCustomFace(String url, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("url", url);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.delCustomFace, postParms, complete);
    }

    public void reqUserInfoSummary(List<Long> uids, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("uids", uids.toArray(new Long[uids.size()]));
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqUserInfoSummary, postParms, complete);
    }

    public void checkycoin(RequestComplete complete) {
        HashMap<String, Object> getParms = new HashMap<>();
        getParms.put("uid", App.uid);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.checkycoin, getParms, complete);
    }

    /**
     * 获取用户热门列表
     *
     * @param page
     * @param limit
     * @param reload   是否刷新缓存 1为刷新缓存 0为向下翻页
     * @param complete
     */
    public void reqUserInfoHotList(int page, final int limit, boolean reload, RequestComplete complete) {
        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("page", page);
        postParams.put("limit", limit);
        postParams.put("reload", reload ? 1 : 0);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqUserHotList, postParams, complete);
    }
}