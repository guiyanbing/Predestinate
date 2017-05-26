package com.juxin.predestinate.module.local.msgview.chatview.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.view.CircleImageView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.ChatMsgType;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kind on 2017/3/30.
 */

public class ChatContentAdapter extends ExBaseAdapter<BaseMessage> {

    private ChatAdapter.ChatInstance chatInstance = null;

    public ChatContentAdapter(Context context, List<BaseMessage> datas) {
        super(context, datas);
    }

    public ChatAdapter.ChatInstance getChatInstance() {
        return chatInstance;
    }

    public void setChatInstance(ChatAdapter.ChatInstance chatInstance) {
        this.chatInstance = chatInstance;
    }

    /**
     * 将更新的消息添加到消息列表中。如果已经存在的替换，新消息则添加的末尾。<br>
     * 注意区分发送的消息还是接收到的消息。
     * @param message 需要更新的消息。
     */
    public void updateData(BaseMessage message) {
        if (message == null) return;

        List<BaseMessage> datas = getList();

        if (datas == null) {
            datas = new ArrayList<>();
            datas.add(message);
            setList(datas);
            return;
        }

        boolean isSender = false;

        if (ChatAdapter.isSender(message.getSendID())) {
            isSender = true;
        }

        BaseMessage data;
        for (int i = 0; i < datas.size(); i++) {
            data = datas.get(i);

            if (isSender) {
                if ((message.getcMsgID() > 0 && data.getcMsgID() == message.getcMsgID())) {
                    // 本地发送的消息更新
                    datas.set(i, message);
                    notifyDataSetChanged();
                    return;
                }

                if ((message.getMsgID() > 0 && data.getMsgID() == message.getMsgID())) {
                    datas.set(i, message);
                    notifyDataSetChanged();
                    return;
                }
            } else {
                if (data.getMsgID() == message.getMsgID() && data.getcMsgID() == message.getcMsgID()) {
                    datas.set(i, message);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        datas.add(message);
        notifyDataSetChanged();
    }

    /**
     * 删除一条消息。
     *
     * @param message 删除消息。
     */
//    public void delData(DeleteMessage message) {
//        if (message == null) {
//            return;
//        }
//
//        List<BaseMessage> datas = getList();
//
//        if (datas == null) {
//            return;
//        }
//
//        BaseMessage data;
//        for (int i = 0; i < datas.size(); i++) {
//            data = datas.get(i);
//
//            if (data.getMsgID() == message.getDeleteMsgid()) {
//                datas.remove(data);
//                notifyDataSetChanged();
//                return;
//            }
//        }
//    }

    /**
     * 回调所有消息。
     *
     * @param
     */
//    public void callAllMsg(SystemMessage message) {
//        if (message == null) {
//            return;
//        }
//
//        List<BaseMessage> datas = getList();
//        if (datas == null) {
//            return;
//        }
//
//        boolean update = false;
//        BaseMessage data;
//
//        for (int i = 0; i < datas.size(); i++) {
//            data = datas.get(i);
//            update |= message.modifyReadStatus(data);
//        }
//
//        if (update) {
//            notifyDataSetChanged();
//        }
//    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflate(R.layout.p1_chat_item);
            convertView.setTag(vh);

            vh.time = (TextView) convertView.findViewById(R.id.chat_item_time);

            ChatItemHolder cih;

            cih = (ChatItemHolder) vh.receiver;
            cih.parent = convertView.findViewById(R.id.chat_item_left);
            cih.name = (TextView) cih.parent.findViewById(R.id.chat_item_name);
            cih.head = (CircleImageView) cih.parent.findViewById(R.id.chat_item_head);
            cih.content = (ViewGroup) cih.parent.findViewById(R.id.chat_item_content);
            cih.status = (TextView) cih.parent.findViewById(R.id.chat_item_status);
            cih.statusImg = (ImageView) cih.parent.findViewById(R.id.chat_item_status_img);
            cih.statusProgress = (ProgressBar) cih.parent.findViewById(R.id.chat_item_status_progress);
            cih.statusError = (ImageView) cih.parent.findViewById(R.id.chat_item_status_error);

            cih = (ChatItemHolder) vh.sender;
            cih.parent = convertView.findViewById(R.id.chat_item_right);
            cih.name = (TextView) cih.parent.findViewById(R.id.chat_item_name);
            cih.head = (CircleImageView) cih.parent.findViewById(R.id.chat_item_head);
            cih.content = (ViewGroup) cih.parent.findViewById(R.id.chat_item_content);
            cih.status = (TextView) cih.parent.findViewById(R.id.chat_item_status);
            cih.statusImg = (ImageView) cih.parent.findViewById(R.id.chat_item_status_img);
            cih.statusProgress = (ProgressBar) cih.parent.findViewById(R.id.chat_item_status_progress);
            cih.statusError = (ImageView) cih.parent.findViewById(R.id.chat_item_status_error);

            ChatViewHolder cvh = vh.custom;
            cvh.parent = convertView.findViewById(R.id.chat_item_custom);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        BaseMessage msgData = getItem(position);
        vh.reset(ChatAdapter.isSender(msgData.getSendID()), msgData, getItem(position - 1));
        return convertView;
    }

    /**
     * 左右的布局是不一样的，因此都要准备好各自的布局。
     */
    private class ViewHolder {
        public TextView time;

        public ChatViewHolder receiver = new ChatItemHolder();
        public ChatViewHolder sender = new ChatItemHolder();
        public ChatViewHolder custom = new ChatCustomHolder();

        public void reset(boolean sender, BaseMessage msgData, BaseMessage preMsgData) {
            if (msgData == null) return;
            String tipTime = "";
            if (preMsgData != null) {
                long tmp = TimeUtil.onPad(msgData.getTime());
                long temp = TimeUtil.onPad(preMsgData.getTime());
                if (tmp - temp > Constant.CHAT_SHOW_TIP_TIME_Interval) {
                    tipTime = TimeUtil.getFormatTimeChatTip(tmp);
                }
            }

            if (TextUtils.isEmpty(tipTime)) {
                time.setVisibility(View.GONE);
            } else {
                time.setVisibility(View.VISIBLE);
                time.setText(tipTime);
            }

            // 提高效率
            if (msgData.isCustomMsgPanel()) {
                this.receiver.parent.setVisibility(View.GONE);
                this.sender.parent.setVisibility(View.GONE);
                this.custom.parent.setVisibility(View.VISIBLE);

                this.custom.reset(msgData, sender);
            } else {
                this.custom.parent.setVisibility(View.GONE);

                if (sender) {
                    this.receiver.parent.setVisibility(View.GONE);
                    this.sender.parent.setVisibility(View.VISIBLE);
                    this.sender.reset(msgData, sender);
                } else {
                    this.sender.parent.setVisibility(View.GONE);
                    this.receiver.parent.setVisibility(View.VISIBLE);
                    this.receiver.reset(msgData, sender);
                }
            }
        }
    }

    public abstract class ChatViewHolder {
        public View parent;

        public ChatPanel chatpanel;
        public BaseMessage msg;
        public Map<String, ChatPanel> msgPanel = new HashMap<String, ChatPanel>();

        public void reset(BaseMessage msgData, boolean sender) {
            msg = msgData;

            if (chatpanel != null) {
                if (!ChatMsgType.isMatchPanel(chatpanel, msg.getType())) {
                    chatpanel.setVisibility(View.GONE);
                    chatpanel = msgPanel.get(ChatMsgType.getPanelClassName(msg.getType()));

                    if (chatpanel != null) {
                        chatpanel.setVisibility(View.VISIBLE);
                    }
                }
            }

            ChatPanel chatpanelTemp = getChatInstance().chatAdapter.getItemPanel(chatpanel, msgData, chatInstance, sender);
            if (chatpanelTemp != null && this instanceof ChatItemHolder) {
                chatpanelTemp.setInit(msgData);
            }

            if (chatpanelTemp != chatpanel && chatpanelTemp != null) {
                chatpanel = chatpanelTemp;

                msgPanel.put(ChatMsgType.getPanelClassName(msg.getType()), chatpanelTemp);

                if (this instanceof ChatItemHolder) {
                    chatpanelTemp.setChatItemHolder((ChatItemHolder) this);
                }

                adjustMargin(msgData, sender);
                init(sender);
            }
        }

        public abstract void init(boolean sender);

        public void adjustMargin(BaseMessage msgData, boolean sender) {
            ViewGroup layoutVG;

            if (sender) {
                layoutVG = (ViewGroup) parent.findViewById(R.id.chat_item_right_layout);

                if (layoutVG != null) {
                    ViewGroup.LayoutParams layoutVG_LP = layoutVG.getLayoutParams();
                    layoutVG_LP.width = UIUtil.dp2px(msgData.getDisplayWidth());
                    layoutVG.setLayoutParams(layoutVG_LP);
                }
            } else {
                layoutVG = (ViewGroup) parent.findViewById(R.id.chat_item_left_layout);

                if (layoutVG != null) {
                    RelativeLayout.LayoutParams layoutVG_LP = (RelativeLayout.LayoutParams) layoutVG.getLayoutParams();
                    layoutVG_LP.setMargins(0, 0, UIUtil.dp2px(msgData.getDisplayWidth()), 0);
                    layoutVG.setLayoutParams(layoutVG_LP);
                }
            }
        }

        /**
         * 是否显示背景边框
         *
         * @param sender
         * @param viewGroup
         */
        public void showLayout(boolean sender, ViewGroup viewGroup) {
            if (chatpanel == null || viewGroup == null) {
                return;
            }

            if (chatpanel.isShowParentLayout()) {
                if (sender) {
                    viewGroup.setBackgroundResource(R.drawable.y1_talk_box_24x24_me);
                } else {
                    viewGroup.setBackgroundResource(R.drawable.y1_talk_box_24x24);
                }
            } else {
                viewGroup.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }
    }

    public class ChatItemHolder extends ChatViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView name;
        public CircleImageView head;
        public ViewGroup content;
        public TextView status;
        public ImageView statusImg, statusError;
        private ProgressBar statusProgress;

        @Override
        public void init(boolean sender) {
            showLayout(sender, content);
            content.addView(chatpanel.getContentView());

            // 注册事件
            head.setOnClickListener(this);
            status.setOnClickListener(this);
            statusError.setOnClickListener(this);

            content.setOnClickListener(this);
            content.setOnLongClickListener(this);
        }

        @Override
        public void reset(BaseMessage msgData, boolean sender) {
            super.reset(msgData, sender);

            final UserInfoLightweight infoLightweight = getChatInstance().chatAdapter.getUserInfo(msg.getSendID());
            updateHead(infoLightweight, sender);

            if (chatpanel != null) {
                updateStatus(ChatMsgType.getMsgType(msg.getType()), sender);
                chatpanel.reset(msg, infoLightweight);

                showLayout(sender, content);
            } else {
                updateStatus(null, sender);
            }
        }

        private void updateHead(UserInfoLightweight infoLightweight, boolean sender) {
            if (infoLightweight != null) {
//                if (sender) {
//                    name.setVisibility(View.GONE);
//                } else {
//                    name.setText(infoLightweight.getNickname());
//                    name.setVisibility(View.VISIBLE);
//                }

                ImageLoader.loadAvatar(getContext(), infoLightweight.getAvatar(), head);
            } else {
                name.setVisibility(View.GONE);
             //   head.setTag("" + msg.getSendID());
              //    head.setImageResource(ModuleMgr.getCenterMgr().isMan() ? R.drawable.y2_hd_man : R.drawable.y2_hd_woman);
            }
        }

        private void updateStatus(ChatMsgType msgType, boolean sender) {
            if (!sender) {
                status.setVisibility(View.GONE);
                statusProgress.setVisibility(View.GONE);
                statusError.setVisibility(View.GONE);

                if (ChatMsgType.CMT_2 == msgType && msg.getfStatus() == 1) {
                    CommonMessage message = (CommonMessage) msg;
                    if(!TextUtils.isEmpty(message.getVoiceUrl())){
                        statusImg.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                statusImg.setVisibility(View.GONE);
                return;
            }

            statusImg.setVisibility(View.GONE);
            statusError.setVisibility(View.GONE);

            switch (msg.getStatus()) {
                case 1: // 发送成功
                    status.setText("送达");
                    break;

                case 2: // 发送失败
                    status.setText("失败");
                    break;

                case 3: // 发送中
                    status.setText("");
                    break;

                case 11: // 已读
                    status.setText("已读");
                    break;

                case 12: // 审核未通过
                    status.setText("");
                    break;

                default: // "未知状态" + msg.getStatus()
                    status.setText("");
                    break;
            }

            // if(msg.getStatus() == 3 || msg.getStatus() == 2){//发送中, 发送失败
            //显示进度条或发送失败且小于五分钟也显示进度条
//                    if(msg.isValid() && ((msg.getTime() + Constant.CHAT_RESEND_TIME) > ModuleMgr.getAppMgr().getTime())){
//                        statusProgress.setVisibility(View.VISIBLE);
//                        status.setVisibility(View.GONE);
//                        statusError.setVisibility(View.GONE);
//                    }else {
//                        statusProgress.setVisibility(View.GONE);
//                        status.setVisibility(View.GONE);
//                        statusError.setVisibility(View.VISIBLE);
//                    }

            if (msg.getStatus() == 3 && ((msg.getTime() + Constant.CHAT_RESEND_TIME) > ModuleMgr.getAppMgr().getTime())) {//发送中,
                statusProgress.setVisibility(View.VISIBLE);
                status.setVisibility(View.GONE);
                statusError.setVisibility(View.GONE);
            } else if (msg.getStatus() == 2) {//发送失败
                statusProgress.setVisibility(View.GONE);
                status.setVisibility(View.GONE);
                statusError.setVisibility(View.VISIBLE);
            } else {
                statusProgress.setVisibility(View.GONE);
                status.setVisibility(View.VISIBLE);
                statusError.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chat_item_head:
                    if (chatpanel != null) {
                        chatpanel.onClickHead(msg);
                    }
                    break;

                case R.id.chat_item_status:
                    if (chatpanel != null) {
                        chatpanel.onClickStatus(msg);
                    }
                    break;

                case R.id.chat_item_content:
                    if (chatpanel != null) {
                        chatpanel.onClickContent(msg, false);
                    }
                    break;
                case R.id.chat_item_status_error:
                    if (chatpanel != null) {
                        chatpanel.onClickErrorResend(msg);
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.chat_item_head:
                    if (chatpanel != null) {
                        chatpanel.onClickHead(msg);
                    }
                    break;

                case R.id.chat_item_status:
                    if (chatpanel != null) {
                        chatpanel.onClickStatus(msg);
                    }
                    break;

                case R.id.chat_item_content:
                    if (chatpanel != null) {
                        chatpanel.onClickContent(msg, true);
                        return true;
                    }
                    break;
            }
            return false;
        }
    }

    public class ChatCustomHolder extends ChatViewHolder {
        @Override
        public void init(boolean sender) {
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).addView(chatpanel.getContentView());
            }
        }

        @Override
        public void reset(BaseMessage msgData, boolean sender) {
            super.reset(msgData, sender);

            if (chatpanel != null) {
                chatpanel.reset(msgData, null);
            }
        }
    }
}
