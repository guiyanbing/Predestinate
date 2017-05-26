package com.juxin.predestinate.module.local.msgview.chatview.msgpanel;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.db.FMessage;
import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.GiftMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import java.util.HashMap;
import java.util.Map;

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
        if (msgData.getfStatus() == 0) {
            tv_gift_status.setVisibility(View.GONE);
        }

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
        GiftMessage msg = (GiftMessage) msgData;
        GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(msg.getGiftID());
        ModuleMgr.getCommonMgr().receiveGift(msg.getGiftLogID(), giftInfo.getName(), msg.getGiftID(), new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    // 往数据库插一条14提示消息，标识已接受礼物
                    int msgType = BaseMessage.BaseMessageType.hint.getMsgType();
                    long currentTimeMillis = System.currentTimeMillis();
                    long cMsgID = BaseMessage.getCMsgID();
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("mct", getContext().getString(R.string.chat_gift_has_received));
                    objectMap.put("fid", msgData.getWhisperID());
                    objectMap.put("tid", msgData.getSendID());
                    objectMap.put("mtp", msgType);
                    objectMap.put("mt", currentTimeMillis);
                    objectMap.put("d", cMsgID);

                    Bundle bundle = new Bundle();
                    bundle.putString(FMessage.COLUMN_WHISPERID, msgData.getWhisperID());
                    bundle.putLong(FMessage.COLUMN_SENDID, msgData.getSendID());
                    bundle.putLong(FMessage.COLUMN_CMSGID, cMsgID);
                    bundle.putInt(FMessage.COLUMN_TYPE, msgType);
                    bundle.putLong(FMessage.COLUMN_TIME, currentTimeMillis);
                    bundle.putString(FMessage.COLUMN_CONTENT, new Gson().toJson(objectMap));
                    ModuleMgr.getChatMgr().onLocalReceiving(new BaseMessage(bundle));
                }
            }
        });
        return true;
    }
}
