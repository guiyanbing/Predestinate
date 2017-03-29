package com.juxin.predestinate.ui.setting;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.juxin.library.log.PSP;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.config.Constant;

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

    interface OnHideInputListener{
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
    public static boolean isShouldHideInput(View v, MotionEvent event, OnHideInputListener listener ) {
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

    static Boolean SEEKBAR_ISON;

    interface OnSeekBarListener {
        void seekBarChange(boolean thum);
    }

    /**
     * 设置滑动条
     * @param seekBar
     * @param onSeekBarListener
     */
    public static void setSeekBar(final SeekBar seekBar, final OnSeekBarListener onSeekBarListener) {
        if (PSP.getInstance().getBoolean(Constant.SETTING_STEALTH, Constant.SETTING_STEALTH_DEFAULT)) {
            SEEKBAR_ISON = true;
            seekBar.setProgress(100);
        } else {
            SEEKBAR_ISON = false;
            seekBar.setProgress(0);
        }
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    switch (v.getId()) {
                        case R.id.sb_stealth://一键隐身
                            if (SEEKBAR_ISON) {
                                SEEKBAR_ISON = false;
                                PSP.getInstance().put(Constant.SETTING_STEALTH, SEEKBAR_ISON);
                                seekBar.setProgress(0);
                            } else {
                                SEEKBAR_ISON = true;
                                PSP.getInstance().put(Constant.SETTING_STEALTH, SEEKBAR_ISON);
                                seekBar.setProgress(100);
                            }
                            onSeekBarListener.seekBarChange(SEEKBAR_ISON);
                            break;
                    }
                }
                return true;
            }
        });
    }
}
