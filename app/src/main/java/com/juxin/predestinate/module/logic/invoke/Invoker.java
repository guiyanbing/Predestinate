package com.juxin.predestinate.module.logic.invoke;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.utils.BitmapUtil;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.detail.UserInfo;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.local.location.LocationMgr;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.WebActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.config.Hosts;
import com.juxin.predestinate.module.logic.media.MediaMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.ChineseFilter;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.LiveHelper;
import com.juxin.predestinate.module.util.MediaNotifyUtils;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * CMD操作统一处理
 * Created by ZRP on 2017/1/3.
 */
public class Invoker {

    // js cmd key
    public static final String JSCMD_refresh_web = "refresh_web";//主动调用刷新web页面
    public static final String JSCMD_ranking_btn_click = "ranking_btn_click";// 风云榜按钮点击事件（本周上周切换）
    public static final String JSCMD_header_right_btn_click = "header_right_btn_click";// 导航条右侧按钮点击事件
    public static final String JSCMD_turntable_result = "turntable_result";// 转盘转动结果(同步他人客户端开始抽奖)
    public static final String JSCMD_chat_message = "chat_message";// 聊天信息
    public static final String JSCMD_gift_message = "gift_message";// 礼物信息
    public static final String JSCMD_turntable_activity = "turntable_activity";//启动关闭转盘

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
        String url = "javascript:window.platform.appCommand('" + JSON.toJSONString(cmdMap) + "')";
        doInJS(url);
    }

    /**
     * 本地调用执行JS逻辑：特殊情况主动调用，如base64图片传输，无需转码
     *
     * @param callbackName   回调方法名
     * @param callbackID     回调id
     * @param responseString 调用传值
     * @param isTranscode    是否进行字符串转码
     */
    public void doInJS(final String callbackName, final String callbackID, final String responseString, final boolean isTranscode) {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext(isTranscode ? ChineseFilter.toUnicode(responseString) : responseString);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                doInJS("javascript:" + callbackName + "(\'" + callbackID + "\',\'" + s + "\')");
            }
        });
    }

    /**
     * 本地调用执行JS逻辑，默认进行字符串转码，以支持中文字符串的传输
     *
     * @param callbackName   回调方法名
     * @param callbackID     回调id
     * @param responseString 是否进行字符串转码
     */
    public void doInJS(String callbackName, String callbackID, String responseString) {
        doInJS(callbackName, callbackID, responseString, true);
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
     * 设置webView，每次webView页面可见的时候重设
     *
     * @param webView WebView实例
     */
    public void setWebView(Object webView) {
        this.webView = webView;
        PLogger.d("------>" + String.valueOf(webView));
    }

    //--------------------CMD处理end--------------------

    public class Invoke {

        // 显示加载loading
        public void show_data_loading(String data) {
            PLogger.d("show_data_loading: ------>" + data);
            Activity act = appInterface.getAct();
            LoadingDialog.show((FragmentActivity) (act == null ? App.getActivity() : act));
        }

        // 关闭加载loading
        public void hide_data_loading(String data) {
            PLogger.d("hide_data_loading: ------>" + data);
            LoadingDialog.closeLoadingDialog(500);
        }

        // 私聊指令
        public void cmd_open_chat(String data) {
            PLogger.d("cmd_open_chat: ------>" + data);
            Activity act = appInterface.getAct();
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            UIShow.showPrivateChatAct(act == null ? App.getActivity() : act,
                    dataObject.optLong("uid"), dataObject.optString("nickname"));
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
                LoadingDialog.closeLoadingDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 播放音效，url为音频相对地址
        public void play_sound(String data) {
            PLogger.d("---play_sound--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            // 该版本无此cmd
            // dataObject.optString("game");
            // dataObject.optInt("is_long");//是否是长音频：0-不是，1-是
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

            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // 根据uid获取用户信息
        public void get_user_info(String data) {
            PLogger.d("---get_user_info--->" + data);
            final JSONObject dataObject = JsonUtil.getJsonObject(data);
            ModuleMgr.getCenterMgr().reqOtherInfo(dataObject.optLong("uid"), new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (!response.isOk()) return;

                    UserDetail userDetail = (UserDetail) response.getBaseData();
                    Map<String, Object> responseObject = new HashMap<>();
                    responseObject.put("uid", userDetail.getUid());
                    responseObject.put("avatar", userDetail.getAvatar());
                    responseObject.put("avatar_status", userDetail.getAvatar_status());
                    responseObject.put("nickname", userDetail.getNickname());
                    responseObject.put("gender", userDetail.getGender());
                    responseObject.put("is_vip", userDetail.isVip());

                    doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
                }
            });
        }

        // 获得用户自己的数据 （包含用户的账户余额等数据）
        public void get_user_detail(String data) {
            PLogger.d("---get_user_detail--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            UserInfo userInfo = ModuleMgr.getCenterMgr().getMyInfo();
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("uid", userInfo.getUid());
            responseObject.put("avatar", userInfo.getAvatar());
            responseObject.put("avatar_status", userInfo.getAvatar_status());
            responseObject.put("nickname", userInfo.getNickname());
            responseObject.put("gender", userInfo.getGender());
            responseObject.put("is_vip", userInfo.isVip());
            //responseObject.put("money", userInfo.getMoney());
            responseObject.put("longitude", LocationMgr.getInstance().getPointD().longitude);
            responseObject.put("latitude", LocationMgr.getInstance().getPointD().latitude);

            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
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
            UIShow.showPrivateChatAct(act == null ? App.context : act, dataObject.optLong("target_uid"), "");
        }

        // 侧滑出app页面
        public void jump_to_app(String data) {
            PLogger.d("---jump_to_app--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            //跳转到应用内页面
            Activity act = appInterface.getAct();
            Activity context = (act == null ? (Activity) App.getActivity() : act);
            switch (dataObject.optInt("code")) {
                case 501://跳转到话费领取页面
                    UIShow.showBillCollectionActivity(context);
                    break;
                case 502://跳转到Y币购买页面
                    UIShow.showBuyCoinActivity(context);
                    break;
                case 503://跳转到设置-活动相关页面
                    UIShow.showActionActivity(context);
                    break;
                default:
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
                case 5:
                    index = FinalKey.MAIN_TAB_5;
                    break;
                default:
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

            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // 进入别人的个人中心页面
        public void jump_to_userinfo(String data) {
            PLogger.d("---jump_to_userinfo--->" + data);
            final JSONObject dataObject = JsonUtil.getJsonObject(data);
            final Activity act = appInterface.getAct();
            UIShow.showCheckOtherInfoAct(act == null ? App.getActivity() : act, dataObject.optLong("target_uid"));
        }

        // 吐司提示
        public void show_toast(String data) {
            PLogger.d("---show_toast--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            PToast.showShort(dataObject.optString("content"));
        }

        // 开启支付页面
        public void start_pay(String data) {
            PLogger.d("---start_pay--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            Activity act = appInterface.getAct();
            UIShow.showPayListAct((FragmentActivity) (act == null ? App.getActivity() : act), dataObject.optInt("pay_id"));
        }

        // 获取认证状态
        public void get_identify_status(String data) {
            PLogger.d("---get_identify_status--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            UserInfo userInfo = ModuleMgr.getCenterMgr().getMyInfo();
            Map<String, Object> responseObject = new HashMap<>();
//            responseObject.put("mobile_auth_status", userInfo.getMobileAuthStatus());
//            responseObject.put("idcard_auth_status", userInfo.getIdcard_auth_status());
//            responseObject.put("bank_auth_status", userInfo.getBankAuthStatus());
//            responseObject.put("video_auth_status", userInfo.getVideoAuthStatus());

            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // 刷新个人详情
        public void refresh_userdetail(String data) {
            PLogger.d("---refresh_userdetail--->" + data);
            ModuleMgr.getCenterMgr().reqMyInfo();
        }

        // 照片选取：打开相册或相机，选取或拍摄完成之后回调图片的base64字符串给js(先进行图片质量压缩)
        public void get_image_data(String data) {
            PLogger.d("---get_image_data--->" + data);
            final JSONObject dataObject = JsonUtil.getJsonObject(data);
            Activity act = appInterface.getAct();
            ImgSelectUtil.getInstance().pickPhotoGallery(act == null ? App.context : act, new ImgSelectUtil.OnChooseCompleteListener() {
                @Override
                public void onComplete(String... path) {
                    if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) return;
                    PLogger.d("------>" + path[0]);
                    // 将选取的图片进行质量压缩并将二进制流转换为base64字符串
                    Map<String, Object> responseObject = new HashMap<>();
                    responseObject.put("imageData", BitmapUtil.imagePathToBase64(path[0]));//base64格式字符串
                    doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject), false);
                }
            });
        }

        // 图片数据转换成url：将图片上传服务器，并回调图片服务器地址给js
        public void image_data_to_url(String data) {
            PLogger.d("---image_data_to_url--->" + data);
            final JSONObject dataObject = JsonUtil.getJsonObject(data);
            final List<String> files = new LinkedList<>();
            JSONArray imageDataList = JsonUtil.getJsonArray(dataObject.optString("imageDataList"));
            for (int i = 0; i < imageDataList.length(); i++) {
                files.add(BitmapUtil.saveBitmap(BitmapUtil.decodeBase64(imageDataList.optString(i)),
                        DirType.getUploadDir() + System.currentTimeMillis() + "_" + i + ".jpg"));//将base64的数据保存成jpg文件
            }
            // 图片上传，上传完成之后删除本地存储的缓存文件
            int type = dataObject.optInt("type");
            ModuleMgr.getMediaMgr().sendHttpMultiFiles(Constant.UPLOAD_TYPE_PHOTO, 0, new MediaMgr.OnMultiFilesUploadComplete() {
                @Override
                public void onUploadComplete(ArrayList<String> mediaUrls) {
                    Map<String, Object> responseObject = new HashMap<>();
                    responseObject.put("urlList", mediaUrls.toArray());
                    doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
                    for (String s : files) FileUtil.deleteFile(s);
                }
            }, (String[]) files.toArray(new String[files.size()]));
        }

        // 跳转到钻石购买页面
        public void jump_to_shop(String data) {
            PLogger.d("---jump_to_shop--->" + data);
            Activity act = appInterface.getAct();
            UIShow.showGoodsDiamondAct(act == null ? App.context : act);
        }

        // 联系qq客服
        public void open_qq_service(String data) {
            PLogger.d("---open_qq_service--->" + data);
            Activity act = appInterface.getAct();
            UIShow.showQQService(act == null ? App.context : act);
        }

        // 获取用户绑定手机号
        public void get_phone_number(String data) {
            PLogger.d("---get_phone_number--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            UserInfo userInfo = ModuleMgr.getCenterMgr().getMyInfo();
            Map<String, Object> responseObject = new HashMap<>();
            //字符串 没有绑定 返回值空字符，绑定的返回手机号
            responseObject.put("num", userInfo.isVerifyCellphone() ? userInfo.getMobile() : "");
            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // 获取服务器请求url
        public void get_agent_url(String data) {
            PLogger.d("---get_agent_url--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            String url = Hosts.HOST_URL;
            switch (dataObject.optString("type")) {
                case "php":
                    url = Hosts.FATE_IT_HTTP;
                    break;
                case "go":
                    url = Hosts.FATE_IT_GO;
                    break;
                case "pay":
                    url = Hosts.FATE_IT_PROTOCOL;
                    break;
                case "image":
                    url = Hosts.FATE_IT_HTTP_PIC;
                    break;
                default:
                    break;
            }
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("url", url);
            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // H5页面用户行为统计
        public void user_behavior(String data) {
            PLogger.d("---user_behavior--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);

            Statistics.userBehavior(dataObject.optString("event_type"),
                    dataObject.optLong("to_uid"), dataObject.optString("event_data"));
        }

        // 跳转小秘书聊天页
        public void open_small_secretary(String data) {
            PLogger.d("---open_small_secretary--->" + data);
            Activity act = appInterface.getAct();
            UIShow.showPrivateChatAct(act == null ? App.context : act, MailSpecialID.customerService.getSpecialID(), "");
        }

        // H5页面用户行为统计
        public void open_live_view(String data) {
            PLogger.d("---open_live_view--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            LiveHelper.openLiveRoom(dataObject.optInt("type"), dataObject.optString("anchor_id"),
                    dataObject.optString("video_url"), dataObject.optString("image_url"),
                    dataObject.optString("download_url"), dataObject.optString("package_name"),
                    dataObject.optString("entrance"));
        }

        // ------------------------------游戏用cmd---------------------------------

        // 获取当前视频通话时长
        public void get_video_time(String data) {
            PLogger.d("---get_video_time--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("timespan", 0);//TODO 当前通话时长(秒数)
            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // 转盘开始转动
        public void turntable_start_rotate(String data) {
            PLogger.d("---turntable_start_rotate--->" + data);
        }

        // 转盘停止转动
        public void turntable_stop_rotate(String data) {
            PLogger.d("---turntable_stop_rotate--->" + data);
        }

        // 选择好友：app显示玩家列表，用户选择其中一个玩家，并回调其uid
        public void choose_friend(String data) {
            PLogger.d("---choose_friend--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            //TODO 客户端弹窗，选择用户之后回调js，以下内容在弹窗确认点击回调中实现
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("target_id", "");//TODO 被选择人uid
            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // 获取鱼分享：分享鱼。分享成功后，回调js
        public void get_fish_to_share(String data) {
            PLogger.d("---get_fish_to_share--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            long fishid = dataObject.optLong("fishid");//鱼的id，分享用

            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("share_success", true);//分享成功或者是失败//TODO 以下内容在分享成功回调中实现
            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }

        // 领取金币并分享：分享金币领取状态。分享成功后，回调js
        public void get_gold_to_share(String data) {
            PLogger.d("---get_gold_to_share--->" + data);
            JSONObject dataObject = JsonUtil.getJsonObject(data);
            int goldCount = dataObject.optInt("goldCount");//金币的个数

            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("share_success", true);//分享成功或者是失败//TODO 以下内容在分享成功回调中实现
            doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), JSON.toJSONString(responseObject));
        }
    }

    /**
     * 游戏交互-请求转发，判断是否为go服务器接口进行url-hash加密，
     *
     * @param dataObject    JS传递的JSONObject
     * @param isSafeRequest 是否是加密请求
     */
    private void cmdRequest(final JSONObject dataObject, boolean isSafeRequest) {
        JSONObject bodyObject = JsonUtil.getJsonObject(dataObject.optString("body"));
        String url = dataObject.optString("url");
        ModuleMgr.getCommonMgr().CMDRequest(dataObject.optString("method"), isSafeRequest, url,
                ChineseFilter.JSONObjectToMap(bodyObject), new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        PLogger.d("---cmdRequest--->" + response.getResponseString());
                        doInJS(dataObject.optString("callbackName"), dataObject.optString("callbackID"), response.getResponseString());
                    }
                });
    }
}
