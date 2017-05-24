package com.juxin.predestinate.ui.mail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.juxin.library.log.PLogger;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.SortList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.mail.item.CustomMailItem;
import com.juxin.predestinate.ui.mail.item.MailItemType;
import com.juxin.predestinate.ui.mail.item.MailMsgID;
import java.util.ArrayList;
import java.util.List;

/**
 * 信箱
 * Created by Kind on 16/1/20.
 */
public class MailFragmentAdapter extends ExBaseAdapter<BaseMessage> {

    private  boolean scrollState=false;
    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }

    private MailItemType mailItemType = null;

    public MailFragmentAdapter(Context context, List<BaseMessage> datas) {
        super(context, datas);
    }

    public int mailItemOtherSize() {
        int size = 0;
        for (BaseMessage tmp : getList()) {
            if (tmp.getMailItemStyle() == MailItemType.Mail_Item_Other.type) {
                size++;
            }
        }

        return size;
    }

    public int mailItemOrdinarySize() {
        return mailItemOrdinary().size();
    }

    public List<BaseMessage> mailItemOrdinary() {
        List<BaseMessage> messageList = new ArrayList<>();
        for (BaseMessage tmp : getList()) {
            if (tmp.getMailItemStyle() == MailItemType.Mail_Item_Ordinary.type) {
                messageList.add(tmp);
            }
        }

        return messageList;
    }

    public void updateAllData() {
        List<BaseMessage> messageLists = ModuleMgr.getChatListMgr().getMsgList();
        PLogger.d("messageLists=多少人=" + messageLists.size());

        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setWhisperID(String.valueOf(MailMsgID.WhoAttentionMe_Msg.type));
        baseMessage.setWeight(BaseMessage.Max_Weight);
        baseMessage.setMailItemStyle(MailItemType.Mail_Item_Other.type);
        int num = ModuleMgr.getChatListMgr().getFollowNum();
        baseMessage.setNum(num);
        baseMessage.setName("谁关注我");
        baseMessage.setAboutme(num > 0 ? "共有" + num + "位关注我" : "暂时还没有人关注我");
        baseMessage.setLocalAvatar(R.drawable.f1_sgzw_ico);
        messageLists.add(baseMessage);

        baseMessage = new BaseMessage();
        baseMessage.setWhisperID(String.valueOf(MailMsgID.MyFriend_Msg.type));
        baseMessage.setWeight(BaseMessage.Max_Weight);
        baseMessage.setMailItemStyle(MailItemType.Mail_Item_Other.type);
        baseMessage.setName("我的好友");
        baseMessage.setAboutme("赠送礼物即可成为好友");
        baseMessage.setLocalAvatar(R.drawable.f1_sgzw02_ico);
        messageLists.add(baseMessage);

        setList(messageLists);
        SortList.sortWeightTimeListView(getList());
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        int TYPE_COUNT = 2;
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = getItem(position);
        if (message != null) {
            return message.getMailItemStyle();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflate(R.layout.p1_mail_fragment_item);
            vh.customMailItem = (CustomMailItem) convertView.findViewById(R.id.mail_item);

            vh.customMailItem.onCreateView();
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        BaseMessage msgData = getItem(position);
        if (msgData != null) {
            MailItemType mailItemType = MailItemType.getMailMsgType(getItemViewType(position));
            if (mailItemType != null) {
                switch (mailItemType) {
                    case Mail_Item_Ordinary:
                        if (msgData.getWeight() == BaseMessage.Max_Weight) {
                            vh.customMailItem.showItemAct(msgData);
                        } else {
                            vh.customMailItem.showItemLetter(msgData);
                        }

                        break;
                    case Mail_Item_Other:
                        vh.customMailItem.showItemAct(msgData);
                        break;
                }

                int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                vh.customMailItem.measure(width, height);
                setItemHeight(vh.customMailItem.getMeasuredHeight());

                if (this.mailItemType != mailItemType) {
                    this.mailItemType = mailItemType;
                    switch (mailItemType) {
                        case Mail_Item_Ordinary:
                            vh.customMailItem.showLetterGap();
                            break;
                        case Mail_Item_Other:
                            vh.customMailItem.showActGap();
                            break;
                    }
                }
            }
        }

        return convertView;
    }

    private class ViewHolder {
        CustomMailItem customMailItem;
    }

    private int itemHeight;

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getItemHeight() {
        return itemHeight;
    }
}