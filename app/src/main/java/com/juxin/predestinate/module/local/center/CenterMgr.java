package com.juxin.predestinate.module.local.center;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.utils.StringUtils;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.ui.start.FindPwdAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 个人中心管理类
 */
public class CenterMgr implements ModuleBase, PObserver {

    private static final String INFO_SAVE_KEY = "INFO_SAVE_KEY"; // 本地化个人资料key
    private UserDetail userDetail = null;

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_App_Login:
                PLogger.d("---MT_App_Login--->" + value);
                if ((Boolean) value) {
                    IMProxy.getInstance().connect();//登录成功之后连接socket
                    reqMyInfo();// 请求个人资料
                } else {
                    IMProxy.getInstance().logout();//退出登录的时候退出socket
                    userDetail = null;
                    setMyInfo(null);
                }
                break;
            case MsgType.MT_App_CoreService://socket已连接，登录
                IMProxy.getInstance().login();
                break;
        }
    }

    /**
     * 请求手机验证码
     *
     * @param mobile
     * @param complete
     * @param tag      301-小友密码重置, 302-小友手机认证
     */
    public void reqVerifyCode(String mobile, RequestComplete complete, int tag) {
        HashMap<String, Object> postparam = new HashMap<>();
        postparam.put("mobile", mobile);
        postparam.put("tag", tag);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqReqVerifyCode, postparam, complete);
    }

    /**
     * 找回密码
     *
     * @param mobile   手机号
     * @param password 新密码
     * @param code     验证码
     * @param complete
     */
    public void resetPassword(String mobile, String password, String code, RequestComplete complete) {
        HashMap<String, Object> postparam = new HashMap<>();
        postparam.put("phone", mobile);
        postparam.put("password", password);
        postparam.put("code", code);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.resetPassword, postparam, complete);
    }

    /**
     * 手机认证
     *
     * @param uid
     * @param mobile   手机号
     * @param password 新密码
     * @param code     验证码
     * @param complete
     */
    public void mobileAuth(long uid, String mobile, String password, String code, RequestComplete complete) {
        HashMap<String, Object> postparam = new HashMap<>();
        postparam.put("uid", uid);
        postparam.put("mobile", mobile);
        postparam.put("pwd", password);
        postparam.put("code", code);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.mobileAuth, postparam, complete);
    }

    /**
     * 意见反馈
     * @param contract 联系方式
     * @param views 意见
     * @param complete
     */
    public void feedBack(String contract, String views, RequestComplete complete) {
        HashMap<String, Object> postparam = new HashMap<>();
        postparam.put("contract", contract);
        postparam.put("views", views);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.feedBack, postparam, complete);
    }

    /**
     * 昵称：个人资料限制昵称最大字数
     * 过滤空格 禁止注册的时候 昵称带有空格
     *
     * @param edit
     */
    public void inputFilterSpace(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {
            int cou = 0;
            int selectionEnd = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cou = before + count;
                String editable = edit.getText().toString();
                String str = StringUtils.noSpace(editable);
                if (!editable.equals(str)) {
                    edit.setText(str);
                }
                edit.setSelection(edit.length());
                cou = edit.length();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cou > 7) {
                    selectionEnd = edit.getSelectionEnd();
                    s.delete(7, selectionEnd);
                    if (!TextUtils.isEmpty(s)) {
                        edit.setText(s.toString());
                    }
                }
            }
        });
    }

    /**
     * 获取我的个人资料
     */
    public UserDetail getMyInfo() {
        if (userDetail == null) {
            userDetail = new UserDetail();
            String result = PSP.getInstance().getString(INFO_SAVE_KEY, "");
            if (!TextUtils.isEmpty(result)) {
                userDetail.parseJson(result);
            }
        }
        return userDetail;
    }

    /**
     * 获取自己的个人资料
     */
    public void reqMyInfo() {
        ModuleMgr.getHttpMgr().reqGetAndCacheHttp(UrlParam.reqMyInfo, null, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    userDetail = (UserDetail) response.getBaseData();

                    // 持久化必须放在这里，不能放在UserDetail解析里
                    try {
                        JSONObject json = new JSONObject(response.getResponseString());
                        setMyInfo(json.optString("res"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取他人用户详细信息
     */
    public void reqOtherInfo(final long uid, RequestComplete complete) {
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqOtherInfo, new HashMap<String, Object>() {
            {
                put("uid", uid);
            }
        }, complete);
    }

    /**
     * 保存个人信息Json串到SP
     */
    private void setMyInfo(String resultStr) {
        PSP.getInstance().put(INFO_SAVE_KEY, resultStr);
    }
}