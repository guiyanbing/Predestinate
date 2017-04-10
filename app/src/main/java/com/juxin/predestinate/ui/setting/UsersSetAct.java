package com.juxin.predestinate.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.mumu.bean.utils.DirUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;

import java.lang.ref.WeakReference;

/**
 * 设置
 * Created YAO on 2016/5/27.
 */
public class UsersSetAct extends BaseActivity implements View.OnClickListener {
    private TextView desc_clean;
    private static String cache;
    private MyHandler handler;
    private EditText et_voice_price, et_video_price;
    private SeekBar sb_stealth;
    // false是开true是开
    private Boolean Stealth_Status;

    private String old_voice_price, old_video_price;
    private boolean voiceChange, videoChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.p1_users_set);
        setBackView("设置");
        initView();
    }

    static class MyHandler extends Handler {
        final WeakReference<UsersSetAct> mActivity;

        MyHandler(UsersSetAct act) {
            mActivity = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            UsersSetAct act = mActivity.get();
            switch (msg.what) {
                case 1:
                    act.desc_clean.setText(cache);
                    break;
                case 2:
                    act.desc_clean.setText("缓存状态良好");
                    if (cache.equals("缓存状态良好")) {
                        PToast.showShort("缓存状态良好，不需要清理");
                    } else {
                        PToast.showShort("缓存已清除");
                    }
                    break;
            }
        }
    }

    private void initView() {
        handler = new MyHandler(this);
        findViewById(R.id.rl_exit).setOnClickListener(this);
        findViewById(R.id.rl_clean).setOnClickListener(this);
        findViewById(R.id.rl_feedback).setOnClickListener(this);
        desc_clean = (TextView) findViewById(R.id.desc_clean);
        sb_stealth = (SeekBar) findViewById(R.id.sb_stealth);
        et_voice_price = (EditText) findViewById(R.id.et_voice_price);
        et_video_price = (EditText) findViewById(R.id.et_video_price);
        et_voice_price.setText("10");//TODO 获取当前设置的价格
        et_video_price.setText("20");
        UsersSetHelper.setCursorRight(et_voice_price);
        UsersSetHelper.setCursorRight(et_video_price);
        UsersSetHelper.setSeekBar(sb_stealth, new UsersSetHelper.OnSeekBarListener() {
            @Override
            public void seekBarChange(boolean thum) {
                if (thum) {
                    //隐身
                } else {
                    //非隐身
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                cache = DirUtils.getFormtCacheSize();
                handler.sendEmptyMessage(1);
            }
        }).start();
        priceWatcher();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_exit://退出登录
                PickerDialogUtil.showSimpleAlertDialog(UsersSetAct.this, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        ModuleMgr.getLoginMgr().logout();
                        setResult(200);
                        finish();
                    }
                }, "确定退出登录吗？", R.color.text_zhuyao_black, "");
                break;
            case R.id.rl_clean://  清除缓存
                PickerDialogUtil.showSimpleTipDialogExt(UsersSetAct.this, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        new Thread() {
                            public void run() {
                                Message msg = new Message();
                                try {
                                    DirUtils.clearCache();
                                    msg.what = 2;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    msg.what = -1;
                                }
                                handler.sendMessage(msg);
                            }
                        }.start();
                    }
                }, "根据缓存文件的大小，清理时间将持续几秒至几分钟不等，请耐心等待", "清除缓存", "取消", "清理", true, R.color.text_zhuyao_black);
                break;
            case R.id.rl_feedback://意见反馈客服
//                UIShow.showPrivateChatAct(this, MailSpecialID.customerService.getSpecialID(), MailSpecialID.customerService.getSpecialIDName());
//                UIShow.showFeedBackAct(this);
                UIShow.showFeedBackAct(this);
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            boolean isHide = UsersSetHelper.isShouldHideInput(v, ev, new UsersSetHelper.OnHideInputListener() {
                @Override
                public void clickOutSide() {
                    checkInput();
                    priceUpdate();
                    et_voice_price.clearFocus();
                    et_video_price.clearFocus();
                }
            });
            if (isHide) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    //价格是否变动检测
    private void priceWatcher() {
        et_voice_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    old_voice_price = et_voice_price.getText().toString();
                }
            }
        });
        et_video_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    old_video_price = et_video_price.getText().toString();
                }
            }
        });
        et_voice_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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

    //价格更新请求
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

    //价格输入规范检查
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
}
