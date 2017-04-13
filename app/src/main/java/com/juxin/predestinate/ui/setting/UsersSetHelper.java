package com.juxin.predestinate.ui.setting;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.juxin.library.log.PSP;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;

import java.util.HashMap;

/**
 * Created YAO on 2017/3/29.
 */

public class UsersSetHelper {

    /**
     * editText光标靠右
     *
     * @param view
     */
    public static void setCursorRight(final EditText view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.requestFocus();
                view.setSelection(view.getText().length());
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view.requestFocus();
                view.setSelection(view.getText().length());
                return false;
            }
        });
    }

    interface OnHideInputListener {
        void clickOutSide();
    }

    ;

    /**
     * 软键盘显隐判断
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event, OnHideInputListener listener) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                listener.clickOutSide();
                return true;
            }
        }
        return false;
    }

    static Boolean SEEKBAR_ON;

    /**
     * 设置滑动条
     *
     * @param seekBar
     */
    public static void setSeekBar(final SeekBar seekBar) {
        if (ModuleMgr.getCenterMgr().getSetting().isStealth()) {
            SEEKBAR_ON = true;
            seekBar.setProgress(100);
        } else {
            SEEKBAR_ON = false;
            seekBar.setProgress(0);
        }
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    switch (v.getId()) {
                        case R.id.sb_stealth://一键隐身
                            HashMap<String,Object> post_param= new HashMap<>();
                            if (SEEKBAR_ON) {
                                SEEKBAR_ON = false;
                                ModuleMgr.getCenterMgr().getSetting().setStealth(1);
                                seekBar.setProgress(0);
                                post_param.put("stealth",1);
                            } else {
                                SEEKBAR_ON = true;
                                ModuleMgr.getCenterMgr().getSetting().setStealth(0);
                                seekBar.setProgress(100);
                                post_param.put("stealth",0);
                            }
                            ModuleMgr.getCenterMgr().updateSetting(post_param);
                            break;
                    }
                }
                return true;
            }
        });
    }
}
