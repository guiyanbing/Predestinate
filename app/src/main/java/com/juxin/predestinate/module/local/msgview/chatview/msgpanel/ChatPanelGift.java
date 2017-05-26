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
import com.juxin.predestinate.module.local.chat.msgtype.TextMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

/**
 * 礼物消息展示panel
 * Created by Kind on 2017/5/10.
 */
public class ChatPanelGift extends ChatPanel {

    private ImageView iv_gift_img;
    private TextView tv_gift_hello, tv_gift_content, tv_gift_status;

    public ChatPanelGift(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_gift, sender);
    }

    @Override
    public void initView() {
        iv_gift_img = (ImageView) findViewById(R.id.iv_gift_img);
        tv_gift_hello = (TextView) findViewById(R.id.tv_gift_hello);
        tv_gift_content = (TextView) findViewById(R.id.tv_gift_content);
        tv_gift_status = (TextView) findViewById(R.id.tv_gift_status);

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
        GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(msg.getGiftID());
        if (giftInfo == null) {
            PLogger.d("------>gift list is empty or gift list doesn't have this gift_id.");
            return false;
        }
        tv_gift_status.setText(msgData.getfStatus() == 0 ? "已领取" : "点击收礼");
        ImageLoader.loadAvatar(context, giftInfo.getPic(), iv_gift_img);
        tv_gift_hello.setText(isSender() ? (ModuleMgr.getCenterMgr().getMyInfo().isMan() ?
                getContext().getString(R.string.chat_gift_hello_woman) :
                getContext().getString(R.string.chat_gift_hello_man)) : Html.fromHtml(msg.getMsgDesc()));
        tv_gift_content.setText(Html.fromHtml("送你<font color='#FD698C'>"
                + (msg.getGiftCount() == 0 ? 1 : msg.getGiftCount()) +
                "个" + giftInfo.getName() + "</font>"));
        return true;
    }

    @Override
    public boolean onClickContent(final BaseMessage msgData, boolean longClick) {
        if (msgData == null || !(msgData instanceof GiftMessage) || isSender() || msgData.getfStatus() == 0) {
            return false;
        }
        //别人给你发的礼物
        final GiftMessage msg = (GiftMessage) msgData;
        GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(msg.getGiftID());
        ModuleMgr.getCommonMgr().receiveGift(msg.getGiftLogID(), giftInfo.getName(), msg.getGiftID(), new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    long ret = ModuleMgr.getChatMgr().updateMsgFStatus(msg.getMsgID());
                    if(ret == MessageConstant.OK){
                        tv_gift_status.setText("已领取");
                    }
                    // 往数据库插一条14提示消息，标识已接受礼物
                    TextMessage textMessage = new TextMessage();
                    textMessage.setWhisperID(msgData.getWhisperID());
                    textMessage.setSendID(App.uid);
                    textMessage.setMsgDesc(getContext().getString(R.string.chat_gift_has_received));
                    textMessage.setcMsgID(BaseMessage.getCMsgID());
                    textMessage.setType(BaseMessage.BaseMessageType.hint.getMsgType());
                    textMessage.setJsonStr(textMessage.getJson(textMessage));
                    ModuleMgr.getChatMgr().onLocalReceiving(textMessage);
                }
            }
        });
        return true;
    }
}
