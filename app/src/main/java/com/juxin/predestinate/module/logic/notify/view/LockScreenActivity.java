package com.juxin.predestinate.module.logic.notify.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.notify.LockScreenMgr;

/**
 * 锁屏弹窗弹出的activity
 */
public class LockScreenActivity extends BaseActivity {

    private ViewGroup viewGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.common_lockscreen);

        initView();
    }

    private void initView() {
        viewGroup = (ViewGroup) findViewById(R.id.lockscreen_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            View view = viewGroup.getChildAt(0);
            View newView = LockScreenMgr.getInstance().getLockView(onLockScreenCallback);
            if (newView != null) {
                if (newView != view) {
                    viewGroup.removeAllViews();
                    viewGroup.addView(newView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        viewGroup.removeAllViews();
        super.onDestroy();
    }

    private LockScreenMgr.OnLockScreenCallback onLockScreenCallback = new LockScreenMgr.OnLockScreenCallback() {
        @Override
        public void closePopupActivity() {
            try {
                LockScreenActivity.this.finish();
            } catch (Exception e) {
                MMLog.printThrowable(e);
            }
        }

        @Override
        public void disableKeyguard() {
            try {
                LockScreenMgr.getInstance().disableKeyguard();
            } catch (Exception e) {
                MMLog.printThrowable(e);
            }
        }
    };
}
