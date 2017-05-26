package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.GiftMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 礼物消息展示panel
 * Created by Kind on 2017/5/10.
 */
public class ChatPanelGift extends ChatPanel {

    private View iv_gift_me, tv_gift_click_send, tv_gift_status;
    private ImageView iv_gift_img;
    private TextView tv_gift_hello, tv_gift_content;

    public ChatPanelGift(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_gift, sender);
    }

    @Override
    public void initView() {
        iv_gift_me = findViewById(R.id.iv_gift_me);
        iv_gift_img = (ImageView) findViewById(R.id.iv_gift_img);
        tv_gift_hello = (TextView) findViewById(R.id.tv_gift_hello);
        tv_gift_content = (TextView) findViewById(R.id.tv_gift_content);
        tv_gift_click_send = findViewById(R.id.tv_gift_click_send);
        tv_gift_status = findViewById(R.id.tv_gift_status);

        if (isSender()) {
            tv_gift_hello.setTextColor(getContext().getResources().getColor(R.color.white));
            tv_gift_content.setTextColor(getContext().getResources().getColor(R.color.color_EEEEEE));
            tv_gift_status.setVisibility(View.GONE);
        } else {
            tv_gift_hello.setTextColor(getContext().getResources().getColor(R.color.color_040000));
            tv_gift_content.setTextColor(getContext().getResources().getColor(R.color.color_777777));
            tv_gift_status.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean reset(BaseMessage msgData, UserInfoLightweight infoLightweight) {
        if (msgData == null || !(msgData instanceof GiftMessage)) return false;

        GiftMessage msg = (GiftMessage) msgData;
        boolean isNormalGift = msg.getType() == BaseMessage.BaseMessageType.gift.getMsgType();
        GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(msg.getGiftID());
        if (giftInfo == null) {
            PLogger.d("------>gift list is empty or gift list doesn't have this gift_id.");
            return false;
        }
        if (msgData.getfStatus() == 0) {
            tv_gift_status.setVisibility(View.GONE);
        }
        iv_gift_me.setVisibility(isNormalGift ? View.GONE : View.VISIBLE);
        tv_gift_click_send.setVisibility(isNormalGift ? View.GONE : View.VISIBLE);

        ImageLoader.loadAvatar(context, giftInfo.getPic(), iv_gift_img);
        tv_gift_hello.setText(msg.getMsgDesc());
        tv_gift_content.setText(Html.fromHtml(isNormalGift ? "送你" : "想要" +
                "<font color='#FD698C'>" + (msg.getGiftCount() == 0 ? 1 : msg.getGiftCount()) +
                "个" + giftInfo.getName() + "</font>"));
        return true;
    }

    @Override
    public boolean onClickContent(BaseMessage msgData, boolean longClick) {
        if (msgData == null || !(msgData instanceof GiftMessage) || isSender() || msgData.getfStatus() == 0) {
            return false;
        }
        GiftMessage msg = (GiftMessage) msgData;
        GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(msg.getGiftID());
        if (msg.getType() == BaseMessage.BaseMessageType.gift.getMsgType()){
            if (!isSender()) {//别人给你发的礼物
               ModuleMgr.getCommonMgr().receiveGift(msg.getGiftLogID(), giftInfo.getName(), msg.getGiftID(), new RequestComplete() {
                   @Override
                   public void onRequestComplete(HttpResponse response) {
                       if (response.isOk()){
                           // TODO: 2017/5/26 往数据库插一条hint提示消息，标识已接受礼物
                         //   ModuleMgr.getChatMgr().onLocalReceiving(message);
                       }
                   }
               });
            }
        }else {
            UIShow.showDiamondSendGiftDlg(App.getActivity(), msg.getGiftID(), msg.getWhisperID());
        }
        return true;
    }
}
