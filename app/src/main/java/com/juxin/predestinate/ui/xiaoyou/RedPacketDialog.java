package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;


/**
 * 排行榜奖品弹框，只在每日第一次切换到排行榜tab时显示
 * Created by ZRP on 2016/9/1.
 */
public class RedPacketDialog extends BaseDialogFragment implements View.OnClickListener {

    private String detailString, iconUrl;

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
        TextView rank_detail = (TextView) contentView.findViewById(R.id.rank_detail);
        ImageView gift_icon = (ImageView) contentView.findViewById(R.id.gift_icon);

        contentView.findViewById(R.id.close).setOnClickListener(this);
        contentView.findViewById(R.id.confirm).setOnClickListener(this);

        rank_detail.setText(detailString);
        
        
        //// TODO: 2017/3/30 设置图片请求
//        ModuleMgr.httpMgr.reqImage(gift_icon, iconUrl, R.drawable.default_pic);
    }

    /**
     * 设置显示数据
     *
     * @param detailString 排行信息
     * @param iconUrl      奖品图片地址
     */
    public void setData(String detailString, String iconUrl) {
        this.detailString = detailString;
        this.iconUrl = iconUrl;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                //TODO 跳转到奖品页面
//                UIShow.showGiftShopAct((FragmentActivity) getContext());
                dismissAllowingStateLoss();
                break;
            case R.id.close:
                dismissAllowingStateLoss();
                break;
        }
    }
}
