package com.juxin.predestinate.ui.setting;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 意见反馈
 *
 * @author xy
 */
public class Suggest_Act extends BaseActivity {

    private EditText sendtext;
    private EditText sendqq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_activity_suggest);
        setBackView("意见反馈");
        initView();
    }


    private void initView() {
        sendtext = (EditText) this.findViewById(R.id.edt_suggest_value);
        sendqq = (EditText) this.findViewById(R.id.edt_suggest_qq);
        findViewById(R.id.btn_suggest_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submitsuggest();
            }
        });
    }


    private void Submitsuggest() {
        String suggesttext = sendtext.getText().toString();
        String qq = sendqq.getText().toString();
        if (suggesttext.equals("") || suggesttext == null) {
            PToast.showShort("请先填写意见再提交。");
            sendtext.setFocusable(true);
            sendtext.setFocusableInTouchMode(true);
            sendtext.requestFocus();
            return;
        }
        if (qq.equals("") || qq == null) {
            PToast.showShort("请留下您的联系方式。");
            sendqq.setFocusable(true);
            sendqq.setFocusableInTouchMode(true);
            sendqq.requestFocus();
            return;
        }

        ModuleMgr.getCenterMgr().feedBack(qq, suggesttext, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.getResponseString());

                    if (!"true".equals(jsonObject.optString("item"))) {
                        PToast.showShort("操作失败，请稍候再试");
                    } else {
                        PToast.showLong("提交成功。");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
