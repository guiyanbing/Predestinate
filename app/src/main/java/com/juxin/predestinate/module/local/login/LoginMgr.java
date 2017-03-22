package com.juxin.predestinate.module.local.login;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.bean.start.UP;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.Url_Enc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 登录逻辑处理
 * Created by ZRP on 2016/9/19.
 */
public class LoginMgr{
    private final static String UID = "sUid";                 // 保存当前登录用户账号信息 uid, pw
    private final static String USER_KEY = "user_key";        // 保存当前登录过的账号信息
    private final static String AUTH = "auth";                // 保存当前登录用户cookie
    public boolean IF_PW_RESET = false;                       // 密码已是否重置
    public static String cookie = null;
    public static boolean hasLogin = false;//是否已经登录

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
     * 登录
     *
     * @param hasJump 登录后是否需要跳转
     */
//    public void onLogin(final Activity context, final long uid, final String pwd, IReqComplete complete, final boolean hasJump) {
//        HashMap<String, Object> userAccount = new HashMap<>();
//        userAccount.put("username", uid);
//        //userAccount.put("password", MD5.encode(pwd));
//        //userAccount.put("platform", Constant.PLATFROM); // web:1, android:2, ios:3
//        userAccount.put("password", pwd);
//        userAccount.put("ms", 7); //1、支持语音 2、新机器人 3、新新机器人 4、支持视频 5、支持Y币 6、支持钻石、礼物 7、红包版本 8、红包来了单独APP
//
//        LoadingDialog.show((FragmentActivity) context, "正在登录，请稍候...");
//        reqPostOldNoCache(UrlParam.reqLogin, null, userAccount, new IReqComplete() {
//            @Override
//            public void onReqComplete(HttpResult result) {
//                LoadingDialog.closeLoadingDialog(500);
//                String jsonResult = result.getResultStr();
//                try {
//                    JSONObject jsonObject = new JSONObject(jsonResult);
//                    String respCode = jsonObject.optString("respCode");
//                    if ("success".equals(respCode)) {
//                        // Cookie 在headers中返回
//                        putAllLoginInfo(uid, pwd, true);
//
//                        // 临时资料设置
//                        JSONObject json = jsonObject.optJSONObject("user_info");
////                        ModuleMgr.getCenterMgr().getMyInfo().setNickname(json.optString("nickname"));
////                        ModuleMgr.getCenterMgr().getMyInfo().setUid(json.optLong("uid"));
//
//                        if (hasJump) {
////                            UIShow.showMainAct(context);
//                        }
//                    } else {
//                        PToast.showShort("登录失败，请稍候重试");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, complete);
//    }

    /**
     * 密码重置结果
     */
    public void setResetStatus(boolean resetStatus) {
        this.IF_PW_RESET = resetStatus;
    }
}
