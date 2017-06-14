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
import com.juxin.predestinate.module.util.UIShow;

/**
 * 索要礼物消息，只能出现在发送左侧
 * Created by Kind on 2017/5/10.
 */
public class ChatPanelGiveMeGift extends ChatPanel {

    private ImageView iv_gift_img;
    private TextView tv_gift_hello, tv_gift_content;

    public ChatPanelGiveMeGift(Context context, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        super(context, chatInstance, R.layout.f1_chat_item_panel_give_me_gift, sender);
        setShowParentBg(false);
    }

    @Override
    public void initView() {
        iv_gift_img = (ImageView) findViewById(R.id.iv_gift_img);
        tv_gift_hello = (TextView) findViewById(R.id.tv_gift_hello);
        tv_gift_content = (TextView) findViewById(R.id.tv_gift_content);
        if (isSender()) {//防止数据库出错，索要礼物消息展示在右侧的情况
            View rl_container = findViewById(R.id.rl_container);
            rl_container.setBackgroundResource(R.drawable.y1_talk_box_24x24_me);
            rl_container.setPadding(0, 0, 0, 0);
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

        ImageLoader.loadAvatar(context, giftInfo.getPic(), iv_gift_img);
        tv_gift_hello.setText(ModuleMgr.getCenterMgr().getMyInfo().isMan() ?
                getContext().getString(R.string.chat_gift_hello_man)
                : getContext().getString(R.string.chat_gift_hello_woman));
        tv_gift_content.setText(Html.fromHtml("想要<font color='#FD698C'>"
                + (msg.getGiftCount() == 0 ? 1 : msg.getGiftCount()) +
                "个" + giftInfo.getName() + "</font>"));
        return true;
    }

    @Override
    public boolean onClickContent(BaseMessage msgData, boolean longClick) {
        if (msgData == null || !(msgData instanceof GiftMessage)) {
            return false;
        }
        GiftMessage msg = (GiftMessage) msgData;
        UserInfoLightweight info = getChatInstance().chatAdapter.getUserInfo(msg.getLWhisperID());
        UIShow.showDiamondSendGiftDlg(App.getActivity(), msg.getGiftID(), msg.getWhisperID(),
                info == null ? "" : String.valueOf(info.getChannel_uid()));
        return true;
    }
}
