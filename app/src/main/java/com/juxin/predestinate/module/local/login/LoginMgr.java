package com.juxin.predestinate.module.local.login;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.juxin.library.enc.MD5;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.mumu.bean.message.Msg;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.message.MsgType;
import com.juxin.predestinate.bean.start.UP;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.request.RequestParam;
import com.juxin.predestinate.module.util.JniUtil;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.Url_Enc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 登录逻辑处理
 * Created by ZRP on 2016/9/19.
 */
public class LoginMgr {
    private final static String UID = "sUid";                 // 保存当前登录用户账号信息 uid, pw
    private final static String USER_KEY = "user_key";        // 保存当前登录过的账号信息
    private final static String AUTH = "auth";                // 保存当前登录用户cookie
    public boolean IF_PW_RESET = false;                       // 密码已是否重置
    public static String cookie = null;
    public static boolean hasLogin = false;//是否已经登录


    /**
     * @return 判断Cookie是否存在
     */
    public boolean checkAuthIsExist() {
        return !TextUtils.isEmpty(getCookie());
    }

    /**
     * @return 获取cookie
     */
    public String getCookie() {
        return PSP.getInstance().getString(AUTH, "");
    }

    //************************************ 登录用户信息列表存储 *****************************\\
    private ArrayList<UP> userList;  // 维护登录过的用户数据列表，防止多次进行SP读写操作

    /**
     * 清空本地Cookie
     */
    public void clearCookie() {
        setCookie("");
    }

    /**
     * 保存cookie
     */
    public void setCookie(String cookie) {
        PSP.getInstance().put(AUTH, cookie);
    }

    /**
     * @return 获取已登录过的所有用户ID和密码
     */
    private List<UP> getUserJson() {
        List<UP> upList = new ArrayList<>();
        try {
            String key = PSP.getInstance().getString(USER_KEY, null);
            if (!TextUtils.isEmpty(key)) {
                JSONObject jsonObject = new JSONObject(key);
                JSONArray jsonArray = jsonObject.optJSONArray("user");
                UP up;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    up = new UP();
                    up.setUid(Long.valueOf(Url_Enc.decryptDES(object.optString("sUid"))));
                    up.setPw(Url_Enc.decryptDES(object.optString("sPw")));
                    up.setTm(object.optLong("tm"));
                    upList.add(up);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upList;
    }

    /**
     * 获取用户列表
     */
    public List<UP> getUserList() {
        if (userList == null) {
            userList = new ArrayList<>();
            userList.addAll(getUserJson());
        }
        return userList;
    }

    /**
     * 登录后存储账号密码list到本地Json
     *
     * @return true：存储成功
     */
    private boolean putUserJson(List<UP> upList) {
        userList = null;  // 更新本地userList;
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject tmpJson;
            for (UP tmp : upList) {
                tmpJson = new JSONObject();
                tmpJson.put("sUid", Url_Enc.encryptDES(String.valueOf(tmp.getUid())));
                tmpJson.put("sPw", Url_Enc.encryptDES(String.valueOf(tmp.getPw())));
                tmpJson.put("tm", tmp.getTm());
                jsonArray.put(tmpJson);
            }
            jsonObject.put("user", jsonArray);
//            ModuleMgr.getCfgMgr().setString(USER_KEY, jsonObject.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除登录用户
     *
     * @return 被移除的用户
     */
    public UP removeLoginUser(int position) {
        UP user = getUserList().remove(position);
        if (getUserList().size() >= 0) {
            Collections.sort(getUserList());
            putUserJson(getUserList());
        }
        return user;
    }

    /**
     * 账号注册
     * <p>
     * 三方注册时简化绑定步骤，防止因网络原因造成的不确定问题
     * <p>
     *
     * @param urlParam UrlParam.reqRegister : 常规注册  UrlParam.reqThridRegister : 三方账号注册
     */
    public HTCallBack onRegister(final Activity context, UrlParam urlParam, final HashMap<String, Object> postParams, RequestComplete requestCallback) {
//        postParams.put("flag", 0);  // 0缘分吧 1爱爱 2同城快约 3附近秘约 标记
        postParams.put("user_client_type", 2); // 2为android 3为iphone
        postParams.put("s_uid", ModuleMgr.getAppMgr().getMainChannelID());
        postParams.put("s_sid", ModuleMgr.getAppMgr().getSubChannelID());
        postParams.put("imei", TextUtils.isEmpty(ModuleMgr.getAppMgr().getIMEI()) ? "" : ModuleMgr.getAppMgr().getIMEI());
        postParams.put("imsi", TextUtils.isEmpty(ModuleMgr.getAppMgr().getIMSI()) ? "" : ModuleMgr.getAppMgr().getIMSI());
        postParams.put("mac", TextUtils.isEmpty(ModuleMgr.getAppMgr().getMAC()) ? "" : ModuleMgr.getAppMgr().getMAC());
        postParams.put("simoperator", TextUtils.isEmpty(ModuleMgr.getAppMgr().getSimOperator()) ? "" : ModuleMgr.getAppMgr().getSimOperator());
        postParams.put("flag", 1);
//        postParams.put("ms", 7); //1、支持语音 2、新机器人 3、新新机器人 4、支持视频 5、支持Y币 6、支持钻石、礼物 7、红包版本 8、红包来了单独APP
        long randNum = new Random().nextLong();
        HashMap<String, Object> getParams = new HashMap<>();
        getParams.put("vcode", randNum);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("User-Agent", "");
        RequestParam requestParam = new RequestParam();
        requestParam.setCacheType(RequestParam.CacheType.CT_Cache_No);
        requestParam.setUrlParam(urlParam);
        requestParam.setHead_param(headerMap);
        requestParam.setPost_param(postParams);
        requestParam.setGet_param(getParams);
        requestParam.setRequestCallback(requestCallback);
        requestParam.setLogicCallBack(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                // 暂时此处进行xxtea解密，看后续能否统一处理
                String jsonResult = JniUtil.GetDecryptString(response.getData().toString());//TODO 待替换
                if (TextUtils.isEmpty(jsonResult)) PToast.showShort("注册失败");

                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String respCode = jsonObject.optString("respCode");
                    if ("success".equals(respCode)) {
                        JSONObject accountObject = jsonObject.optJSONObject("user_account");
//                        ModuleMgr.getCenterMgr().getMyInfo().setUid(accountObject.optLong("uid"));
//                        ModuleMgr.getCenterMgr().getMyInfo().setNickname(postParams.get("nickname") + "");
//                        ModuleMgr.getCenterMgr().getMyInfo().setScity(accountObject.optInt("city"));
//                        ModuleMgr.getCenterMgr().getMyInfo().setSprovince(accountObject.optInt("province"));

                        putAllLoginInfo(accountObject.optLong("uid"), accountObject.optString("password"), false);
                        UIShow.showMainClearTask(context);
                    } else {
                        PToast.showShort("注册失败，请稍候重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return ModuleMgr.getHttpMgr().request(requestParam);
    }

    /**
     * 登录
     *
     * @param hasJump 登录后是否需要跳转
     */
    public void onLogin(final Activity context, final long uid, final String pwd, RequestComplete requestCallback, final boolean hasJump) {
        HashMap<String, Object> userAccount = new HashMap<>();
        userAccount.put("username", uid);
        userAccount.put("password", MD5.encode(pwd));
        //userAccount.put("platform", Constant.PLATFROM); // web:1, android:2, ios:3

        LoadingDialog.show((FragmentActivity) context, "正在登录，请稍候...");

        RequestParam requestParam = new RequestParam();
        requestParam.setCacheType(RequestParam.CacheType.CT_Cache_No);
        requestParam.setUrlParam(UrlParam.reqLogin);
        requestParam.setPost_param(userAccount);
        requestParam.setRequestCallback(requestCallback);
        requestParam.setLogicCallBack(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                LoadingDialog.closeLoadingDialog(500);
                //TODO 待替换
                String jsonResult = response.getData().toString();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String respCode = jsonObject.optString("respCode");
                    if ("success".equals(respCode)) {
                        // Cookie 在headers中返回
                        putAllLoginInfo(uid, pwd, true);

                        // 临时资料设置
                        JSONObject json = jsonObject.optJSONObject("user_info");
//                        ModuleMgr.getCenterMgr().getMyInfo().setNickname(json.optString("nickname"));
//                        ModuleMgr.getCenterMgr().getMyInfo().setUid(json.optLong("uid"));

                        if (hasJump) {
//                            UIShow.showMainAct(context);
                        }
                    } else {
                        PToast.showShort("登录失败，请稍候重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ModuleMgr.getHttpMgr().request(requestParam);
    }
    // ************************************内部调用************************** \\

    /**
     * 保存登录信息
     */
    private void putAllLoginInfo(long uid, String password, boolean isUserLogin) {
        setUid(uid + "");
        putUserinfo(uid, password); //保存登录账户到list配置
        setLoginInfo(uid, isUserLogin);  //设置登录状态
    }

    /**
     * 设置登录信息，并发送登录信息。
     */
    public boolean setLoginInfo(long uid, boolean isUserLogin) {
        Msg msg = new Msg();
        App.cookie = getCookie();

        if (isUserLogin) {
            if (TextUtils.isEmpty(App.cookie) || uid == 0) {
                App.uid = 0;
                App.isLogin = false;
            } else {
                App.uid = uid;
                App.isLogin = true;
            }
        } else {   // 注册成功
            App.uid = uid;
            App.isLogin = true;
        }
        msg.setData(App.isLogin);
        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Login, msg);
        return App.isLogin;
    }

    /**
     * 登录后存储账号密码到list配置
     */
    private void putUserinfo(long uid, String pwd) {
        List<UP> list = getUserJson();
        if (list == null) {
            list = new ArrayList<>();
            list.add(new UP(uid, pwd, TimeUtil.getTimeInMillis()));
        } else {
            boolean b = false;
            for (int i = 0; i < list.size(); i++) {
                UP up = list.get(i);
                if (up != null && up.getUid() != 0 && up.getUid() == uid) {
                    up.setPw(pwd);
                    up.setTm(TimeUtil.getTimeInMillis());
                    b = true;
                }
            }
            if (!b) {//新帐号
                list.add(new UP(uid, pwd, TimeUtil.getTimeInMillis()));
            }
        }
        if (list.size() != 0) {
            Collections.sort(list);
            putUserJson(list);
        }
    }

    /**
     * 保存单独的 uid
     *
     * @param uid
     */
    public void setUid(String uid) {
        PSP.getInstance().put(UID, uid);
    }

    /**
     * 密码重置结果
     */
    public void setResetStatus(boolean resetStatus) {
        this.IF_PW_RESET = resetStatus;
    }

}
