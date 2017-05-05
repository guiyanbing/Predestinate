package com.juxin.predestinate.ui.xiaoyou.wode;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.SendGiftResultInfo;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.GiftsList;


public class DiamondSendGiftDlg extends Dialog implements View.OnClickListener,RequestComplete {
    private String channelId;
    private String otherId;
    private Context mContext;
    private TextView tv_diamonds, tv_gift_diamonds;
    private ImageView iv_pic;
    private String giftPic;
    private int giftDiamonds;
    private GiftsList.GiftInfo giftBean;

    public DiamondSendGiftDlg(Context context, int giftid,String OtherId, String ChannelId) {
        super(context, R.style.dialog);
        mContext = context;
        giftBean = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(giftid);
        if(null == giftBean){
            PToast.showShort("末找到礼物");
            return;
        }
        giftPic = giftBean.getPic();
        giftDiamonds = giftBean.getMoney();
        otherId = OtherId;
        channelId = ChannelId;
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.f1_dlg_send_gift, null);
        setContentView(view);
        view.findViewById(R.id.btn_diamond_ok).setOnClickListener(this);

        tv_diamonds = (TextView) findViewById(R.id.tv_send_gift_my_diamonds);
        tv_gift_diamonds = (TextView) findViewById(R.id.tv_send_gift_diamonds);
        iv_pic = (ImageView) findViewById(R.id.iv_send_gift_pic);
        tv_gift_diamonds.setText(giftDiamonds + "个钻石");
        tv_diamonds.setText("钻石余额：" + ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum());
        findViewById(R.id.tv_send_gift_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ImageLoader.loadAvatar(context,giftPic,iv_pic);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_diamond_ok:
                int dec = ModuleMgr.getCenterMgr().getMyInfo().getDiamondsSum() - giftBean.getMoney();
                if (dec >= 0) {//赠送礼物
                    //            if (null != iGiftSend) {
                    //                iGiftSend.onSend(giftBean);
                    //            }
                    ModuleMgr.getCommonMgr().sendGift(otherId,giftBean.getId()+"",this);
                } else {
                    //            if (null != iGiftSend) {
                    //                iGiftSend.onSendToPay(giftBean);
                    //            }
                    //            UIHelper.showDiamondsNormalDlg((Activity) mContext, otherId, channelId, Math.abs(dec));
                    PToast.showShort("跳转充值弹框");
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        SendGiftResultInfo info = new SendGiftResultInfo();
        info.parseJson(response.getResponseString());
        PToast.showShort(info.getMsg()+"");
    }
}
