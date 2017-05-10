package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.chat.msgtype.GiftMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.wode.bean.GiftsList;

/**
 * Created by Kind on 2017/5/10.
 */

public class ChatPanelGift extends ChatPanel {

    private ImageView chat_item_gift_img;
    private TextView chat_item_gift_tvMsg, chat_item_gift_tvInfo, chat_item_gift_tvGetGift;

    public ChatPanelGift(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_gift, sender);
    }

    @Override
    public void initView() {
        chat_item_gift_img = (ImageView) findViewById(R.id.chat_item_gift_img);
        chat_item_gift_tvMsg = (TextView) findViewById(R.id.chat_item_gift_tvMsg);
        chat_item_gift_tvInfo = (TextView) findViewById(R.id.chat_item_gift_tvInfo);
        chat_item_gift_tvGetGift = (TextView) findViewById(R.id.chat_item_gift_tvGetGift);

        if (isSender()) {
            chat_item_gift_tvMsg.setTextColor(getContext().getResources().getColor(R.color.white));
            chat_item_gift_tvInfo.setTextColor(getContext().getResources().getColor(R.color.color_EEEEEE));
        }else {
            chat_item_gift_tvMsg.setTextColor(getContext().getResources().getColor(R.color.color_040000));
            chat_item_gift_tvInfo.setTextColor(getContext().getResources().getColor(R.color.color_777777));
            chat_item_gift_tvGetGift.setTextColor(getContext().getResources().getColor(R.color.color_777777));
        }
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof CommonMessage)) return false;

        GiftMessage msg = (GiftMessage) msgData;

        GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(msg.getGiftID());
        if(giftInfo == null){
            return false;
        }

        chat_item_gift_tvMsg.setText(Html.fromHtml(giftInfo.getName() + ""));
        chat_item_gift_tvInfo.setText(Html.fromHtml(giftInfo.getGif() + ""));
        ImageLoader.loadAvatar(context, giftInfo.getPic(), chat_item_gift_img);

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
