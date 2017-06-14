package com.juxin.predestinate.module.local.statistics;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.utils.EncryptUtil;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.module.local.location.LocationMgr;
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

    // ------------------------------旧版本统计项-----------------------------------

    /**
     * 应用激活统计
     */
    public static void activeStatistic() {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("app_key", EncryptUtil.sha1(ModuleMgr.getAppMgr().getSignature()));
        getParams.put("package_name", ModuleMgr.getAppMgr().getPackageName());
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.activeStatistic, getParams, null);
    }

    /**
     * 旧版本php应用内统计
     *
     * @param action 行为字符串
     */
    public static void userOnline(String action) {
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());
        getParams.put("action", action);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.userOnline, getParams, null);
    }

    /**
     * 支付统计
     *
     * @param uid   交互对方uid
     * @param tplId 交互对方channel_uid
     */
    public static void payStatistic(String uid, String tplId) {
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(tplId)) return;

        PLogger.d("------>" + uid + "/" + tplId);
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id", uid);
        getParams.put("tplId", tplId);
        ModuleMgr.getHttpMgr().reqGetNoCacheHttp(UrlParam.payStatistic, getParams, null);
    }

    // ---------------------------新版本统计-----------------------------

    private static final String BEHAVIOR_CACHE_KEY = "BEHAVIOR_CACHE_KEY";//用户行为缓存key
    private static final String BEHAVIOR_SESSION_KEY = "BEHAVIOR_SESSION_KEY";//用户行为session time校验存储key
    private static final String BEHAVIOR_SESSION_ID_KEY = "BEHAVIOR_SESSION_ID_KEY";//用户行为sessionId存储key
    private static final String BEHAVIOR_ACCOUNT_KEY = "BEHAVIOR_ACCOUNT_KEY";//是否切换帐号登录存储key，存储用户uid
    private static final long BEHAVIOR_SESSION_TIME = 30 * 60 * 1000;//session有效时间：30分钟

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
     * 每隔5min发送一次定位信息
     */
    public static void loopLocation() {
        MsgMgr.getInstance().delay(new Runnable() {
            @Override
            public void run() {
                location();
                loopLocation();
            }
        }, Constant.CHAT_RESEND_TIME, false);
    }

    /**
     * 向大数据发送位置统计
     */
    public static void location() {
        if (ModuleMgr.getCenterMgr().getMyInfo().getUid() == 0) return;

        LocationMgr.PointD pointD = LocationMgr.getInstance().getPointD();
        String province = pointD.province;
        if (TextUtils.isEmpty(province) || "null".equals(province)) return;

        Map<String, Object> requestObject = new HashMap<>();
        requestObject.put("longitude", pointD.longitude);
        requestObject.put("latitude", pointD.latitude);
        requestObject.put("country_code", pointD.countryCode);
        requestObject.put("province", province);
        requestObject.put("city_code", pointD.city);
        requestObject.put("district", pointD.district);
        requestObject.put("street_number", pointD.streetNum);
        requestObject.put("building_id", pointD.buildID);

        PLogger.d("------>" + JSON.toJSONString(requestObject));
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.locationStatistics, requestObject, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PLogger.d("---locationStatistics--->" + response.getResponseString());
            }
        });
    }

    /**
     * APP启动日志:用户每启动一次APP,就发送一次最新的数据
     */
    public static void startUp() {
        Map<String, Object> singleMap = new HashMap<>();
        singleMap.put("time", ModuleMgr.getAppMgr().getSecondTime());//发送时间戳
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//用户UID,获取失败返回0
        singleMap.put("gender", ModuleMgr.getCenterMgr().getMyInfo().getGender());//用户性别
        singleMap.put("client_type", Constant.PLATFORM_TYPE);//客户端类型
        singleMap.put("version", String.valueOf(Constant.SUB_VERSION));//客户端标记号
        singleMap.put("build_ver", ModuleMgr.getAppMgr().getVerCode());//客户端打包版本号
        singleMap.put("channel_id", ModuleMgr.getCenterMgr().getMyInfo().getChannel_uid() + "_"
                + ModuleMgr.getCenterMgr().getMyInfo().getChannel_sid());//渠道编号<主渠道>_<子渠道>
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
        singleMap.put("first_start", isSwitchAccount());

        HashMap<String, Object> postParams = new HashMap<>();
        postParams.put("topic", StatisticPoint.Startup);//统计项名称
        postParams.put("message", singleMap);//统计项数据内容
        sendStatistics(postParams);
    }

    /**
     * @return 用户切换帐号登录-false，同一帐号登录-true
     */
    private static boolean isSwitchAccount() {
        long account = PSP.getInstance().getLong(BEHAVIOR_ACCOUNT_KEY, -1);
        if (account == -1 || ModuleMgr.getLoginMgr().getUid() == account) {
            PSP.getInstance().put(BEHAVIOR_ACCOUNT_KEY, ModuleMgr.getLoginMgr().getUid());
            return false;
        }
        return true;
    }

    /**
     * APP关闭事件日志
     */
    public static void shutDown() {
        Map<String, Object> singleMap = new HashMap<>();
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//用户UID,获取失败返回0
        singleMap.put("time", ModuleMgr.getAppMgr().getSecondTime());//发送时间戳

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
        singleMap.put("time", ModuleMgr.getAppMgr().getSecondTime());//发送时间戳
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
     */
    public static void userBehavior(SendPoint sendPoint) {
        userBehavior(sendPoint, 0);
    }

    /**
     * 用户行为统计
     *
     * @param sendPoint 统计点
     * @param to_uid    与其产生交互的用户uid
     */
    public static void userBehavior(SendPoint sendPoint, long to_uid) {
        userBehavior(sendPoint, to_uid, null);
    }

    /**
     * 用户行为统计
     *
     * @param sendPoint 统计点
     * @param fixParams 行为修正参考参数，map格式，内部拼接成json格式字符串
     */
    public static void userBehavior(SendPoint sendPoint, Map<String, Object> fixParams) {
        userBehavior(sendPoint, 0, fixParams);
    }

    /**
     * 用户行为统计
     *
     * @param sendPoint 统计点
     * @param to_uid    与其产生交互的用户uid
     * @param fixParams 行为修正参考参数，map格式，内部拼接成json格式字符串
     */
    public static void userBehavior(SendPoint sendPoint, long to_uid, Map<String, Object> fixParams) {
        userBehavior(sendPoint.toString(), to_uid, fixParams == null ? "{}" : JSON.toJSONString(fixParams));
    }

    /**
     * 用户行为统计
     *
     * @param sendPoint 统计点
     * @param to_uid    与其产生交互的用户uid
     * @param fixParams 行为修正参考参数，拼接成json格式字符串，如{"to_uid":80429386,"index":3}
     */
    public static void userBehavior(String sendPoint, long to_uid, String fixParams) {
        if (TextUtils.isEmpty(sendPoint)) return;

        Map<String, Object> singleMap = new HashMap<>();
        singleMap.put("time", ModuleMgr.getAppMgr().getSecondTime());//发送时间戳
        singleMap.put("uid", ModuleMgr.getCenterMgr().getMyInfo().getUid());//用户UID,获取失败返回0
        singleMap.put("to_uid", to_uid);//与谁交互UID(可选)

        singleMap.put("gender", ModuleMgr.getCenterMgr().getMyInfo().getGender());//用户性别
        singleMap.put("client_type", Constant.PLATFORM_TYPE);//客户端类型
        singleMap.put("version", String.valueOf(Constant.SUB_VERSION));//客户端标记号
        singleMap.put("build_ver", ModuleMgr.getAppMgr().getVerCode());//客户端打包版本号
        singleMap.put("channel_id", ModuleMgr.getCenterMgr().getMyInfo().getChannel_uid() + "_"
                + ModuleMgr.getCenterMgr().getMyInfo().getChannel_sid());//渠道编号<主渠道>_<子渠道>
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
        singleMap.put("referer", activities.size() > 1 ?
                activities.get(activities.size() - 2).getClass().getSimpleName() : "");//来源页面(可选)，栈中第二个activity
        singleMap.put("event_type", sendPoint);//事件类型
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
        LinkedList<HashMap<String, Object>> cachedList = JSON.parseObject(PCache.getInstance().getCache(BEHAVIOR_CACHE_KEY),
                new TypeReference<LinkedList<HashMap<String, Object>>>() {
                });
        if (cachedList == null) cachedList = new LinkedList<>();
        if (cachedList.size() < 10) {
            cachedList.add(postParams);
            PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, JSON.toJSONString(cachedList));
            return;
        }
        if (NetworkUtils.isConnected(App.context)) {
            cachedList.add(postParams);

            LinkedList<Map<String, Object>> statisticsList = new LinkedList<>();
            Map<String, Object> batchMap = new HashMap<>();
            for (HashMap<String, Object> maps : cachedList) {
                Map<String, Object> headBodyMap = new HashMap<>();
                headBodyMap.put("headers", new HashMap<>());
                headBodyMap.put("body", JSON.toJSONString(maps));
                statisticsList.add(headBodyMap);
            }
            batchMap.put("data", statisticsList);
            final LinkedList<HashMap<String, Object>> tempList = cachedList;

            PLogger.d("---Statistics--->" + JSON.toJSONString(batchMap));
            ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.statistics, batchMap, new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (response.isOk()) {
                        PCache.getInstance().deleteCache(BEHAVIOR_CACHE_KEY);
                    } else {
                        PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, JSON.toJSONString(tempList));
                    }
                }
            });
        } else {
            cachedList.add(postParams);
            PCache.getInstance().cacheString(BEHAVIOR_CACHE_KEY, JSON.toJSONString(cachedList));
        }
    }
}
