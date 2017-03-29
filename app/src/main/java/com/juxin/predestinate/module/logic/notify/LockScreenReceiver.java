/**
 *
 */
package com.juxin.predestinate.module.logic.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.juxin.library.log.PLogger;

/**
 * 锁屏状态监听receiver
 */
public class LockScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                LockScreenMgr.getInstance().reenableKeyguard();
                LockScreenMgr.getInstance().popupActivity(false);
            }

            PLogger.d("---LockScreenReceiver--->" + action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
