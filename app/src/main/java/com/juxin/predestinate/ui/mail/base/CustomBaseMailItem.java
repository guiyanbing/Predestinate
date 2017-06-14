package com.juxin.predestinate.ui.mail.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.unread.BadgeView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.EmojiTextView;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.ui.mail.item.MailMsgID;

/**
 * item基类
 * Created by Kind on 16/2/3.
 */
public class CustomBaseMailItem extends LinearLayout implements View.OnClickListener {

    private Context context;
    private View contentView;

    public CustomBaseMailItem(Context context) {
        super(context);
        this.context = context;
    }

    public CustomBaseMailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomBaseMailItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void init(int resource) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (resource == -1) {
            contentView = inflater.inflate(R.layout.mail_item_letter, this);
            onCreateView(contentView);
        } else {
            contentView = inflater.inflate(resource, this);
        }
    }

    public View getContentView() {
        return contentView;
    }

    public LinearLayout mail_item_letter;
    public ImageView item_headpic, item_vip, item_ranking_state;
    public TextView item_nickname, item_last_time, item_last_status, item_certification;
    public EmojiTextView item_last_msg;
    public BadgeView item_unreadnum;

    public void onCreateView(View contentView) {
        mail_item_letter = (LinearLayout) contentView.findViewById(R.id.mail_item_letter);
        item_headpic = (ImageView) contentView.findViewById(R.id.mail_item_headpic);
        item_unreadnum = (BadgeView) contentView.findViewById(R.id.mail_item_unreadnum);
        item_nickname = (TextView) contentView.findViewById(R.id.mail_item_nickname);
        item_certification = (TextView) contentView.findViewById(R.id.mail_item_certification);
        item_last_msg = (EmojiTextView) contentView.findViewById(R.id.mail_item_last_msg);
        item_last_time = (TextView) contentView.findViewById(R.id.mail_item_last_time);
        item_last_status = (TextView) contentView.findViewById(R.id.mail_item_last_status);
        item_headpic.setOnClickListener(this);

        item_ranking_state = (ImageView) contentView.findViewById(R.id.mail_item_ranking_state);
        item_vip = (ImageView) contentView.findViewById(R.id.mail_item_vip);
    }

    public void showGap() {
        findViewById(R.id.gap_item).setVisibility(VISIBLE);
    }

    /**
     * 显示数据
     *
     * @param msgData
     */
    public void showData(BaseMessage msgData) {
        long uid = msgData.getLWhisperID();

        if(uid == MailSpecialID.customerService.getSpecialID() && TextUtils.isEmpty(msgData.getAvatar())){
            ImageLoader.loadCircleAvatar(getContext(), R.drawable.f1_secretary_avatar, item_headpic);
        }else {
            ImageLoader.loadRoundAvatar(getContext(), msgData.getAvatar(), item_headpic);
        }

        String nickname = msgData.getName();

        if (!TextUtils.isEmpty(nickname)) {
            item_nickname.setText(nickname.length() <= 10 ? nickname : nickname.substring(0, 9) + "...");
        } else {
            if(uid == MailSpecialID.customerService.getSpecialID()){
                item_nickname.setText(MailSpecialID.customerService.getSpecialIDName());
            }else {
                item_nickname.setText(String.valueOf(uid));
            }
        }

        item_certification.setVisibility(GONE);

        String result = BaseMessage.getContent(msgData);
        if (msgData.getType() == BaseMessage.BaseMessageType.common.getMsgType()) {
            item_last_msg.setTextContent(result);
        } else {
            item_last_msg.setText(Html.fromHtml(result));
        }

        long time = msgData.getTime();
        if (time > 0) {
            item_last_time.setText(TimeUtil.formatBeforeTimeWeek(time));
        } else {
            item_last_time.setText("");
        }
        setUnreadnum(msgData);
        setStatus(msgData);

        setRanking(msgData);
    }

    /**
     * 角标
     * @param msgData
     */
    protected void setUnreadnum(BaseMessage msgData) {
        item_unreadnum.setVisibility(View.GONE);
        if (msgData.getNum() > 0) {
            item_unreadnum.setVisibility(View.VISIBLE);
            item_unreadnum.setText(ModuleMgr.getChatListMgr().getUnreadNum(msgData.getNum()));
        }
    }

    /**
     * 状态
     *
     * @param msgData
     */
    protected void setStatus(BaseMessage msgData) {
        item_last_status.setVisibility(View.GONE);
        if (msgData.getType() == BaseMessage.BaseMessageType.hint.getMsgType() || msgData.getLWhisperID() == MailMsgID.Greet_Msg.type) {
            return;
        }
        if (JsonUtil.getJsonObject(msgData.getJsonStr()).has("fid")) return;
        item_last_status.setVisibility(View.VISIBLE);

        BaseMessage.BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(msgData.getType());
        if (messageType != BaseMessage.BaseMessageType.common && messageType != BaseMessage.BaseMessageType.gift) {
            item_last_status.setVisibility(View.GONE);
            return;
        }
        // 发送成功2.发送失败3.发送中 10.未读11.已读//12未审核通过
        switch (msgData.getStatus()) {
            case 1:
                item_last_status.setText("送达");
                item_last_status.setBackgroundResource(R.drawable.f1_mail_item_delivery);
                break;

            case 2: // 发送失败
                item_last_status.setText("失败");
                item_last_status.setBackgroundResource(R.drawable.f1_mail_item_fall);
                break;

            case 3: // 发送中
                long time = msgData.getCurrentTime() - msgData.getTime();
                if (time <= 90000) {
                    item_last_status.setText("发送中");
                    break;
                }
                item_last_status.setText("失败");
                break;
            case 11: // 已读
                item_last_status.setText("已读");
                item_last_status.setBackgroundResource(R.drawable.f1_mail_item_read);
                break;

            default: // "未知状态" + msg.getStatus()
                item_last_status.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * vip角标
     *
     * @param msgData
     */
    protected void setRanking(BaseMessage msgData) {
        item_vip.setVisibility(ModuleMgr.getCenterMgr().isVip(msgData.getIsVip()) ? View.VISIBLE : View.GONE);

        if (!msgData.isTop()) {
            item_ranking_state.setVisibility(View.GONE);
            return;
        }

        item_ranking_state.setVisibility(View.VISIBLE);
        if (!ModuleMgr.getCenterMgr().getMyInfo().isMan()) {
            item_ranking_state.setImageResource(R.drawable.f1_top02);
        } else {
            item_ranking_state.setImageResource(R.drawable.f1_top01);
        }
    }

    @Override
    public void onClick(View view) {
    }
}