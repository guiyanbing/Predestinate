package com.juxin.predestinate.ui.mail.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.EmojiTextView;
import com.juxin.predestinate.module.util.TimeUtil;

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

    public View getContentView(){
        return contentView;
    }

    public LinearLayout mail_item_letter, item_ranking_state;
    public ImageView item_headpic, item_vip;
    public TextView item_nickname, item_last_time, item_online, item_last_status,
            item_certification, item_ranking_type, item_ranking_level;
    public EmojiTextView item_last_msg;
    public BadgeView item_unreadnum;

    public void onCreateView(View contentView) {
        mail_item_letter = (LinearLayout) contentView.findViewById(R.id.mail_item_letter);
        item_headpic = (ImageView) contentView.findViewById(R.id.mail_item_headpic);
        item_unreadnum = (BadgeView) contentView.findViewById(R.id.mail_item_unreadnum);
        item_online = (TextView) contentView.findViewById(R.id.mail_item_online);
        item_nickname = (TextView) contentView.findViewById(R.id.mail_item_nickname);
        item_certification = (TextView) contentView.findViewById(R.id.mail_item_certification);
        item_last_msg = (EmojiTextView) contentView.findViewById(R.id.mail_item_last_msg);
        item_last_time = (TextView) contentView.findViewById(R.id.mail_item_last_time);
        item_last_status = (TextView) contentView.findViewById(R.id.mail_item_last_status);
        item_headpic.setOnClickListener(this);
        // mail_item_letter.setOnClickListener(this);

        item_ranking_state = (LinearLayout) contentView.findViewById(R.id.mail_item_ranking_state);
        item_ranking_type = (TextView) contentView.findViewById(R.id.mail_item_ranking_type);
        item_ranking_level = (TextView) contentView.findViewById(R.id.mail_item_ranking_level);
        item_vip = (ImageView) contentView.findViewById(R.id.mail_item_vip);
    }

    public void showGap(){
        findViewById(R.id.gap_item).setVisibility(VISIBLE);
    }

    /**
     * 显示数据
     *
     * @param msgData
     */
    public void showData(BaseMessage msgData) {
        ImageLoader.loadAvatar(getContext(), msgData.getAvatar(), item_headpic);

        String nickname = msgData.getName();
        PLogger.printObject("nickname===1" + nickname);
        PLogger.printObject("nickname===2" + msgData.getAvatar());
        if (!TextUtils.isEmpty(nickname)) {
            item_nickname.setText(nickname.length() <= 10 ? nickname : nickname.substring(0, 9) + "...");
        } else {
            item_nickname.setText(String.valueOf(msgData.getLWhisperID()));
        }

        item_certification.setVisibility(GONE);
//        if(msgData.getLWhisperID() == MailSpecialID.customerService.getSpecialID()){
//            item_certification.setVisibility(VISIBLE);
//            item_certification.setText("官方");
//        }

        item_last_msg.setText(BaseMessage.getContent(msgData));
        long time = msgData.getTime();
        if (time > 0) {
            item_last_time.setText(TimeUtil.formatBeforeTimeWeek(time));
        } else {
            item_last_time.setText("");
        }

//        if (ModuleMgr.getCenterMgr().isOnline(msgData.getIsOnline())) {
//            item_online.setVisibility(View.VISIBLE);
//        } else {
//            item_online.setVisibility(View.GONE);
//        }
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
     * @param msgData
     */
    protected void setStatus(BaseMessage msgData) {
        if (msgData.getType() == BaseMessage.BaseMessageType.hint.getMsgType()) {
            item_last_status.setVisibility(View.GONE);
            return;
        }
        item_last_status.setVisibility(View.GONE);
        if (msgData.getStatus() == 0) {
            item_last_status.setText("已读");
            item_last_status.setVisibility(View.VISIBLE);
        }
    }

    protected void setRanking(BaseMessage msgData) {
        item_vip.setVisibility( ModuleMgr.getCenterMgr().isVip(msgData.getIsVip()) ? View.VISIBLE : View.GONE);

        if (!msgData.isTop()){
            item_ranking_state.setVisibility(View.GONE);
            return;
        }

        item_ranking_state.setVisibility(View.VISIBLE);
        if (!ModuleMgr.getCenterMgr().getMyInfo().isMan()) {
            item_ranking_state.setBackgroundResource(R.drawable.f1_ranking_bg_m);
            item_ranking_type.setText(context.getString(R.string.top_type_man));
            item_ranking_level.setText("TOP " + msgData.getTop());
        } else {
            item_ranking_state.setBackgroundResource(R.drawable.f1_ranking_bg_w);
            item_ranking_type.setText(context.getString(R.string.top_type_woman));
            item_ranking_level.setText("TOP " + msgData.getTop());
        }
    }

    @Override
    public void onClick(View v) {}
}