package com.juxin.predestinate.module.local.login;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.utils.EncryptUtil;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.start.UP;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.BaseUtil;
import com.juxin.predestinate.module.util.NotificationsUtils;
import com.juxin.predestinate.module.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 登录逻辑处理
 * Created by ZRP on 2016/9/19.
 */
public class LoginMgr implements ModuleBase {

    private final static String LOGINMGR_UID = "LOGINMGR_UID";          // 保存当前登录用户账号信息 uid, pw
    private final static String LOGINMGR_COOKIE = "LOGINMGR_COOKIE";    // 保存当前登录用户cookie
    private final static String LOGINMGR_AUTH = "LOGINMGR_AUTH";        // 保存当前登录用户的密码md5

    public boolean IF_PW_RESET = false;                                 // 密码已是否重置
    public static String cookie = null;

    @Override
    public void init() {

    }

    @Override
    public void release() {

    }

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
        return PSP.getInstance().getString(LOGINMGR_COOKIE, "");
    }

    // ************************************ 登录用户信息列表存储 *****************************

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
        PSP.getInstance().put(LOGINMGR_COOKIE, cookie);
    }

    /**
     * @return 获取已登录过的所有用户ID和密码
     */
    private List<UP> getUserJson() {
        List<UP> upList = new ArrayList<>();
        try {
            String key = PSP.getInstance().getString(FinalKey.LOGIN_USER_KEY, null);
            if (!TextUtils.isEmpty(key)) {
                JSONObject jsonObject = new JSONObject(key);
                JSONArray jsonArray = jsonObject.optJSONArray("user");
                UP up;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    up = new UP();
                    up.setUid(Long.valueOf(EncryptUtil.decryptDES(object.optString("sUid"), FinalKey.UP_DES_KEY)));
                    up.setPw(EncryptUtil.decryptDES(object.optString("sPw"), FinalKey.UP_DES_KEY));
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
                tmpJson.put("sUid", EncryptUtil.encryptDES(String.valueOf(tmp.getUid()), FinalKey.UP_DES_KEY));
                tmpJson.put("sPw", EncryptUtil.encryptDES(String.valueOf(tmp.getPw()), FinalKey.UP_DES_KEY));
                tmpJson.put("tm", tmp.getTm());
                jsonArray.put(tmpJson);
            }
            jsonObject.put("user", jsonArray);
            PSP.getInstance().put(FinalKey.LOGIN_USER_KEY, jsonObject.toString());
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
     */
    public HTCallBack onRegister(UrlParam urlParam, final HashMap<String, Object> postParams, RequestComplete requestCallback) {
        postParams.put("client_type", 1); // 1为android 2为iphone
        postParams.put("suid", ModuleMgr.getAppMgr().getMainChannelID());
        postParams.put("ssid", ModuleMgr.getAppMgr().getSubChannelID());
        postParams.put("imei", TextUtils.isEmpty(ModuleMgr.getAppMgr().getIMEI()) ? "" : ModuleMgr.getAppMgr().getIMEI());
        postParams.put("imsi", TextUtils.isEmpty(ModuleMgr.getAppMgr().getIMSI()) ? "" : ModuleMgr.getAppMgr().getIMSI());
        postParams.put("mac", TextUtils.isEmpty(ModuleMgr.getAppMgr().getMAC()) ? "" : ModuleMgr.getAppMgr().getMAC());
        postParams.put("version", ModuleMgr.getAppMgr().getVerCode());
        postParams.put("pkgname", ModuleMgr.getAppMgr().getPackageName());
        HashMap<String, Object> getParams = new HashMap<>();
        getParams.put("vcode", new Random().nextLong());

        return ModuleMgr.getHttpMgr().reqPostNoCacheHttp(urlParam, getParams, postParams, requestCallback);
    }

    /**
     * 登录
     */
    public void onLogin(final Activity context, final long uid, final String pwd, RequestComplete requestCallback) {
        HashMap<String, Object> userAccount = new HashMap<>();
        userAccount.put("name", uid);
        userAccount.put("pwd", EncryptUtil.md5(pwd));
        LoadingDialog.show((FragmentActivity) context, context.getResources().getString(R.string.tip_loading_login));
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqLogin, userAccount, requestCallback);
    }

    /**
     * @return 获取cookie + vercode
     */
    public String getCookieVerCode() {
        String cookie = getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            return cookie + ";" + "v=" + ModuleMgr.getAppMgr().getVerCode();
        } else {
            clearCookie();
            return "v=" + ModuleMgr.getAppMgr().getVerCode();
        }
    }

    /**
     * 保存登录信息
     *
     * @param uid
     * @param password
     * @param cookie
     * @param isUserLogin
     */
    public void putAllLoginInfo(long uid, String password, String cookie, boolean isUserLogin) {
        setUid(uid + "");
        PSP.getInstance().put(LOGINMGR_AUTH, EncryptUtil.md5(password));
        putUserInfo(uid, password); //保存登录账户到list配置
        setCookie(cookie);//在setLoginInfo方法之前执行
        setLoginInfo(uid, isUserLogin);  //设置登录状态
    }

    /**
     * 设置登录信息，并发送登录信息。
     *
     * @param uid
     * @param isUserLogin 　进行登录请求或处于登录状态
     * @return
     */
    public boolean setLoginInfo(long uid, boolean isUserLogin) {
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
        MMLog.d("yao", "isLogin=" + App.isLogin + "==isUserLogin==" + isUserLogin);
        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Login, App.isLogin);
        return App.isLogin;
    }

    /**
     * 清除当前用户存储到cookies表中的所有数据
     */
    private void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 退出登录，并清空用户登录信息
     */
    public void logout() {
        removeCookie(App.context);
        NotificationsUtils.cancelAll();//如果还有通知栏提示，在退出帐号的时候全部清掉
        setUid("");//清空uid
        clearCookie();//在setLoginInfo方法之前执行
        setLoginInfo(0, true);
//        App.appState = App.AppState.AS_Service; //TODO
    }

    // **************************内部调用**************************

    /**
     * 登录后存储账号密码到list配置
     */
    private void putUserInfo(long uid, String pwd) {
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
        PSP.getInstance().put(LOGINMGR_UID, uid);
    }

    public long getUid() {
        String uid = PSP.getInstance().getString(LOGINMGR_UID, "");
        if (!TextUtils.isEmpty(uid)) {
            return BaseUtil.getLong(uid, 0);
        }
        return 0L;
    }

    /**
     * @return 获取保存的密码md5值
     */
    public String getAuth() {
        return PSP.getInstance().getString(LOGINMGR_AUTH, "");
    }

    /**
     * 初始化登陆信息
     */
    public void initCookie() {
        long uid = getUid();
        if (uid == 0) {
            clearCookie();
            return;
        }
        setLoginInfo(uid, true);
    }

    /**
     * 密码重置结果
     */
    public void setResetStatus(boolean resetStatus) {
        this.IF_PW_RESET = resetStatus;
    }

}
