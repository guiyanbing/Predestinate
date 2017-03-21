package com.juxin.predestinate.ui.setting;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.base.BaseActivity;


/**
 * 意见反馈
 * Created YAO on 2017/3/20.
 */

public class FeedBackAct extends BaseActivity{
    private TextView tv_suggest_count;//建议字数计数器
    private TextView et_contact;//联系方式
    private EditText et_suggest;//建议
    private CharSequence temp;
    private int maxCount = 120;
    private int selectionStart;
    private int selectionEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.feedback_act);
//        setBackView(R.id.back_view, false, "意见反馈", ContextCompat.getColor(this, R.color.text_zhuyao_black));
//        setTitleRight("提交", R.color.red, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////TODO 意见反馈请求
//            }
//        });
        initView();
    }

    private void initView() {
//        tv_suggest_count = (TextView) findViewById(R.id.tv_suggest_count);
//        et_suggest = (EditText) findViewById(R.id.et_suggest);
//        et_contact = (EditText) findViewById(R.id.et_contact);
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
                tv_suggest_count.setText(s.length() + "/120");
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

}
