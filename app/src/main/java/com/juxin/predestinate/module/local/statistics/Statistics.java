package com.juxin.predestinate.module.local.statistics;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.utils.EncryptUtil;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.cache.PCache;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 各种统计管理类
 * Created by ZRP on 2017/4/13.
 */
public class Statistics {

    private static final String BEHAVIOR_CACHE_KEY = "BEHAVIOR_CACHE_KEY";//用户行为缓存key
    private static final String BEHAVIOR_SESSION_KEY = "BEHAVIOR_SESSION_KEY";//用户行为session time校验存储key
    private static final String BEHAVIOR_SESSION_ID_KEY = "BEHAVIOR_SESSION_ID_KEY";//用户行为sessionId存储key
    private static final String BEHAVIOR_FIRST_KEY = "BEHAVIOR_FIRST_KEY";//是否到达第二天存储key
    private static final long BEHAVIOR_SESSION_TIME = 30 * 60 * 1000;//session有效时间：30分钟
    private static final Gson gson = new Gson();

    /**
     * 客户端统计项
     */
    private enum StatisticPoint {
        Startup,                // APP启动日志:用户每启动一次APP,就发送一次最新的数据
        Shutdown,               // APP关闭事件日志
        InstallVideoPlugin,     // 安装语音视频插件
        UserBehavior,           // 客户端用户行为日志
    }

    /**
     * APP启动日志:用户每启动一次APP,就发送一次最新的数据
     */
    public static void startUp() {
        Map<String, Object> singleMap = new HashMap<>();
        singleMap.put("time", System.currentTimeMillis());//发送时间戳
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//用户UID,获取失败返回0
        singleMap.put("gender", ModuleMgr.getCenterMgr().getMyInfo().getGender());//用户性别
        singleMap.put("client_type", Constant.PLATFORM_TYPE);//客户端类型
        singleMap.put("version", String.valueOf(Constant.SUB_VERSION));//客户端标记号
        singleMap.put("build_ver", ModuleMgr.getAppMgr().getVerCode());//客户端打包版本号
        singleMap.put("channel_id", ModuleMgr.getAppMgr().getMainChannelID() + "_"
                + ModuleMgr.getAppMgr().getSubChannelID());//渠道编号<主渠道>_<子渠道>
        singleMap.put("package_name", ModuleMgr.getAppMgr().getPackageName());//客户端包名
        singleMap.put("sign_name", EncryptUtil.sha1(ModuleMgr.getAppMgr().getSignature()));//客户端签名
        singleMap.put("device_id", ModuleMgr.getAppMgr().getDeviceID());//设备标识符,获取失败返回空字符串
        singleMap.put("client_ip", NetworkUtils.getIpAddressString());//客户端IP
        singleMap.put("device_model", android.os.Build.MODEL);//手机型号
        singleMap.put("device_os_version", android.os.Build.DISPLAY);//手机操作系统版本
        singleMap.put("screen_width", String.valueOf(ModuleMgr.getAppMgr().getScreenWidth()));//屏幕宽度
        singleMap.put("screen_height", String.valueOf(ModuleMgr.getAppMgr().getScreenHeight()));//屏幕高度
        singleMap.put("tracker_code", EncryptUtil.md5(ModuleMgr.getAppMgr().getDeviceID()));//追踪码MD5,访客(包含游客)唯一标识且终生唯一
        singleMap.put("session_id", EncryptUtil.md5(getSessionId()));//会话标识MD5,30分钟无操作失效

        // 判断离上次产生session是否超过1天 //是否是APP启动后发送的第1条Startup数据,统计APP启动次数字段
        singleMap.put("first_start", ModuleMgr.getCommonMgr().checkDateAndSave(BEHAVIOR_FIRST_KEY));

        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("topic", StatisticPoint.Startup);//统计项名称
        postParams.put("message", singleMap);//统计项数据内容
        sendStatistics(postParams);
    }

    /**
     * APP关闭事件日志
     */
    public static void shutDown() {
        Map<String, Object> singleMap = new HashMap<>();
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//用户UID,获取失败返回0
        singleMap.put("time", System.currentTimeMillis());//发送时间戳

        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("topic", StatisticPoint.Shutdown);//统计项名称
        postParams.put("message", singleMap);//统计项数据内容
        sendStatistics(postParams);
    }

    /**
     * 安装语音视频插件统计
     *
     * @param success 是否成功安装插件
     */
    public static void installVideoPlugin(boolean success) {
        Map<String, Object> singleMap = new HashMap<>();
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//用户UID,获取失败返回0
        singleMap.put("time", System.currentTimeMillis());//发送时间戳
        singleMap.put("success", success);//是否成功安装插件

        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("topic", StatisticPoint.InstallVideoPlugin);//统计项名称
        postParams.put("message", singleMap);//统计项数据内容
        sendStatistics(postParams);
    }

    /**
     * 用户行为统计
     *
     * @param sendPoint 统计点
     * @param to_uid    与其产生交互的用户uid
     * @param fixParams 行为修正参考参数，拼接成json格式字符串，如{"to_uid":80429386,"index":3}
     */
    public static void userBehavior(SendPoint sendPoint, long to_uid, String fixParams) {
        Map<String, Object> singleMap = new HashMap<>();
        singleMap.put("time", System.currentTimeMillis());//发送时间戳
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//用户UID,获取失败返回0
        singleMap.put("to_uid", to_uid);//与谁交互UID(可选)

        singleMap.put("gender", ModuleMgr.getCenterMgr().getMyInfo().getGender());//用户性别
        singleMap.put("client_type", Constant.PLATFORM_TYPE);//客户端类型
        singleMap.put("version", String.valueOf(Constant.SUB_VERSION));//客户端标记号
        singleMap.put("build_ver", ModuleMgr.getAppMgr().getVerCode());//客户端打包版本号
        singleMap.put("channel_id", ModuleMgr.getAppMgr().getMainChannelID() + "_"
                + ModuleMgr.getAppMgr().getSubChannelID());//渠道编号<主渠道>_<子渠道>
        singleMap.put("package_name", ModuleMgr.getAppMgr().getPackageName());//客户端包名
        singleMap.put("sign_name", EncryptUtil.sha1(ModuleMgr.getAppMgr().getSignature()));//客户端签名
        singleMap.put("device_id", ModuleMgr.getAppMgr().getDeviceID());//设备标识符,获取失败返回空字符串
        singleMap.put("client_ip", NetworkUtils.getIpAddressString());//客户端IP
        singleMap.put("device_model", android.os.Build.MODEL);//手机型号
        singleMap.put("device_os_version", android.os.Build.DISPLAY);//手机操作系统版本
        singleMap.put("screen_width", String.valueOf(ModuleMgr.getAppMgr().getScreenWidth()));//屏幕宽度
        singleMap.put("screen_height", String.valueOf(ModuleMgr.getAppMgr().getScreenHeight()));//屏幕高度
        singleMap.put("tracker_code", EncryptUtil.md5(ModuleMgr.getAppMgr().getDeviceID()));//追踪码MD5,访客(包含游客)唯一标识且终生唯一
        singleMap.put("session_id", EncryptUtil.md5(getSessionId()));//会话标识MD5,30分钟无操作失效

        LinkedList<Activity> activities = App.getLifecycleCallbacks().getActivities();
        singleMap.put("page", App.getActivity().getClass().getSimpleName());//当前页面，栈顶activity
        singleMap.put("referer", activities.size() <= 1 ? ""
                : activities.get(activities.size() - 1).getClass().getSimpleName());//来源页面(可选)，栈中第二个activity
        singleMap.put("event_type", sendPoint.toString());//事件类型
        singleMap.put("event_data", TextUtils.isEmpty(fixParams) ? "{}" : fixParams);//事件数据(可选,有数据需提供数据说明文档)

        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("topic", StatisticPoint.UserBehavior);//统计项名称
        postParams.put("message", singleMap);//统计项数据内容
        sendStatistics(postParams);
    }

    /**
     * @return 获取用户sessionId，该id本地使用时间戳进行标识
     */
    private static String getSessionId() {
        long sessionTime = PSP.getInstance().getLong(BEHAVIOR_SESSION_KEY, -1);
        String sessionId = PSP.getInstance().getString(BEHAVIOR_SESSION_ID_KEY, "");
        long currentTimeMillis = System.currentTimeMillis();
        if (sessionTime == -1 || TextUtils.isEmpty(sessionId) || currentTimeMillis - sessionTime > BEHAVIOR_SESSION_TIME) {
            PSP.getInstance().put(BEHAVIOR_SESSION_KEY, currentTimeMillis);
            PSP.getInstance().put(BEHAVIOR_SESSION_ID_KEY, String.valueOf(currentTimeMillis));
            return String.valueOf(currentTimeMillis);
        }
        PSP.getInstance().put(BEHAVIOR_SESSION_KEY, currentTimeMillis);
        return sessionId;
    }

    /**
     * 发送统计内容
     *
     * @param postParams 提交参数map
     */
    private static void sendStatistics(HashMap<String, Object> postParams) {
        if (postParams == null) postParams = new HashMap<>();
        LinkedList<HashMap<String, Object>> cachedList = gson.fromJson(PCache.getInstance().getCache(BEHAVIOR_CACHE_KEY),
                new TypeToken<LinkedList<HashMap<String, Object>>>() {
                }.getType());
        if (cachedList == null) cachedList = new LinkedList<>();
        if (cachedList.size() < 10) {
            cachedList.add(postParams);
            PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, gson.toJson(cachedList));
            return;
        }
        if (NetworkUtils.isConnected(App.context)) {
            cachedList.add(postParams);

            LinkedList<Map<String, Object>> statisticsList = new LinkedList<>();
            Map<String, Object> batchMap = new HashMap<>();
            for (HashMap<String, Object> maps : cachedList) {
                Map<String, Object> headBodyMap = new HashMap<>();
                headBodyMap.put("headers", new HashMap<>());
                headBodyMap.put("body", gson.toJsonTree(maps).toString());
                statisticsList.add(headBodyMap);
            }
            batchMap.put("data", statisticsList);
            final LinkedList<HashMap<String, Object>> tempList = cachedList;

            PLogger.d("---Statistics--->" + gson.toJson(batchMap));
            ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.statistics, batchMap, new RequestComplete() {
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
            cachedList.add(postParams);
            PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, gson.toJson(cachedList));
        }
    }
}
