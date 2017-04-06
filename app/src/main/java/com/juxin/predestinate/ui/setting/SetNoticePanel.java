package com.juxin.predestinate.ui.setting;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.config.Constant;


/**
 * 通知设置
 * Created YAO on 2016/5/27.
 */
public class SetNoticePanel extends BaseViewPanel implements View.OnTouchListener, View.OnClickListener{
    private SeekBar sb_stealth;
    // false是开true是开
    private Boolean Stealth_Status;
    private EditText et_voice_price, et_video_price;

    private String old_voice_price, old_video_price;
    private boolean voiceChange, videoChange;
    public SetNoticePanel(Context context) {
        super(context);
        setContentView(R.layout.p1_set_notice_panel);
        initView();
        InitPreference();
    }

    private void InitPreference() {
        if (PSP.getInstance().getBoolean(Constant.SETTING_STEALTH, Constant.SETTING_STEALTH_DEFAULT)) {
            Stealth_Status = true;
            sb_stealth.setProgress(100);
        } else {
            Stealth_Status = false;
            sb_stealth.setProgress(0);
        }
    }
    private void initView() {
        sb_stealth = (SeekBar) findViewById(R.id.sb_stealth);
        et_voice_price = (EditText) findViewById(R.id.et_voice_price);
        et_video_price = (EditText) findViewById(R.id.et_video_price);
        et_voice_price.setText("10");//TODO 获取当前设置的价格
        et_video_price.setText("20");
        et_voice_price.setSelection(et_voice_price.getText().length());
        et_video_price.setSelection(et_video_price.getText().length());

//        checkInput();
//        priceUpdate();
        sb_stealth.setOnTouchListener(this);
        priceWatcher();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.sb_stealth://一键隐身
                    if (Stealth_Status) {
                        Stealth_Status = false;
                        PSP.getInstance().put(Constant.SETTING_STEALTH, Stealth_Status);
                        //TODO 隐身请求
                        sb_stealth.setProgress(0);
                    } else {
                        Stealth_Status = true;
                        PSP.getInstance().put(Constant.SETTING_STEALTH, Stealth_Status);
                        //TODO 隐身请求
                        sb_stealth.setProgress(100);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private void priceWatcher() {
        et_voice_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                old_voice_price = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                voiceChange = !old_voice_price.equals(s);
            }
        });
        et_video_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                old_video_price = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                videoChange = !old_video_price.equals(s);
            }
        });
    }

    private void priceUpdate() {
        if (voiceChange) {
            PToast.showShort("更新语音价格");
            voiceChange = false;
        }
        if (videoChange) {
            PToast.showShort("更新视频价格");
            videoChange = false;
        }
    }

    private void checkInput() {
        if (TextUtils.isEmpty(et_voice_price.getText()) || Integer.parseInt(et_voice_price.getText().toString()) < 10) {
            et_voice_price.setText(old_voice_price);
            voiceChange = false;
        }
        if (TextUtils.isEmpty(et_video_price.getText()) || Integer.parseInt(et_video_price.getText().toString()) < 20) {
            et_video_price.setText(old_video_price);
            videoChange = false;
        }
    }

    private void open(Context context, View editText){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }
}
