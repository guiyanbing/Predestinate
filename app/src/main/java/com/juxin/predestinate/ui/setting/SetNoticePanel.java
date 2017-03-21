package com.juxin.predestinate.ui.setting;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.baseui.BaseViewPanel;


/**
 * 通知设置
 * Created YAO on 2016/5/27.
 */
public class SetNoticePanel extends BaseViewPanel implements View.OnTouchListener {
    private SeekBar sb_new_msg, sb_shaker, sb_voice, sb_stealth;
    // false是开true是开
    private Boolean Message_Status, Vibration_Status, Voice_Status, Stealth_Status;

    public SetNoticePanel(Context context) {
        super(context);
        setContentView(R.layout.y1_set_notice_panel);
        initView();
        InitPreference();
    }

    private void InitPreference() {
//        if (ModuleMgr.getCfgMgr().getBoolean(Constant.SETTING_MESSAGE, true)) {
//            Message_Status = true;
//            sb_new_msg.setProgress(100);
//        } else {
//            Message_Status = false;
//            sb_new_msg.setProgress(0);
//        }
//
//        if (ModuleMgr.getCfgMgr().getBoolean(Constant.SETTING_VIBRATION, true)) {
//            Vibration_Status = true;
//            sb_shaker.setProgress(100);
//        } else {
//            Vibration_Status = false;
//            sb_shaker.setProgress(0);
//        }
//
//        if (ModuleMgr.getCfgMgr().getBoolean(Constant.SETTING_VOICE, true)) {
//            Voice_Status = true;
//            sb_voice.setProgress(100);
//        } else {
//            Voice_Status = false;
//            sb_voice.setProgress(0);
//        }
//
//        if (ModuleMgr.getCfgMgr().getBoolean(Constant.SETTING_STEALTH, true)) {
//            Stealth_Status = true;
//            sb_stealth.setProgress(100);
//        } else {
//            Stealth_Status = false;
//            sb_stealth.setProgress(0);
//        }
    }

    private void initView() {
        sb_new_msg = (SeekBar) findViewById(R.id.sb_new_msg);
        sb_shaker = (SeekBar) findViewById(R.id.sb_shaker);
        sb_voice = (SeekBar) findViewById(R.id.sb_voice);
        sb_stealth = (SeekBar) findViewById(R.id.sb_stealth);
        sb_new_msg.setOnTouchListener(this);
        sb_shaker.setOnTouchListener(this);
        sb_voice.setOnTouchListener(this);
        sb_stealth.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
//            switch (v.getId()) {
//                case R.id.sb_new_msg://接收新消息
//                    if (Message_Status) {
//                        Message_Status = false;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_MESSAGE, Message_Status);
//                        sb_new_msg.setProgress(0);
//                    } else {
//                        Message_Status = true;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_MESSAGE, Message_Status);
//                        sb_new_msg.setProgress(100);
//                    }
//                    break;
//                case R.id.sb_shaker://震动提醒
//                    if (Vibration_Status) {
//                        Vibration_Status = false;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_VIBRATION, Vibration_Status);
//                        sb_shaker.setProgress(0);
//                    } else {
//                        Vibration_Status = true;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_VIBRATION, Vibration_Status);
//                        sb_shaker.setProgress(100);
//                    }
//                    break;
//                case R.id.sb_voice://声音提醒
//                    if (Voice_Status) {
//                        Voice_Status = false;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_VOICE, Voice_Status);
//                        sb_voice.setProgress(0);
//                    } else {
//                        Voice_Status = true;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_VOICE, Voice_Status);
//                        sb_voice.setProgress(100);
//                    }
//                    break;
//                case R.id.sb_stealth://一键隐身
//                    if (Stealth_Status) {
//                        Stealth_Status = false;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_STEALTH, Stealth_Status);
//                        sb_stealth.setProgress(0);
//                    } else {
//                        Stealth_Status = true;
//                        ModuleMgr.getCfgMgr().setBoolean(Constant.SETTING_STEALTH, Stealth_Status);
//                        sb_stealth.setProgress(100);
//                    }
//                    break;
//            }
        }
        return true;
    }
}
