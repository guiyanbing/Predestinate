package com.juxin.predestinate.ui.mail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.juxin.mumu.bean.log.MMLog;
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

    private MailItemType mailItemType = null;

    public MailFragmentAdapter(Context context, List<BaseMessage> datas) {
        super(context, datas);
    }

    public int mailItemOtherSize() {
        int size = 0;
        for(BaseMessage tmp : getList()){
            if(tmp.getMailItemStyle() == MailItemType.Mail_Item_Other.type){
                size ++;
            }
        }

        return size;
    }

    public int mailItemOrdinarySize() {
        return mailItemOrdinary().size();
    }

    public List<BaseMessage> mailItemOrdinary() {
        List<BaseMessage> messageList = new ArrayList<>();
        for(BaseMessage tmp : getList()){
            if(tmp.getMailItemStyle() == MailItemType.Mail_Item_Ordinary.type){
                messageList.add(tmp);
            }
        }

        return messageList;
    }

//        if (messages != null && messages.size() > 0) {
//            msgList.addAll(messages);
//            for (BaseMessage tmp : messages) {
//                unreadNum += tmp.getNum();
//            }
//        }

    public void updateAllData() {
        List<BaseMessage> messageLists = ModuleMgr.getChatListMgr().getMsgList();
//        Boolean b = false;
//        for(BaseMessage tmp : messageLists){
//            MailMsgID mailMsgID = MailMsgID.getMailMsgID(tmp.getLWhisperID());
//            if(mailMsgID != null){
//                switch (mailMsgID){
//                    case act_msg:
//                        b = true;
//                        tmp.setWeight(BaseMessage.In_Weight);
//                        tmp.setMailItemStyle(MailItemType.Mail_Item_Act.type);
//                        tmp.setName("活动");
//                        tmp.setAboutme(BaseMessage.getContent(ModuleMgr.getChatListMgr().getActMsg()));
//                        tmp.setLocalAvatar(R.drawable.y2_msg_2);
//                        break;
//                }
//            }
//        }

        MMLog.autoDebug("messageLists=多少人=" + messageLists.size());


        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setWhisperID(String.valueOf(MailMsgID.WhoAttentionMe_Msg.type));
        baseMessage.setWeight(BaseMessage.Max_Weight);
        baseMessage.setMailItemStyle(MailItemType.Mail_Item_Other.type);
        baseMessage.setName("谁关注我");
        baseMessage.setAboutme("暂时还没有人关注我");
        baseMessage.setLocalAvatar(R.drawable.f1_sgzw_ico);
        messageLists.add(baseMessage);

        baseMessage = new BaseMessage();
        baseMessage.setWhisperID(String.valueOf(MailMsgID.MyFriend_Msg.type));
        baseMessage.setWeight(BaseMessage.Max_Weight);
        baseMessage.setMailItemStyle(MailItemType.Mail_Item_Other.type);
        baseMessage.setName("我的好友");
        baseMessage.setAboutme("我的好友");
        baseMessage.setLocalAvatar(R.drawable.f1_sgzw02_ico);
        messageLists.add(baseMessage);



        baseMessage = new BaseMessage();
        baseMessage.setWhisperID(String.valueOf(979797));
        //  baseMessage.setNum(ModuleMgr.getChatListMgr().getVisitNum());
        baseMessage.setWeight(BaseMessage.Small_Weight);
        baseMessage.setMailItemStyle(MailItemType.Mail_Item_Ordinary.type);
        baseMessage.setName("张三");
        baseMessage.setAboutme("暂未");
        baseMessage.setLocalAvatar(R.drawable.f1_sgzw02_ico);
        messageLists.add(baseMessage);

//        if(!b){
//            //活动
//            baseMessage = new BaseMessage();
//            baseMessage.setWhisperID(String.valueOf(MailMsgID.act_msg.type));
//            baseMessage.setWeight(BaseMessage.In_Weight);
//            baseMessage.setMailItemStyle(MailItemType.Mail_Item_Act.type);
//            baseMessage.setName("活动");
//            baseMessage.setAboutme(BaseMessage.getContent(ModuleMgr.getChatListMgr().getActMsg()));
//            baseMessage.setLocalAvatar(R.drawable.y2_msg_2);
//            messageLists.add(baseMessage);
//        }
//
//       //新朋友
//        baseMessage = new BaseMessage();
//        baseMessage.setWhisperID(String.valueOf(MailMsgID.new_friend_msg.type));
//        baseMessage.setNum(ModuleMgr.getChatListMgr().getNewFriendListUnread());
//        baseMessage.setWeight(BaseMessage.In_Weight);
//        baseMessage.setMailItemStyle(MailItemType.Mail_Item_Act.type);
//        baseMessage.setName("新朋友消息");
//
//        List<BaseMessage> messageList = ModuleMgr.getChatListMgr().getNewFriendList();
//        if(messageList != null && messageList.size() > 0){
//            SortList.sortWeightTimeListView(messageList);
//            BaseMessage tmpMsg = messageList.get(0);
//            if(tmpMsg != null){
//                baseMessage.setAboutme(BaseMessage.getContent(tmpMsg));
//            }
//        }
//
//        baseMessage.setLocalAvatar(R.drawable.y2_msg_3);
//        messageLists.add(baseMessage);

        //最近来访
//        baseMessage = new BaseMessage();
//        baseMessage.setWhisperID(String.valueOf(MailMsgID.visitors_msg.type));
//        baseMessage.setNum(ModuleMgr.getChatListMgr().getVisitNum());
//        baseMessage.setWeight(BaseMessage.In_Weight);
//        baseMessage.setMailItemStyle(MailItemType.Mail_Item_Act.type);
//        baseMessage.setName("最近来访");
//        baseMessage.setAboutme("点击查看");
//        baseMessage.setLocalAvatar(R.drawable.y2_msg_4);
      //  messageLists.add(baseMessage);

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
        if(message != null){
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
        if(msgData != null){
            MailItemType mailItemType = MailItemType.getMailMsgType(getItemViewType(position));
            if (mailItemType != null) {
                switch (mailItemType){
                    case Mail_Item_Ordinary:
                        if(msgData.getWeight() == BaseMessage.Max_Weight){
                            vh.customMailItem.showItemAct(msgData);
                        }else {
                            vh.customMailItem.showItemLetter(msgData);
                        }

                        break;
                    case Mail_Item_Other:
                        vh.customMailItem.showItemAct(msgData);
                        break;
                }

                if(this.mailItemType != mailItemType){
                    this.mailItemType = mailItemType;
                    switch (mailItemType){
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
}