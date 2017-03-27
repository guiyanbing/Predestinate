package com.juxin.predestinate.module.local.center;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.utils.StringUtils;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import java.util.HashMap;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.config.UrlParam;

import java.util.HashMap;

/**
 * 个人中心管理类
 */
public class CenterMgr implements ModuleBase {
    private static final String INFO_SAVE_KEY = "myInfo"; // 本地化个人资料key
    private UserDetail userDetail = null;

    @Override
    public void init() {
    }

    @Override
    public void release() {
    }


    /**
     * 请求手机验证码
     * @param mobile
     * @param complete
     */
    public void reqReqVerifyCode(String mobile, RequestComplete complete) {
        HashMap<String, Object> postparam = new HashMap<>();
        postparam.put("mobile", mobile);
        ModuleMgr.getHttpMgr().reqPostNoCacheHttp(UrlParam.reqReqVerifyCode, postparam, complete);
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
    public void setMyInfo(String resultStr) {
        PSP.getInstance().put(INFO_SAVE_KEY, resultStr);
    }
}