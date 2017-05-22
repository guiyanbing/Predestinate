package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.chat.msgtype.GiftMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 礼物消息
 * Created by Kind on 2017/5/10.
 */

public class ChatPanelGiveMeGift extends ChatPanel {
    private ImageView imgThumb;
    private TextView tvMsg, tvInfo, tvSendGift;

    public ChatPanelGiveMeGift(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_give_me_gift, sender);
    }

    @Override
    public void initView() {
        imgThumb = (ImageView) findViewById(R.id.chat_item_give_thumb_img);
        tvMsg = (TextView) findViewById(R.id.chat_item_give_tvMsg);
        tvInfo = (TextView) findViewById(R.id.chat_item_give_tvInfo);
        tvSendGift = (TextView) findViewById(R.id.chat_item_give_tvSendGift);

        if (isSender()) {
            tvMsg.setTextColor(Color.parseColor("#ffffff"));
            tvInfo.setTextColor(Color.parseColor("#eeeeee"));
        }else {
            tvMsg.setTextColor(Color.parseColor("#040000"));
            tvInfo.setTextColor(Color.parseColor("#777777"));
            tvSendGift.setTextColor(Color.parseColor("#ea5514"));
      //      parent.setBackgroundResource(R.drawable.chat_item_left_dialog2);
       ///     parent.setPadding(BaseUtil.dip2px(mContext, 8), 0, 0, 0);
        }
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof GiftMessage)) return false;

        GiftMessage msg = (GiftMessage) msgData;

        GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(msg.getGiftID());
        if(giftInfo == null){
            return false;
        }

        tvMsg.setText(Html.fromHtml(giftInfo.getName() + ""));
        tvInfo.setText(Html.fromHtml(giftInfo.getGif() + ""));

        ImageLoader.loadAvatar(context, giftInfo.getPic(), imgThumb);



//        if (giveMeGiftBean != null && giveMeGiftBean.sGiftName != null){
//            basePanel.setTag(giveMeGiftBean);
//            this.listener = listener;
//            tvSendGift.setVisibility(View.VISIBLE);
//        } else {
//            tvSendGift.setVisibility(View.GONE);
//            this.listener = null;
//            basePanel.setTag(null);
//        }



        return true;
    }

    @Override
    public boolean onClickContent(BaseMessage msgData, boolean longClick) {
        if (msgData == null || !(msgData instanceof GiftMessage)) {
            return false;
        }
        GiftMessage msg = (GiftMessage) msgData;
        UIShow.showDiamondSendGiftDlg(getContext(), msg.getGiftID(), msg.getWhisperID());
        return true;
    }
}
