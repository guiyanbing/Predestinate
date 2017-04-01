package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.util.UIUtil;


/**
 * 排行榜奖品弹框，只在每日第一次切换到排行榜tab时显示
 * Created by ZRP on 2016/9/1.
 */
public class RedPacketDialog extends BaseDialogFragment implements View.OnClickListener {

    private LinearLayout llDetail;//红包详情
    private LinearLayout llOpen;//红包打开布局

    private double money;
    private TextView txvMoney;

    public RedPacketDialog() {
        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
        setGravity(Gravity.CENTER);
        setDialogSizeRatio(-2, -2);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.p1_redpacket_dialog);

        View contentView = getContentView();
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        llOpen = (LinearLayout) contentView.findViewById(R.id.hongbao_ll_open);
        llDetail = (LinearLayout) contentView.findViewById(R.id.hongbao_ll_detail);

        contentView.findViewById(R.id.hongbao_img_open).setOnClickListener(this);
        contentView.findViewById(R.id.hongbao_ll_go).setOnClickListener(this);

        txvMoney = (TextView) contentView.findViewById(R.id.hongbao_txv_money);
    }

    /**
     * 设置显示数据
     *
     * @param money 得到的钱
     */
    public void setData(double money) {
        this.money = money;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hongbao_img_open:
                //TODO 打开红包操作
//                UIShow.showGiftShopAct((FragmentActivity) getContext());
                llOpen.setVisibility(View.GONE);
                llDetail.setVisibility(View.VISIBLE);
                String str = txvMoney.getText().toString();
                if (!TextUtils.isEmpty(str)){
//                    SpannableString WordtoSpan = new SpannableString("大字小字");
//                    WordtoSpan.setSpan(new AbsoluteSizeSpan(UIUtil.px2sp(130),true), 0, str.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    WordtoSpan.setSpan(new AbsoluteSizeSpan(UIUtil.px2sp(50),true), str.length()-1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    txvMoney.setText(WordtoSpan.toString(),TextView.BufferType.SPANNABLE);
                    Spannable sp = new SpannableString(str);
                    sp.setSpan(new AbsoluteSizeSpan(UIUtil.px2sp(130),true),0, str.length()-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sp.setSpan(new AbsoluteSizeSpan(UIUtil.px2sp(50),true),str.length()-1, str.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    txvMoney.setText(sp);
                }

                break;
            case R.id.hongbao_ll_go:
                //// TODO: 2017/3/31  跳转到小友钱包明细页
                dismissAllowingStateLoss();
                PToast.showShort("跳转到小友钱包明细页");
                break;
        }
    }
}
