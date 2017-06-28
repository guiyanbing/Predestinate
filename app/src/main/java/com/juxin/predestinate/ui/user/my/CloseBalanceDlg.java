package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.juxin.library.log.PSP;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.config.Constant;

/**
 * 创建日期：2017/6/20
 * 描述:关闭余额弹框
 * 作者:lc
 */
public class CloseBalanceDlg extends BaseDialogFragment implements View.OnClickListener {

    private Context context;
    private CheckBox cb_def_sel;
    private TextView tv_cancel,tv_sure;
    private IsCloseYTips isCloseYTips;

    public interface IsCloseYTips {
        void isCloseYTips();
    }

    public CloseBalanceDlg() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(1, 0);
        setCancelable(false);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIsCloseYTips(IsCloseYTips callBack) {
        this.isCloseYTips = callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_close_balance_dlg);
        View view = getContentView();
        initView(view);
        return view;
    }

    private void initView(View view) {
        cb_def_sel = (CheckBox) view.findViewById(R.id.cb_def_sel);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_sure = (TextView) view.findViewById(R.id.tv_sure);

        tv_cancel.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        cb_def_sel.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                PSP.getInstance().put(ModuleMgr.getCommonMgr().getPrivateKey(Constant.CLOSE_Y_TIPS_VALUE), cb_def_sel.isChecked());
                PSP.getInstance().put(ModuleMgr.getCommonMgr().getPrivateKey(Constant.CLOSE_Y_TMP_TIPS_VALUE), true);
                isCloseYTips.isCloseYTips();
                dismiss();
                break;
            default:
                break;
        }
    }
}
