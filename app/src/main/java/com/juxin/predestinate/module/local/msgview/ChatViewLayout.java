package com.juxin.predestinate.module.local.msgview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.utils.TypeConvertUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.msgview.chatview.ChatBasePanel;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatContentAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatExtendPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatInputPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatRecordPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatSmilePanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.intercept.InterceptTouchLinearLayout;
import com.juxin.predestinate.module.logic.baseui.intercept.OnInterceptTouchEventLayout;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;
import com.juxin.predestinate.module.logic.config.Constant;

/**
 * Created by Kind on 2017/3/30.
 */
public class ChatViewLayout extends LinearLayout implements InterceptTouchLinearLayout.OnInterceptTouchEvent {

    private ChatAdapter.ChatInstance chatInstance = null;
    private ViewGroup chatFixedTip = null;
    private ViewGroup chatFloatTip = null;
    private ImageView input_giftview, input_look_at_her, iv_y_tips_close;
    private LinearLayout ll_y_tips,ll_y_left_tips;
    public TextView tv_y_tips_count;
    private TextView tv_y_tips_buy, tv_y_tips_split;

    public ChatViewLayout(Context context) {
        super(context);
        initView();
    }

    public ChatViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @SuppressLint("NewApi")
    public ChatViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        if (chatInstance == null) {
            chatInstance = new ChatAdapter.ChatInstance();
            chatInstance.context = getContext();
            chatInstance.chatViewLayout = this;
            chatInstance.chatAdapter = new ChatAdapter();
            chatInstance.chatAdapter.setChatInstance(chatInstance);
        }

        chatInstance.chatContentAdapter = null;
        chatInstance.chatInputPanel = null;
        chatInstance.chatExtendPanel = null;

        ViewGroup viewGroup;
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.p1_chat_content, this);

        chatFixedTip = (ViewGroup) contentView.findViewById(R.id.chat_fixed_tip);
        chatFloatTip = (ViewGroup) contentView.findViewById(R.id.chat_float_tip);
        input_look_at_her = (ImageView) contentView.findViewById(R.id.input_look_at_her);
        input_giftview = (ImageView) contentView.findViewById(R.id.input_giftview);
        ll_y_tips = (LinearLayout) contentView.findViewById(R.id.ll_y_tips);
        ll_y_left_tips = (LinearLayout) contentView.findViewById(R.id.ll_y_left_tips);
        tv_y_tips_count = (TextView) contentView.findViewById(R.id.tv_y_tips_count);
        tv_y_tips_buy = (TextView) contentView.findViewById(R.id.tv_y_tips_buy);
        tv_y_tips_split = (TextView) contentView.findViewById(R.id.tv_y_tips_split);
        iv_y_tips_close = (ImageView) contentView.findViewById(R.id.iv_y_tips_close);

        yTipsLogic(false, false);
        // 最外层
        viewGroup = (ViewGroup) contentView.findViewById(R.id.chat_content_layout);
        if (viewGroup instanceof OnInterceptTouchEventLayout) {
            ((OnInterceptTouchEventLayout) viewGroup).setOnInterceptTouchEvent(this);
        }

        // 语音录制
        chatInstance.chatRecordPanel = new ChatRecordPanel(getContext(), chatInstance);
        viewGroup.addView(chatInstance.chatRecordPanel.getContentView());

        // 聊天输入
        viewGroup = (ViewGroup) contentView.findViewById(R.id.chat_content_input);
        new ChatInputPanel(getContext(), chatInstance);
        viewGroup.addView(chatInstance.chatInputPanel.getContentView());

        // 扩展、表情
        viewGroup = (ViewGroup) contentView.findViewById(R.id.chat_content_extend);
        new ChatExtendPanel(getContext(), chatInstance);
        viewGroup.addView(chatInstance.chatExtendPanel.getContentView());
        if (getContext() instanceof BaseActivity) {//添加右滑退出忽略view
            ((BaseActivity) getContext()).addIgnoredView(viewGroup);
        }

        new ChatSmilePanel(getContext(), chatInstance);
        viewGroup.addView(chatInstance.chatSmilePanel.getContentView());

        ChatContentAdapter chatContentAdapter = new ChatContentAdapter(getContext(), null);
        chatContentAdapter.setChatInstance(chatInstance);
        ExListView list = (ExListView) contentView.findViewById(R.id.chat_content_list);
        list.setHeaderStr(chatInstance.context.getString(R.string.xlistview_header_hint_normal),
                chatInstance.context.getString(R.string.xlistview_header_hint_loading));

        chatInstance.chatListView = list;
        list.setAdapter(chatContentAdapter);
        list.setXListViewListener(chatInstance.chatAdapter);

        chatInstance.chatContentAdapter = chatContentAdapter;
    }

    /**
     * Y币提示逻辑
     *
     * @param isShow  是否显示
     * @param isClose 是否点了关闭提示
     */
    public void yTipsLogic(boolean isShow, boolean isClose) {
        //提示条：男非VIP隐藏，女隐藏
        if (!ModuleMgr.getCenterMgr().getMyInfo().isMan() || (ModuleMgr.getCenterMgr().getMyInfo().isMan() && !ModuleMgr.getCenterMgr().getMyInfo().isVip()) || (!isShow || isClose)) {
            ll_y_tips.setVisibility(View.GONE);
            return;
        }
        int yCoin = ModuleMgr.getCenterMgr().getMyInfo().getYcoin();
        boolean isCloseYTips = PSP.getInstance().getBoolean(ModuleMgr.getCommonMgr().getPrivateKey(Constant.CLOSE_Y_TIPS_VALUE), false);
        if (yCoin < 100) {
            tv_y_tips_split.setVisibility(View.GONE);
            iv_y_tips_close.setVisibility(View.GONE);
            ll_y_tips.setVisibility(View.VISIBLE);
            changeYTipsCount(yCoin);
        } else {
            if (isCloseYTips) {
                ll_y_tips.setVisibility(View.GONE);
            } else {
                ll_y_tips.setVisibility(View.VISIBLE);
                changeYTipsCount(yCoin);
            }
        }
    }

    /**
     * 改变Y值
     */
    private void changeYTipsCount(int yCoin) {
        if (tv_y_tips_count == null) return;
        tv_y_tips_count.setText(Html.fromHtml(getResources().getString(R.string.chat_y_tips, yCoin)));
    }

    /**
     * 获取ChatAdapter相关的实例。
     *
     * @return 实例集合。
     */
    public ChatAdapter getChatAdapter() {
        return chatInstance.chatAdapter;
    }


    public ImageView getInputLookAtHer() {
        return input_look_at_her;
    }

    /**
     * 设置ChatAdapter相关的实例。
     *
     * @param chatAdapter 实例集合。
     */
    public void setChatAdapter(ChatAdapter chatAdapter) {
        this.chatInstance.chatAdapter = chatAdapter;
    }

    public void onClickChatGift(View.OnClickListener listener) {
        input_giftview.setOnClickListener(listener);
    }

    public void onClickLookAtHer(View.OnClickListener listener) {
        input_look_at_her.setOnClickListener(listener);
    }

    public void onClickYTipsBuy(View.OnClickListener listener) {
        ll_y_left_tips.setOnClickListener(listener);
    }

    public void onClickYTipsClose(View.OnClickListener listener) {
        iv_y_tips_close.setOnClickListener(listener);
    }

    /**
     * 右上角添加固定提示。
     *
     * @param chatPanel 需要显示的面板，必须有效，不能为null。
     */
    public void addFixedPanel(ChatPanel chatPanel) {
        final View view = chatPanel.getContentView();
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.down_in);
        chatFixedTip.addView(view);
        view.startAnimation(animation);

        handler.removeMessages(0);

        if (chatFixedTip.getChildCount() > 1) {
            removeFirstView();
        }

        handler.sendEmptyMessageDelayed(0, 3000);
        PLogger.d("");
    }

    public void removeFirstView() {
        PLogger.d("" + chatFixedTip.getChildCount());

        if (chatFixedTip.getChildCount() > 0) {
            View view = chatFixedTip.getChildAt(0);

            Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.up_out);
            animation.setFillAfter(true);
            view.startAnimation(animation);
            handler.sendEmptyMessageDelayed(1, 500);
        }
    }

    /**
     * 在聊天面板添加悬浮界面。
     *
     * @param chatPanel
     */
    public void addFloatView(ChatBasePanel chatPanel) {
        chatFloatTip.setVisibility(View.GONE);
        chatFloatTip.addView(chatPanel.getContentView());
    }

    /**
     * 删除聊天面板的悬浮界面。
     *
     * @param chatPanel
     */
    public void removeFloatView(ChatBasePanel chatPanel) {
        chatFloatTip.removeView(chatPanel.getContentView());

        if (chatFloatTip.getChildCount() == 0) {
            chatFloatTip.setVisibility(View.INVISIBLE);
        }
    }

    public void setInputGiftviewVisibility(int visibility) {
        input_giftview.setVisibility(visibility);
    }

    public void setInputLookAtHerVisibility(int visibility) {
        input_look_at_her.setVisibility(visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (chatInstance != null && chatInstance.chatAdapter != null) {
            chatInstance.chatAdapter.detach();
        }

        super.onDetachedFromWindow();
    }

    private boolean disableIntercept = false;

    /**
     * 是否关闭自动关闭输入框。
     *
     * @param disableIntercept
     */
    public void setDisableIntercept(boolean disableIntercept) {
        this.disableIntercept = disableIntercept;
    }

    @Override
    public void interceptTouchEvent(MotionEvent ev) {
        try {
            if (!disableIntercept && ev.getAction() == MotionEvent.ACTION_DOWN) {
                getChatAdapter().closeAllInput();
            }
        } catch (Exception e) {

        }
    }

    private Handler handler = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    removeFirstView();
                    break;
                case 1:
                    if (chatFixedTip.getChildCount() != 0) {
                        chatFixedTip.removeViewAt(0);
                    }
                    break;
            }
        }
    };
}
