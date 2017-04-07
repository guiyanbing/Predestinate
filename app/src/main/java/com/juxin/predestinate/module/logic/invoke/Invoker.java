package com.juxin.predestinate.module.logic.invoke;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.juxin.library.log.PLogger;
import com.juxin.library.observe.MsgMgr;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.bean.center.user.detail.UserInfo;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.WebActivity;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.ChineseFilter;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.MediaNotifyUtils;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.main.MainActivity;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * CMD操作统一处理
 * Created by ZRP on 2017/1/3.
 */
public class Invoker {

    // js cmd key
    public static final String JSCMD_visit_home = "visit_home";         // 返回自己的摇钱树
    public static final String JSCMD_visit_other = "visit_other";       // 进入他人摇钱树
    public static final String JSCMD_shake = "shake";                   // 摇动手机
    public static final String JSCMD_share_success = "share_success";   // QQ/微信客户端分享成功后通知js
    public static final String JSCMD_update_data = "update_data";       // 通知游戏刷新数据（充值，分享等可能要更新个人信息的操作后调用）
    public static final String JSCMD_game_guide = "game_guide";         // 游戏引导：event:“rob_other”引导的事件（rob_other为引导抢夺红包）
    public static final String JSCMD_cancel_dialog = "cancel_dialog";   // 取消cmd型对话框（取消分享，取消充值才发送）

    public static String JSCMD_cache_uid = "";//缓存的访问uid

    private WebAppInterface appInterface = new WebAppInterface(App.context, null);
    private Object webView;

    /**
     * @return 获取持有的WebAppInterface实例
     */
    public WebAppInterface getWebAppInterface() {
        return appInterface;
    }

    //--------------------CMD处理start--------------------

    private static class SingletonHolder {
        public static Invoker instance = new Invoker();
    }

    public static Invoker getInstance() {
        return SingletonHolder.instance;
    }

    private Invoke invoke = new Invoke();

    /**
     * 根据command和data执行对应方法（处理在app内情况）
     *
     * @param appInterface WebAppInterface实例
     * @param cmd          CMD操作码
     * @param data         操作执行的json字符串数据
     */
    public void doInApp(WebAppInterface appInterface, final String cmd, final String data) {
        PLogger.d("---doInApp--->cmd：" + cmd + "，data：" + data +
                "，AppMgr.isForground()：" + ModuleMgr.getAppMgr().isForeground());

        this.appInterface = (appInterface == null ? new WebAppInterface(App.context, null) : appInterface);
        webView = appInterface == null ? null : appInterface.getWebView();

        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Method notifyCMDMethod = Invoke.class.getMethod(cmd, String.class);
                    notifyCMDMethod.invoke(invoke, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 本地调用执行JS逻辑
     *
     * @param cmd  JS cmd命令
     * @param data 传递给JS的数据
     */
    public void doInJS(String cmd, Map<String, Object> data) {
        Map<String, Object> cmdMap = new HashMap<>();
        cmdMap.put("jcmd", cmd);
        cmdMap.put("data", data == null ? new HashMap<>() : data);
        String url = "javascript:window.platform.appCommand('" + new Gson().toJson(cmdMap) + "')";
        doInJS(url);
    }

    /**
     * 本地调用执行JS逻辑
     *
     * @param callbackName   回调方法名
     * @param callbackID     回调id
     * @param responseString 调用传值
     */
    public void doInJS(String callbackName, String callbackID, String responseString) {
        doInJS("javascript:" + callbackName + "(\'" + callbackID + "\',\'" + responseString + "\')");
    }

    /**
     * 本地调用执行JS逻辑
     *
     * @param loadUrl 调用执行的命令
     */
    public void doInJS(String loadUrl) {
        if (webView != null) {
            if (webView instanceof com.tencent.smtt.sdk.WebView) {
                ((com.tencent.smtt.sdk.WebView) webView).loadUrl(loadUrl);
                PLogger.d("---tencent x5--->" + loadUrl);
            } else if (webView instanceof android.webkit.WebView) {
                ((android.webkit.WebView) webView).loadUrl(loadUrl);
                PLogger.d("---webkit--->" + loadUrl);
            }
        }
    }

    /**
     * 设置webView，AAMainAct专用，在离开AAMainAct时设置webView为摇钱树webView
     *
     * @param webView
     */
    public void setWebView(Object webView) {
        this.webView = webView;
    }

    //--------------------CMD处理end--------------------

    public class Invoke {

        // 私聊指令
        public void cmd_open_chat(String data) {
            PLogger.d("cmd_open_chat: ------>" + data);
        }

        // 打开游戏页面
        // type:1   打开方式（1为侧滑页面，2为全屏页面，全屏时显示loading条）
        public void open_game_web(String data) {
            PLogger.d("---open_game_web--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            UIShow.showWebActivity(appInterface.getAct(), dataObject.optInt("type"), dataObject.optString("url"));
        }

        // 关闭loading页面
        public void hide_loading(String data) {
            PLogger.d("---hide_loading--->" + data);
            try {
                Activity act = appInterface.getAct();
                if (act != null && act instanceof WebActivity) {
                    ((WebActivity) act).hideLoading();
                }
                //缓存最后一条访问别人摇钱树的uid
                if (!TextUtils.isEmpty(JSCMD_cache_uid)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("target_id", JSCMD_cache_uid);
                    doInJS(JSCMD_visit_other, params);
                    JSCMD_cache_uid = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 播放音效，url为音频相对地址
        // 具体查看音乐地址文档-> http://test.game.xiaoyouapp.cn:30081/static/assets/cutfruit/config/sounds.json
        // 游戏名：fruit_ninja切水果      cashcow  红包来了
        public void play_sound(String data) {
            PLogger.d("---play_sound--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            //TODO 待配置文档
//            if ("cashcow".equalsIgnoreCase(dataObject.optString("game"))) {
//                PlayerPool.getInstance().playSound(AppCfg.ASet.getCashcow_asset_url() + dataObject.optString("url"));
//            } else {
//                if (dataObject.optInt("is_long") == 1) {//是否是长音频：0-不是，1-是
//                    PlayerPool.getInstance().playBg(AppCfg.ASet.getBase_url() + dataObject.optString("url"));
//                } else {
//                    PlayerPool.getInstance().playSound(AppCfg.ASet.getBase_url() + dataObject.optString("url"));
//                }
//            }
        }

        // 结束当前游戏页面
        public void do_finish(String data) {
            PLogger.d("---do_finish--->" + data);
            // [注意]：当前游戏页面必须是继承自BaseActivity.class
            Activity act = appInterface.getAct();
            if (act != null) act.finish();
        }

        // 获取当前用户的uid和auth
        public void get_app_data(String data) {
            PLogger.d("---get_app_data--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            UserInfo userInfo = ModuleMgr.getCenterMgr().getMyInfo();
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("uid", userInfo.getUid());
            responseObject.put("auth", URLEncoder.encode(ModuleMgr.getLoginMgr().getCookie()));
            responseObject.put("user_type", "8956".equals(ModuleMgr.getAppMgr().getMainChannelID()) ? 2 : 1);//只根据主渠道进行区分
            responseObject.put("gender", userInfo.getGender());
            responseObject.put("is_vip", userInfo.isVip());
            responseObject.put("version", 2);//1为之前的缘分吧版本（礼物版），2为红包来了之后的缘分吧版本

            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JsonUtil.mapToJSONString(responseObject));
        }

        // 根据uid获取用户信息
        public void get_user_info(String data) {
            PLogger.d("---get_user_info--->" + data);
            final JSONObject dataObject = JsonUtil.getJsonObject(data);
            //TODO
//            RequestHolder.getInstance().requestLiteUserInfo(dataObject.optString("uid"), new RequestHolder.OnRequestListener() {
//                @Override
//                public void onResult(String requestUrl, boolean isSuccess, String data) {
//                    if (isSuccess) {
//                        JSONObject content = JsonUtil.getJsonObject(data);
//                        Map<String, Object> responseObject = new HashMap<>();
//                        responseObject.put("uid", content.optString("uid"));
//                        responseObject.put("avatar", content.optString("avatar"));
//                        responseObject.put("avatar_status", content.optInt("avatar_status"));
//                        responseObject.put("nickname", content.optString("nickname"));
//                        responseObject.put("gender", content.optInt("gender"));
//                        responseObject.put("is_vip", content.optInt("ismonth") == 1);
//
//                        String responseString = JsonUtil.mapToJSONString(responseObject);
//                        doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), responseString);
//                    } else {
//                        doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), "{status:\"fail\"}");
//                    }
//                }
//            });
        }

        // 弹出模态对话框
        public void show_dialog(String data) {
            PLogger.d("---show_dialog--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            Activity act = appInterface.getAct();
            Activity context = (act == null ? (Activity) App.getActivity() : act);
            //展示游戏弹框
            switch (dataObject.optInt("code")) {
                case 101://弹出灵气不足弹框
                    break;
                case 104://弹出分享恢复灵气弹框
                    break;
                case 105://弹出添加好友分享弹框
                    break;
                case 106://分享获得哈士奇
                    break;
                case 201://弹出购买VIP弹框
                    break;
                case 202://获得斗牛犬，开通VIP弹框样式
                    break;
                case 301://弹出开通vip服务弹框：提示是高级场需要开通vip，下面只去掉赠送体力提示
                    break;
                case 401://弹出购买y币弹框
                    break;
            }
        }

        // 跳转到跟指定uid用户私聊界面
        public void jump_to_chat(String data) {
            PLogger.d("---jump_to_chat--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            Activity act = appInterface.getAct();
            //TODO
//            UIHelper.showMail_Chat_Act(act == null ? (Activity) App.getActivity() : act,
//                    String.valueOf(AppModel.getInstance().getUserDetail().getUid()),
//                    dataObject.optString("target_uid"));
        }

        // 侧滑出app页面
        public void jump_to_app(String data) {
            PLogger.d("---jump_to_app--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            //跳转到应用内页面
            Activity act = appInterface.getAct();
            Activity context = (act == null ? (Activity) App.getActivity() : act);
            //TODO
            switch (dataObject.optInt("code")) {
                case 11://跳转到灵气说明页面
                    break;
                case 21://跳转到亲密度说明页面
                    break;
                case 31://跳转到购买体力页面
                    break;
                case 41://跳转到掠夺记录页面
                    break;
            }
        }

        // 改变页面标题
        public void change_title(String data) {
            PLogger.d("---change_title--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            Activity act = appInterface.getAct();
            if (act != null && act instanceof BaseActivity) {
                try {// catch页面中可能未include base_title的情况
                    ((BaseActivity) act).setTitle(dataObject.optString("title"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 屏幕震动
        public void play_shock(String data) {
            PLogger.d("---play_shock--->" + data);
            MediaNotifyUtils.vibrator();
        }

        // 加密网络请求
        public void safe_request(String data) {
            PLogger.d("---safe_request--->" + data);
            cmdRequest(JsonUtil.getJsonObject(data), true);
        }

        // 普通网络请求
        public void normal_request(String data) {
            PLogger.d("---normal_request--->" + data);
            cmdRequest(JsonUtil.getJsonObject(data), false);
        }

        // 切换到主tab页
        public void enter_tab(String data) {
            PLogger.d("---enter_tab--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            // tab页面索引
            int index = 1;
            switch (dataObject.optInt("index")) {
                case 1:
                    index = FinalKey.MAIN_TAB_1;
                    break;
                case 2:
                    index = FinalKey.MAIN_TAB_2;
                    break;
                case 3:
                    index = FinalKey.MAIN_TAB_3;
                    break;
                case 4:
                    index = FinalKey.MAIN_TAB_4;
                    break;
            }
            Activity act = appInterface.getAct();
            if (act != null && act instanceof MainActivity) {
                ((MainActivity) act).changeTab(index, null);
            } else {
                UIShow.showMainWithTabData(act, index, null);
            }
        }

        // 获取设备信息
        public void getdevicemodel(String data) {
            PLogger.d("---getdevicemodel--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("devicemodel", android.os.Build.MODEL);//解密后的服务端返回（安卓注意大小写）

            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JsonUtil.mapToJSONString(responseObject));
        }

        // 进入别人的个人中心页面
        public void jump_to_userinfo(String data) {
            PLogger.d("---jump_to_userinfo--->" + data);
            final JSONObject dataObject = JsonUtil.getJsonObject(data);
            final Activity act = appInterface.getAct();
            //TODO
//            RequestHolder.getInstance().requestLiteUserInfo(dataObject.optString("target_uid"), new RequestHolder.OnRequestListener() {
//                @Override
//                public void onResult(String requestUrl, boolean isSuccess, String data) {
//                    if (isSuccess) {
//                        JSONObject jsonObject = JsonUtil.getJsonObject(data);
//                        UIHelper.showUserInfo(act == null ? (Activity) App.getActivity() : act,
//                                dataObject.optString("target_uid"), jsonObject.optString("nickname"),
//                                jsonObject.optInt("gender"));
//                    }
//                }
//            });
        }

        // 吐司提示
        public void show_toast(String data) {
            PLogger.d("---show_toast--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            MMToast.showShort(dataObject.optString("content"));
        }

        // 开启支付页面
        public void start_pay(String data) {
            PLogger.d("---start_pay--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            Activity act = appInterface.getAct();
            //TODO
//            UIHelper.showCMDPaymentAct(act == null ? (Activity) App.getActivity() : act, dataObject.optInt("pay_id"),
//                    dataObject.optString("desc"), dataObject.optString("money"));//money价格(元)
        }

        // 获取认证状态
        public void get_identify_status(String data) {
            PLogger.d("---get_identify_status--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            UserInfo userInfo = ModuleMgr.getCenterMgr().getMyInfo();
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("mobile_auth_status", userInfo.getMobileAuthStatus());
            responseObject.put("idcard_auth_status", userInfo.getIdcard_auth_status());
            responseObject.put("bank_auth_status", userInfo.getBankAuthStatus());
            responseObject.put("video_auth_status", userInfo.getVideoAuthStatus());

            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JsonUtil.mapToJSONString(responseObject));
        }

        // 刷新个人详情
        public void refresh_userdetail(String data) {
            PLogger.d("---refresh_userdetail--->" + data);
            ModuleMgr.getCenterMgr().reqMyInfo();
        }
    }

    /**
     * 游戏交互-请求转发
     *
     * @param dataObject JS传递的JSONObject
     * @param isEnc      是否为加密请求
     */
    private void cmdRequest(final JSONObject dataObject, boolean isEnc) {
        JSONObject bodyObject = JsonUtil.getJsonObject(dataObject.optString("body"));
        ModuleMgr.getCommonMgr().CMDRequest(dataObject.optString("method"), isEnc, dataObject.optString("url"),
                ChineseFilter.JSONObjectToMap(bodyObject), new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        PLogger.d("---cmdRequest--->" + response.getResponseString());
                        doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), response.getResponseString());
                    }
                });
    }
}
