package com.juxin.predestinate.ui.user.paygoods.diamond;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.baseui.custom.RadiationView;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;
import com.juxin.predestinate.ui.user.paygoods.GoodsListPanel;
import com.juxin.predestinate.ui.user.paygoods.GoodsPayTypePanel;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 音视频聊天钻石不足充值页面
 * <p>
 * Created by Su on 2017/6/22.
 */
public class BottomChatDiamondDlg extends BaseDialogFragment implements View.OnClickListener {
    public static final int RTC_CHAT_VIDEO = 1;    // 视频通信
    public static final int RTC_CHAT_VOICE = 2;    // 音频通信

    private PayGoods payGoods;  // 商品信息
    private GoodsListPanel goodsPanel; // 商品列表
    private GoodsPayTypePanel payTypePanel; // 支付方式

    private String girlUrl;
    private int chatType;
    private int price;

    public BottomChatDiamondDlg() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(-2, -2);
        setCancelable(true);
    }

    /**
     * @param girlUrl  女用户头像
     * @param chatType 视频，语音邀请
     * @param price    通信价格
     */
    public void setInfo(String girlUrl, int chatType, int price) {
        this.girlUrl = girlUrl;
        this.chatType = chatType;
        this.price = price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_bottom_chat_diamond_dialog);
        View contentView = getContentView();
        initList();
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        initRadiationView();
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.btn_recharge).setOnClickListener(this);

        TextView diamond = (TextView) findViewById(R.id.diamond_remain);
        ImageView myHead = (ImageView) findViewById(R.id.iv_my_head);
        ImageView girlHead = (ImageView) findViewById(R.id.iv_girl_head);
        TextView chatPrice = (TextView) findViewById(R.id.chat_price);

        chatPrice.setText(getString(R.string.invite_video_price, price));
        diamond.setText(getString(R.string.diamond_balance) + ModuleMgr.getCenterMgr().getMyInfo().getDiamand());
        ImageLoader.loadCircleAvatar(getContext(), ModuleMgr.getCenterMgr().getMyInfo().getAvatar(), myHead);
        ImageLoader.loadCircleAvatar(getContext(), girlUrl, girlHead);

        if (chatType == RTC_CHAT_VOICE) {
            ImageView chatImg = (ImageView) findViewById(R.id.chat_img);
            TextView chatText = (TextView) findViewById(R.id.chat_text);
            chatImg.setImageResource(R.drawable.f1_chat_voice_img);
            chatText.setText(getString(R.string.invite_voice_tips));
        }

        // 商品
        LinearLayout container = (LinearLayout) contentView.findViewById(R.id.pay_type_container);
        goodsPanel = new GoodsListPanel(getContext());
        container.addView(goodsPanel.getContentView());
        goodsPanel.refresh(payGoods.getCommodityList());

        // 支付方式
        payTypePanel = new GoodsPayTypePanel(getContext(), GoodsConstant.PAY_TYPE_NEW);
        container.addView(payTypePanel.getContentView());
    }

    private void initRadiationView() {
        final RadiationView rvFirst = (RadiationView) findViewById(R.id.rv_first);
        final RadiationView rvSec = (RadiationView) findViewById(R.id.rv_sec);
        rvFirst.setDpMultiple(UIUtil.toDpMultiple(getActivity()));
        rvSec.setDpMultiple(UIUtil.toDpMultiple(getActivity()));

        // 两个控件互相调起
        rvFirst.setRadiationListener(new RadiationView.RadiationListener() {
            @Override
            public void onArrival() {
                rvSec.startRadiate();   // rvSec开启
                if (rvSec.getVisibility() == View.GONE)
                    rvSec.setVisibility(View.VISIBLE);
            }
        });

        rvSec.setRadiationListener(new RadiationView.RadiationListener() {
            @Override
            public void onArrival() {
                rvFirst.startRadiate(); // rvFirst开启
            }
        });
        rvFirst.startRadiate();  // 启动其中之一
    }

    private void initList() {
        if (payGoods == null)
            payGoods = new PayGoods();

        String json = FileUtil.getFromAssets(App.getActivity(), "info_goods.json");
        try {
            if (TextUtils.isEmpty(json)) return;
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("chat_diamond"))
                payGoods.parseJson(jsonObject.optString("chat_diamond"));
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:  // 充值
                UIShow.showPayAlipayt(getActivity(), payGoods.getCommodityList().get(goodsPanel.getPosition()).getId(), payTypePanel.getPayType(),
                        0, "");
                break;

            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}