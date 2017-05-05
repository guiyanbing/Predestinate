package com.juxin.predestinate.module.local.statistics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxin.library.log.PLogger;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.cache.PCache;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 各种统计管理类//TODO 接口及统计策略待确认
 * Created by ZRP on 2017/4/13.
 */
public class Statistics {

    //向大数据发送行为统计
    private static final String FATE_SEND_PATH_STATISTICS = Constant.FATE_IT_GO + "xs/hdp/Action";

    /**
     * 用户行为统计点
     */
    public enum Behavior {
        RegisterFirstPage,      //注册首页
        LoginPage,              //登录
        UserLoginPage,          //用户登录
        UserRegisterPage,       //用户注册
        DiscoveryFirstPage,     //发现-首页
        DiscoveryNearPage,      //发现-附近
        PersonalInfoPage,       //查看他人资料界面
        MessageFirstPage,       //消息首页
        MessagePage             //聊天界面
    }

    /**
     * 用户行为发送点
     */
    public enum SendPoint {
        ClickSayHi,             //打招呼
        ViewPersonalInfo,       //查看个人资料
        ClickViewAlbum,         //点击相册
        ClickFocus,             //点击关注
        ClickViewQQWechat,      //点击QQ微信
        ClickSendMessage        //点击发信
    }

    private static final String BEHAVIOR_CACHE_KEY = "BEHAVIOR_CACHE_KEY";//用户行为缓存key
    private static final Gson gson = new Gson();
    private static LinkedHashMap<String, String> behaviorPathMap = new LinkedHashMap<>();//用户行为统计list

    /**
     * 添加统计点
     *
     * @param behavior 统计点
     */
    public static void addBehavior(Class clazz, Behavior behavior) {
        behaviorPathMap.put(clazz.getSimpleName(), behavior.toString());
    }

    /**
     * 移除class对应的behavior
     *
     * @param clazz 统计点class
     */
    public static void removeBehavior(Class clazz) {
        String remove = behaviorPathMap.remove(clazz.getSimpleName());
        PLogger.d("--->" + remove);
    }

    /**
     * @return 获取行为列表
     */
    private static LinkedList<String> getBehaviorPath() {
        LinkedList<String> linkedList = new LinkedList<>();
        for (Map.Entry<String, String> entry : behaviorPathMap.entrySet()) {
            linkedList.add(String.valueOf(entry.getValue()));
        }
        return linkedList;
    }

    /**
     * 打招呼统计
     *
     * @param to_uid  打招呼对方uid
     * @param success 是否打招呼成功
     */
    public static void clickSayHi(long to_uid, boolean success) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("touid", to_uid);
        hashMap.put("success", success);
        sendBehaviorStatistics(SendPoint.ClickSayHi, hashMap);
    }

    /**
     * 查看对方个人资料统计
     *
     * @param to_uid 对方uid
     */
    public static void viewPersonalInfo(long to_uid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("touid", to_uid);
        sendBehaviorStatistics(SendPoint.ViewPersonalInfo, hashMap);
    }

    /**
     * 查看对方相册统计
     *
     * @param to_uid  对方uid
     * @param success 是否成功进入对方相册
     * @param action  "cancel"-取消；"buy_vip"-去开通；""-进入相册成功
     */
    public static void clickViewAlbum(long to_uid, boolean success, String action) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("touid", to_uid);
        hashMap.put("success", success);
        hashMap.put("action", action);
        sendBehaviorStatistics(SendPoint.ClickViewAlbum, hashMap);
    }

    /**
     * 点击关注统计
     *
     * @param to_uid 对方uid
     * @param action "focus"-关注；"unfocus"-取消关注
     */
    public static void clickFocus(long to_uid, String action) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("touid", to_uid);
        hashMap.put("action", action);
        sendBehaviorStatistics(SendPoint.ClickFocus, hashMap);
    }

    /**
     * 点击查看QQ微信统计
     *
     * @param to_uid 对方uid
     */
    public static void clickViewQQWechat(long to_uid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("touid", to_uid);
        sendBehaviorStatistics(SendPoint.ClickViewQQWechat, hashMap);
    }

    /**
     * 点击发信统计
     *
     * @param to_uid  对方uid
     * @param success 是否成功进入私聊页面
     * @param action  "say_hi"-给他/她打个招呼；"buy_vip"-开通vip；""-进入私聊页面成功
     */
    public static void clickSendMessage(long to_uid, boolean success, String action) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("touid", to_uid);
        hashMap.put("success", success);
        hashMap.put("action", action);
        sendBehaviorStatistics(SendPoint.ClickSendMessage, hashMap);
    }

    /**
     * 向大数据发送行为统计数据
     *
     * @param sendPoint 用户行为发送点
     */
    public static void sendBehaviorStatistics(SendPoint sendPoint, HashMap<String, Object> singleMap) {
        final HashMap<String, Object> postParams = new HashMap<>();
        singleMap.put("time", System.currentTimeMillis());
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());
        //[此处两个版本号信息文档](http://test.game.xiaoyouapp.cn:20080/juxin/api_doc/src/master/version/versions.md)
        singleMap.put("version", Constant.SUB_VERSION + "");
        singleMap.put("client_type", "2");
        singleMap.put("path", getBehaviorPath().toArray());
        postParams.put("topic", sendPoint.toString());
        postParams.put("message", singleMap);

        LinkedList<HashMap<String, Object>> cachedList = gson.fromJson(PCache.getInstance().getCache(BEHAVIOR_CACHE_KEY),
                new TypeToken<LinkedList<HashMap<String, Object>>>() {
                }.getType());
        if (NetworkUtils.isConnected(App.context)) {
            if (cachedList == null) cachedList = new LinkedList<>();
            cachedList.add(postParams);
            Map<String, Object> batchMap = new HashMap<>();
            batchMap.put("data", cachedList);
            final LinkedList<HashMap<String, Object>> tempList = cachedList;

            PLogger.d("---Statistics--->" + gson.toJson(batchMap));
            ModuleMgr.getCommonMgr().CMDRequest("POST", true, FATE_SEND_PATH_STATISTICS, batchMap, new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (response.isOk()) {
                        PCache.getInstance().deleteCache(BEHAVIOR_CACHE_KEY);
                    } else {
                        PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, gson.toJson(tempList));
                    }
                }
            });
        } else {
            if (cachedList == null) {
                LinkedList<HashMap<String, Object>> tempList = new LinkedList<>();
                tempList.add(postParams);
                PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, gson.toJson(tempList));
            } else {
                cachedList.add(postParams);
                PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, gson.toJson(cachedList));
            }
        }
    }
}
