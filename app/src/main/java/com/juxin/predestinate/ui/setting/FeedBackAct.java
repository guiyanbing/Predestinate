package com.juxin.predestinate.ui.setting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.CommonUtil;


/**
 * 意见反馈
 * Created YAO on 2017/3/20.
 */

public class FeedBackAct extends BaseActivity {
    private TextView tv_suggest_count;//建议字数计数器
    private TextView et_contact;//联系方式
    private EditText et_suggest;//建议
    private CharSequence temp;
    private int maxCount = 120;
    private int selectionStart;
    private int selectionEnd;
    private String contact, suggest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_feedback_act);
        setBackView(getResources().getString(R.string.title_feedback));
        setTitleRight(getResources().getString(R.string.title_right_submit), R.color.title_right_commit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSubmitInfo())
                    ModuleMgr.getCenterMgr().feedBack(contact, suggest, new RequestComplete() {
                        @Override
                        public void onRequestComplete(HttpResponse response) {
                            if (response.isOk()) {
                                PToast.showShort(getResources().getString(R.string.toast_suggest_ok));
                                back();
                            } else {
                                PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
                            }
                        }
                    });
            }
        });
        initView();
    }

    private void initView() {
        tv_suggest_count = (TextView) findViewById(R.id.tv_suggest_count);
        et_suggest = (EditText) findViewById(R.id.et_suggest);
        et_contact = (EditText) findViewById(R.id.et_contact);
        et_suggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                s.length() + "/120"
                tv_suggest_count.setText(String.format("%s/120", s.length()));
                selectionStart = et_suggest.getSelectionStart();
                selectionEnd = et_suggest.getSelectionEnd();
                if (temp.length() > maxCount) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    et_suggest.setText(s);
                    et_suggest.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }


    private boolean checkSubmitInfo() {
        suggest = et_suggest.getText().toString();
        if (TextUtils.isEmpty(suggest)) {
            PToast.showShort(getResources().getString(R.string.toast_suggest_isnull));
            return false;
        }
        contact = et_contact.getText().toString();
        if (TextUtils.isEmpty(contact)) {
            PToast.showShort(getResources().getString(R.string.toast_contact_isnull));
            return false;
        }
        return true;
    }
}
