package com.juxin.predestinate.module.local.common;

import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.utils.EncryptUtil;
import com.juxin.predestinate.bean.config.CommonConfig;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.ServerTime;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.request.RequestParam;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.ui.mail.sayhi.SayHelloDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用，新版本
 * Created by Xy on 17/3/22.
 */
public class CommonMgr implements ModuleBase {

    private CommonConfig commonConfig;//服务器静态配置

    @Override
    public void init() {
        requestStaticConfig();
    }

    @Override
    public void release() {

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
     * 请求服务器在线配置
     */
    public void requestStaticConfig() {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("ver", Constant.SUB_VERSION);//静态配置内容的版本本号(整数)
        ModuleMgr.getHttpMgr().reqGet(UrlParam.staticConfig, null, requestParams,
                RequestParam.CacheType.CT_Cache_Url, true, new RequestComplete() {
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
        return commonConfig;
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
     * 检查应用升级
     *
     * @param complete 界面回调
     */
    public void checkUpdate(RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("ssid", ModuleMgr.getAppMgr().getSubChannelID());// 子渠道号
        postParams.put("suid", ModuleMgr.getAppMgr().getMainChannelID());// 渠道号
        postParams.put("platform", 1);// 平台 1-android， 2-ios
        postParams.put("version", ModuleMgr.getAppMgr().getVerCode());// 版本号(整数)
        postParams.put("app_key", EncryptUtil.sha1(ModuleMgr.getAppMgr().getSignature()));// 软件签名sha1
        postParams.put("package_name", ModuleMgr.getAppMgr().getPackageName());// 包名
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.checkUpdate, postParams, complete);
    }

    /**
     * 获取每日推荐列表（一键打招呼列表）
     *
     * @param complete
     */
    public void getSayHiList(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.reqSayHiList, null, complete);
    }

    /**
     * 一键打招呼的状态Key
     *
     * @return
     */
    private String getSayHelloKey() {
        return "Say_Hello_" + ModuleMgr.getCenterMgr().getMyInfo().getUid();
    }

    /**
     * 显示一键打招呼对话框
     *
     * @param context
     */
    public void showSayHelloDialog(FragmentActivity context) {
//        if (checkDateAndSave(getSayHelloKey())) {
        SayHelloDialog sayHelloDialog = new SayHelloDialog();
        sayHelloDialog.showDialog(context);
//        }
    }

    /**
     * 推荐的人
     *
     * @param complete
     */
    public void sysRecommend(RequestComplete complete, final int cur, HashMap<String, Object> post_param) {
        post_param.put("page", cur);
        post_param.put("limit", 10);
        post_param.put("tm", ServerTime.getServerTime().getTimeInMillis());
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.sysRecommend, post_param, complete);
    }

    /**
     * 系统标签
     *
     * @param complete
     */
    public void sysTags(RequestComplete complete) {
        HashMap<String, Object> post_param = new HashMap<>();
        post_param.put("page", 1);
        post_param.put("limit", 20);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.sysTags, post_param, complete);
    }

    //============================== 小友模块相关接口 =============================

    /**
     * 好友标签分组成员
     *
     * @param complete
     */
    public void getTagGroupMember(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqTagGroupMember, null, complete);
    }

    /**
     * 增加自己的好友的
     *
     * @param complete
     */
    public void addFriendTag(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqAddFriendTag, null, complete);
    }

    /**
     * 添加标签分组
     *
     * @param complete
     */
    public void addTagGroup(List<String> tag_name, List<String> uid_list, RequestComplete complete) {
//        "tag_name": ["新标签2","sssss","aaaa"]	// 标签名字,
//        "uid_list": [10000,12222,13333]			// opt 标签成员
//        String names = "[\"新标签2\"]";
//        String list = "[\"10000\",\"12222\"]";
        String[] names = tag_name.toArray(new String[tag_name.size()]);
        String[] list = uid_list.toArray(new String[uid_list.size()]);
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());// 标签名字
        postParams.put("tag_name", names);// 标签名字
        postParams.put("uid_list", list);// 标签成员
//        Log.e("TTTTTTTTTTTTTTTBB",names+"||"+list);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqAddTagGroup, postParams, complete);
    }

    /**
     * 添加好友标签分组成员
     *
     * @param complete
     */
    public void addTagGroupMember(long tag, Set<String> uids, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        String[] list = uids.toArray(new String[uids.size()]);
//        Log.e("TTTTTTTTTTTTTTTBB", tag + "||" + list);
        postParams.put("tag", tag);// 标签id
        postParams.put("uids", list);// 要删除的uid
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqAddTagGroupMember, postParams, complete);
    }

    /**
     * 删除自己好友的 tag
     *
     * @param complete
     */
    public void delFriendTag(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqDelFriendTag, null, complete);
    }

    /**
     * 删除标签分组
     *
     * @param complete
     */
    public void delTagGroup(int tag_id, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("tag_id", tag_id);// tag_id
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqDelTagGroup, postParams, complete);
    }

    /**
     * 删除好友标签分组成员
     *
     * @param complete
     */
    public void delTagGroupMember(int tag, List<Long> uids, RequestComplete complete) {
        Gson gson = new Gson();
        String list = gson.toJson(uids);
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("tag", tag);// 标签id
        postParams.put("uids", list);// 要删除的uid
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqDelTagGroupMember, postParams, complete);
    }

    /**
     * 好友列表
     *
     * @param complete
     */
    public void getFriendList(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqFriendList, null, complete);
    }

    /**
     * 最近互动好友列表
     *
     * @param complete
     */
    public void getLatestInteractiveList(int page, int limit, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("page", page);// 第几页
        postParams.put("limit", limit);// 每页条数
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqLatestInteractive, postParams, complete);
    }

    /**
     * 修改标签分组
     *
     * @param complete
     */
    public void ModifyTagGroup(long tag_id, String name, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("tag_id", tag_id);// 标签 ID
        postParams.put("name", name);// 新的分组名字
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqModifyTagGroup, postParams, complete);
    }

    /**
     * 好友标签分组
     *
     * @param complete
     */
    public void getTagGroupList(RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqTagGroup, null, complete);
    }

    /**
     * 批量获取用户简略信息
     *
     * @param complete
     */
    public void getUserSimpleList(ArrayList<String> userLists, RequestComplete complete) {
//        Gson gson = new Gson();
//        String uidlist = gson.toJson(userLists);
        String[] uidlist = userLists.toArray(new String[userLists.size()]);
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uidlist", uidlist);// uids
//        Log.e("TTTTTNNN", uidlist + "");
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
     * 送礼物
     *
     * @param complete
     */
    public void givePresent(long uid, long tuid, int id, int count, RequestComplete complete) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uid", uid);// 收礼物的uid
        postParams.put("tuid", tuid);// 送礼物的用户id
        postParams.put("id", id);// 礼物id
        postParams.put("count", count);// 礼物数量
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.givePresent, postParams, complete);
    }
}