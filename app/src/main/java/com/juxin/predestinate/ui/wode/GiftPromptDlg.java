package com.juxin.predestinate.ui.wode;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.ui.wode.view.LimitedWrodTextview;

public class GiftPromptDlg extends Dialog implements RequestComplete {

    public static String MY_GETGIFT_TIME = "OLDTIME";//时间键

    private TextView contentText;
    private LimitedWrodTextview lwt;
    private TextView tv_ok;
    private Context context;
    private View view;

    protected GiftPromptDlg(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public GiftPromptDlg(Context context) {
        this(context, R.style.dialog);
    }

    public GiftPromptDlg(Context context, int theme) {
        super(context, theme);
        initView(context);
        this.context = context;
    }


    private void initView(final Context context) {
        PSP.getInstance().put(MY_GETGIFT_TIME + ModuleMgr.getCenterMgr().getMyInfo().getUid(), ModuleMgr.getAppMgr().getTime());
        view = LayoutInflater.from(context).inflate(R.layout.f1_want_gift_dlg, null);
        setContentView(view);

        lwt = (LimitedWrodTextview) findViewById(R.id.dlg_wangmoney_lwt_main);
        lwt.setHints(R.string.redbag_wantgift);
        findViewById(R.id.dlg_wangmoney_tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("TTTTTTTMM",TimeUtil.formatBeforeTimeHour(Long.valueOf(PSP.getInstance().getString(MY_GETGIFT_TIME + ModuleMgr.getCenterMgr().getMyInfo().getUid(), "0")))+"|||"+ModuleMgr.getAppMgr().getTime()+"|||"+PSP.getInstance().getLong(MY_GETGIFT_TIME + ModuleMgr.getCenterMgr().getMyInfo().getUid(),0));
                if (TimeUtil.formatBeforeTimeHour(PSP.getInstance().getLong(MY_GETGIFT_TIME + ModuleMgr.getCenterMgr().getMyInfo().getUid(), 0)) < 1) {
                    PToast.showShort(context.getString(R.string.one_hour_onece));
                    dismiss();
                    return;
                }
                if ("".equals(lwt.getText().trim())) {
                    PToast.showShort(context.getString(R.string.want_to_say));
                    return;
                }
                getGifts();
                dismiss();
            }
        });
    }

    private void getGifts() {
        LoadingDialog.show((FragmentActivity) context, "正在提交...");
        ModuleMgr.getCommonMgr().begGift(lwt.getText().trim(), this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (!(event.getX() >= -10 && event.getY() >= -10)
                    || event.getX() >= view.getWidth() + 10
                    || event.getY() >= view.getHeight() + 20) {
                lwt.clearFocus();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        if (response.isOk()){
            PSP.getInstance().put(MY_GETGIFT_TIME + ModuleMgr.getCenterMgr().getMyInfo().getUid(), ModuleMgr.getAppMgr().getTime());
            PToast.showShort(response.getMsg());
            return;
        }
//                if (rb.getIsOk()) TimeUtil.setOwerOneHoursFlag(KeyMgr.U_MY_GETGIFT);
//                T.showShort(context, rb.getMsg());
        PToast.showShort(context.getString(R.string.error_retry));
    }
}
