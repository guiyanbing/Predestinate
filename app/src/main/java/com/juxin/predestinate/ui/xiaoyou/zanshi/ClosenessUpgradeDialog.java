package com.juxin.predestinate.ui.xiaoyou.zanshi;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;


/**
 * 消息红包弹框
 * Created by zm on 2017/3/.
 */
public class ClosenessUpgradeDialog extends BaseDialogFragment implements View.OnClickListener {

    private ImageView imgLevel;
    private int[] levels = new int[]{R.drawable.p1_lv00b, R.drawable.p1_lv01b, R.drawable.p1_lv02b, R.drawable.p1_lv03b, R.drawable.p1_lv04b, R.drawable.p1_lv05b, R.drawable.p1_lv06b};

    public ClosenessUpgradeDialog() {
        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
        setGravity(Gravity.CENTER);
        setDialogSizeRatio(-2, -2);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.p1_closenesstoupgrade_dialog);

        View contentView = getContentView();
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        imgLevel = (ImageView) contentView.findViewById(R.id.upgrade_ceiv_level);
        contentView.findViewById(R.id.upgrade_btn_ok).setOnClickListener(this);
    }

    private void setLevel() {
        //设置等级
        //        imgLevel.setImageResource();
    }

    //    /**
    //     * 设置显示数据
    //     *
    //     * @param money 得到的钱
    //     */
    //    public void setData(double money) {
    //        this.money = money;
    //    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upgrade_btn_ok://取消弹框
                dismissAllowingStateLoss();
                break;
        }
    }
}
