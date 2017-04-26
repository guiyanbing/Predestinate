package com.juxin.predestinate.module.local.common;

import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.utils.EncryptUtil;
import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.bean.config.CommonConfig;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.config.ServerTime;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.request.RequestParam;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.sayhi.SayHelloDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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

    // ---------------------------- 软件升级 start ------------------------------

    /**
     * 检查应用升级
     *
     * @param activity FragmentActivity实例
     */
    public void checkUpdate(final FragmentActivity activity) {
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("ssid", ModuleMgr.getAppMgr().getSubChannelID());// 子渠道号
        postParams.put("suid", ModuleMgr.getAppMgr().getMainChannelID());// 渠道号
        postParams.put("platform", 1);// 平台 1-android， 2-ios
        postParams.put("version", ModuleMgr.getAppMgr().getVerCode());// 版本号(整数)
        postParams.put("app_key", EncryptUtil.sha1(ModuleMgr.getAppMgr().getSignature()));// 软件签名sha1
        postParams.put("package_name", ModuleMgr.getAppMgr().getPackageName());// 包名
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.checkUpdate, postParams, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    UIShow.showUpdateDialog(activity, (AppUpdate) response.getBaseData());
                }
            }
        });
    }

    /**
     * 将用户信息写入文件，跨软件升级使用
     */
    public void updateSaveUP() {
        String str_json = PSP.getInstance().getString(FinalKey.LOGIN_USER_KEY, null);
        if (!TextUtils.isEmpty(str_json)) {
            String cacheDir = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                    File.separator + "xiaou" + File.separator;
            if (DirType.isFolderExists(cacheDir)) {
                File file = new File(cacheDir + "user_cache");
                OutputStreamWriter out = null;
                try {
                    out = new OutputStreamWriter(new FileOutputStream(file));//根据文件创建文件的输出流
                    out.write(str_json);//向文件写入内容
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
        }
    }
    // ---------------------------- 软件升级 end ------------------------------

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

    /**
     * 生成订单
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
     * @param complete
     */
    public void reqWXMethod(String name, int payID, int payMoney, int payCType, RequestComplete complete) {
        HashMap<String, Object> postParms = new HashMap<>();
        postParms.put("subject", name);
        postParms.put("body", name);
        postParms.put("productid", payID);
        postParms.put("total_fee", payMoney);

      //  postParms.put("total_fee", "0.01");// 钱
        if(payCType > -1){
            postParms.put("payCType", payCType);
        }
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqWX, postParms, complete);
    }

    /**
     * 银联或支付宝
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
        postParms.put("body","android-" +  name);
        postParms.put("productid", payID);
        postParms.put("total_fee", payMoney);
        postParms.put("payCType", 1000);

        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(urlParam, postParms, complete);
    }

    /**
     * 手机充值卡
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

        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqPhoneCard, postParms, complete);
    }

    public void reqSearchPhoneCardMethod(String orderNo,  RequestComplete complete) {
        HashMap<String, Object> getParams = new HashMap<>();
        getParams.put("orderNo", orderNo);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqSearchPhoneCard, getParams, complete);

    }
}