package com.juxin.predestinate.module.local.center;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.juxin.library.observe.ModuleBase;
import com.juxin.library.utils.StringUtils;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import java.util.HashMap;

/**
 * 个人中心管理类
 */
public class CenterMgr implements ModuleBase {

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
    }
}