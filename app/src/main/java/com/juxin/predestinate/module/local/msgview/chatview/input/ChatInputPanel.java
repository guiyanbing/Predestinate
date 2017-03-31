package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juxin.library.utils.InputUtils;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatViewPanel;
import com.juxin.predestinate.module.local.msgview.utils.EggUtil;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;

/**
 * Created by Kind on 2017/3/30.
 */
public class ChatInputPanel extends ChatViewPanel implements View.OnClickListener, View.OnTouchListener, TextWatcher {
    private View chatBtnVoice = null;
    private View chatBtnText = null;

    private View chatBtnExpression = null;

    private TextView chatVoiceRecord = null;
    private EditText chatTextEdit = null;

    private View chatBtnExtend = null;
    private View chatBtnSend = null;

    private View bgline = null;
    private View bg = null;
    private View input_monthly = null;
    private View chat_rel;

    private ChatBigSmileMatchingPanel matchingPanel = null;

    public ChatInputPanel(Context context, ChatAdapter.ChatInstance chatInstance) {
        super(context, chatInstance);

        chatInstance.chatInputPanel = this;
        setContentView(R.layout.p1_chat_input);
        initView();
    }

    /**
     * 初始化所有需要显示的View。
     */
    public void initView() {
        bgline = findViewById(R.id.bg_line);
        bg = findViewById(R.id.bg);
        input_monthly = findViewById(R.id.input_monthly);

        chatBtnVoice = findViewById(R.id.chat_voice);
        chatBtnText = findViewById(R.id.chat_text);

        chatBtnExpression = findViewById(R.id.chat_expression);

        chatVoiceRecord = (TextView) findViewById(R.id.chat_voice_record);
        chatTextEdit = (EditText) findViewById(R.id.chat_text_edit);

        chat_rel = findViewById(R.id.chat_rel);

        chatBtnExtend = findViewById(R.id.chat_extend);
        chatBtnSend = findViewById(R.id.chat_send);

        input_monthly.setOnClickListener(this);
        chatBtnVoice.setOnClickListener(this);
        chatBtnText.setOnClickListener(this);

        chatBtnExpression.setOnClickListener(this);

        chatVoiceRecord.setOnClickListener(this);
//        chatTextEdit.setOnClickListener(this);

        chatTextEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClickChatTextEdit();
                }
                return false;
            }
        });
        chatBtnExtend.setOnClickListener(this);
        chatBtnSend.setOnClickListener(this);

        chatVoiceRecord.setOnTouchListener(this);
        chatTextEdit.addTextChangedListener(this);

        showSendBtn(false);
    }

    /**
     * 添加一个表情到输入框中。
     *
     * @param simleItem 表情文件。
     */
    public void addSmile(EmojiPack.EmojiItem simleItem) {
        try {
            if (!simleItem.isDeleteBtn()) {
                chatTextEdit.append(ChatSmile.getSmiledText(getContext(), simleItem.key, UIUtil.dp2px(20)));
            } else {
                // 删除文字或者表情
                if (!TextUtils.isEmpty(chatTextEdit.getText())) {

                    int selectionStart = chatTextEdit.getSelectionStart();// 获取光标的位置
                    if (selectionStart > 0) {
                        String body = chatTextEdit.getText().toString();
                        String tempStr = body.substring(0, selectionStart);
                        int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                        if (i != -1) {
                            CharSequence cs = tempStr.substring(i, selectionStart);
                            if (ChatSmile.containsKey(cs.toString()))
                                chatTextEdit.getEditableText().delete(i, selectionStart);
                            else
                                chatTextEdit.getEditableText().delete(selectionStart - 1, selectionStart);
                        } else {
                            chatTextEdit.getEditableText().delete(selectionStart - 1, selectionStart);
                        }
                    }
                }

            }
        } catch (Exception e) {
        }
    }

    public void showSendBtn() {
        try {
            String context = chatTextEdit.getText().toString();
            context = context.trim();


            showSendBtn(!TextUtils.isEmpty(context));
        } catch (Exception e) {
        }
    }

    /**
     * 显示或者隐藏输入按钮。
     *
     * @param show 是否显示输入按钮。
     */
    private void showSendBtn(boolean show) {
        if (!getChatInstance().chatAdapter.isShowExtend()) {
            chatBtnExtend.setVisibility(View.GONE);
            chatBtnSend.setVisibility(View.VISIBLE);
            return;
        }

        if (show) {
            chatBtnSend.setVisibility(View.VISIBLE);
            chatBtnExtend.setVisibility(View.GONE);
        } else {
            chatBtnExtend.setVisibility(View.VISIBLE);
            chatBtnSend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_voice:
                onClickChatVoice();
                break;
            case R.id.chat_text:
                onClickChatText();
                break;
            case R.id.chat_expression:
                showChatExpression();
                break;
            case R.id.chat_voice_record:
                onClickChatVoiceRecord();
                break;
            case R.id.chat_text_edit:
                onClickChatTextEdit();
                break;
            case R.id.chat_extend:
                onClickChatExtend();
                break;
            case R.id.chat_send:
                onClickChatSend();
                break;
            case R.id.input_monthly:
                //  UIShow.showGoodsMonthlyLetterAct((Activity) getContext(), UIHelper.PAY_ACT);
            //    UIShow.showMonthMailAct((Activity) getContext());

                break;
        }
    }

    private String channelId = "";
    private String whisperId = "";
    private ChatRecordPanel chatRecordPanel = null;
    private long timeCount = 0;//计时，防止用户快速点击时MediaRecord未初始化引起的界面卡死

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        MMLog.autoDebug(event.getAction());

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                chatVoiceRecord.setText("松开 发送");
                chatVoiceRecord.setPressed(true);

                channelId = getChatInstance().chatAdapter.getChannelId();
                whisperId = getChatInstance().chatAdapter.getWhisperId();

                chatRecordPanel = getChatInstance().chatRecordPanelUser;

                if (chatRecordPanel == null) {
                    chatRecordPanel = getChatInstance().chatRecordPanel;
                }

                if (timeCount == 0 || System.currentTimeMillis() - timeCount > 500) {
                    chatRecordPanel.onTouch(action, 0f);
                } else {
                    MMLog.autoDebug("---ChatInputPanel--->点击间隔<500ms，过于频繁");
                }
                break;

            case MotionEvent.ACTION_MOVE:
                chatRecordPanel.onTouch(action, event.getY());
                break;

            case MotionEvent.ACTION_UP:
                chatVoiceRecord.setText("按下 开始");
                chatVoiceRecord.setPressed(false);
                chatRecordPanel.onTouch(action, event.getY(), channelId, whisperId);
                timeCount = System.currentTimeMillis();
                break;

            default:
                chatVoiceRecord.setText("按下 开始");
                chatVoiceRecord.setPressed(false);
                getChatInstance().chatRecordPanel.onTouch(action, 0f);
                timeCount = System.currentTimeMillis();
                return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        sendSystemMsgTyping();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean empty = TextUtils.isEmpty(s);

        showSendBtn(!empty);
        showMatchingSmile(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > Constant.CHAT_TEXT_LIMIT) {
            MMToast.showShort("字数超出限制");
        }
    }

    public void sendSystemMsgTyping() {
        if (getChatInstance().chatAdapter.isTyping()) {
            getChatInstance().chatAdapter.setTyping(false);
       //     ModuleMgr.getMsgCommonMgr().sendSystemMsgTyping(getChatInstance().chatAdapter.getWhisperId(), "", getChatInstance().chatAdapter.getIsKF_ID(), null);
        }
    }

    public void sendSystemMsgCancelInput() {
        if (!getChatInstance().chatAdapter.isTyping()) {
            getChatInstance().chatAdapter.setTyping(true);
        //    ModuleMgr.getMsgCommonMgr().sendSystemMsgCancelInput(getChatInstance().chatAdapter.getWhisperId(), "", getChatInstance().chatAdapter.getIsKF_ID(), null);
        }
    }

    /**
     * 调用前，key必须用TextUtils.isEmpty(key)进行判断。
     *
     * @param key
     */
    public void showMatchingSmile(CharSequence key) {
        if (matchingPanel == null) {
            matchingPanel = new ChatBigSmileMatchingPanel(getContext(), getChatInstance());
            getChatInstance().chatViewLayout.addFloatView(matchingPanel);
        }

        matchingPanel.reset(key);
    }

    /**
     * 切换到语音输入模式。0
     */
    private void onClickChatVoice() {
        chatBtnExpression.setVisibility(View.GONE);

        chatBtnVoice.setVisibility(View.INVISIBLE);
        chatTextEdit.setVisibility(View.GONE);

        chatBtnText.setVisibility(View.VISIBLE);

        chatVoiceRecord.setVisibility(View.VISIBLE);

        chat_rel.setVisibility(View.GONE);

        showSendBtn(false);
        closeAllInput();
    }

    /**
     * 切换到文本输入模式。
     */
    private void onClickChatText() {
        chatBtnExpression.setVisibility(View.GONE);

        chatBtnVoice.setVisibility(View.VISIBLE);
        chatTextEdit.setVisibility(View.VISIBLE);

        chatBtnText.setVisibility(View.INVISIBLE);

        chatVoiceRecord.setVisibility(View.INVISIBLE);

        chat_rel.setVisibility(View.VISIBLE);

        showSendBtn();
    }

    /**
     * 打开表情面板。
     */
    public void showChatExpression() {
        InputUtils.HideKeyboard(chatTextEdit);
        getChatInstance().chatExtendPanel.show(false);
        getChatInstance().chatSmilePanel.showToggle();
    }

    /**
     * 发送语音消息，走OnTouch事件，此处不起作用。
     */
    private void onClickChatVoiceRecord() {
    }

    private void onClickChatTextEdit() {
        getChatInstance().chatExtendPanel.show(false);
        getChatInstance().chatSmilePanel.show(false);

        showMatchingSmile(chatTextEdit.getText().toString());

        // 键盘弹出需要时间。
        MsgMgr.getInstance().sendMsg(new Runnable() {
            @Override
            public void run() {
                getChatInstance().chatAdapter.moveToBottom();
            }
        }, 500);
    }

    /**
     * 打开扩展功能框。
     */
    private void onClickChatExtend() {
        onClickChatText();

        InputUtils.HideKeyboard(chatTextEdit);
        getChatInstance().chatExtendPanel.show(true);
        getChatInstance().chatSmilePanel.show(false);
    }

    public void closeAllInput() {
        try {
            sendSystemMsgCancelInput();
            InputUtils.HideKeyboard(chatTextEdit);
            getChatInstance().chatExtendPanel.show(false);
            getChatInstance().chatSmilePanel.show(false);

            if (matchingPanel != null) {
                matchingPanel.show(false);
            }
        } catch (Exception e) {
            MMLog.printThrowable(e);
        }
    }

    /**
     * 发送消息。
     */
    private void onClickChatSend() {
        try {
            String context = chatTextEdit.getText().toString();

            if ("         ".equals(context)) {
                if (EggUtil.getInstance().showEgg(getChatInstance().chatAdapter)) {
                    return;
                }
            }

            context = context.trim();

            if (TextUtils.isEmpty(context)) {
                return;
            }

            if (context.length() > Constant.CHAT_TEXT_LIMIT) {
                MMToast.showShort("字数超出限制,请分条发送.");
                return;
            }

            String channelId = getChatInstance().chatAdapter.getChannelId();
            String whisperID = getChatInstance().chatAdapter.getWhisperId();

//            if (!ModuleMgr.getCommonMgr().headRemindOnChat()) {
//                return;
//            }
//            ModuleMgr.getChatMgr().sendTextMsg(getChatInstance().chatAdapter.getFolder(), channelId, whisperID, context);

            chatTextEdit.setText("");
            sendSystemMsgCancelInput();

            //      MediaUtils.playSound(R.raw.send_msg);
        } catch (Exception e) {
            MMLog.printThrowable(e);
        }
    }

    /**
     * 清空输入框内容。
     */
    public void clearInputEdit() {
        chatTextEdit.setText("");
    }

    public void setBg(int bgColor, int bgLineColor) {
        this.bg.setBackgroundResource(bgColor);
        this.bgline.setBackgroundResource(bgLineColor);
    }

    /**
     * 设置输入内容。
     *
     * @param text
     */
    public void setInputText(String text) {
        chatTextEdit.setText(text);
        chatTextEdit.setSelection(text.length());
        chatTextEdit.requestFocus();
    }


    public EditText getChatTextEdit() {
        return chatTextEdit;
    }

    /**
     * 是否可以看信
     */
    public void showChat(boolean isCanChat) {
        if (!isCanChat) {//不能回复信息
            input_monthly.setVisibility(View.VISIBLE);
            bg.setVisibility(View.GONE);
        } else {//能回复信息
            input_monthly.setVisibility(View.GONE);
            bg.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 如果是小秘书都不显示
     */
    public void showInputGONE() {
        input_monthly.setVisibility(View.GONE);
        bg.setVisibility(View.GONE);
    }
}