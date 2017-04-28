package com.juxin.predestinate.ui.xiaoyou.wode.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;


/**
 * Created by IQQ on 2017/1/8.
 */

public class LimitedWrodTextview extends RelativeLayout {
    private EditText tv_edit;
    private TextView tv_hasnum;
    private Context context;
    private int limitNum = 50;
    private int hintsid = 0;

    public LimitedWrodTextview(Context context) {
        super(context);
        this.context = context;
        initView();
        initData();
    }

    public LimitedWrodTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initData();
    }

    public LimitedWrodTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initData();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.f1_limited_wrod_textview, this);
        tv_edit = (EditText) view.findViewById(R.id.ev_imput);
        tv_hasnum = (TextView) view.findViewById(R.id.tv_hasnum);
        tv_edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hintsid != 0)
                    if (hasFocus) {
                        tv_edit.setHint("");
                    } else {
                        tv_edit.setHint(hintsid);
                    }
            }
        });
    }

    private void setHasnum(int num) {
        tv_hasnum.setText(num + "/" + limitNum);
    }

    public void setHints(int stringid) {
        hintsid = stringid;
        tv_edit.setHint(stringid);
    }

    public String getText() {
        return tv_edit.getText().toString().trim();
    }

    private void setLimitNum(int num) {
        limitNum = num;
    }

    private void initData() {
        tv_edit.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                //int number = limitNum - s.length();
                setHasnum(s.length()); //修改为正计数
                selectionStart = tv_edit.getSelectionStart();
                selectionEnd = tv_edit.getSelectionEnd();
                if (temp.length() > limitNum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    tv_edit.setText(s);
                    tv_edit.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }
}
