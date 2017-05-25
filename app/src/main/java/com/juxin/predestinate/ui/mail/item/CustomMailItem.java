package com.juxin.predestinate.ui.mail.item;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.ui.mail.base.CustomBaseMailItem;
import com.juxin.predestinate.ui.mail.base.CustomLetterMailItem;
import com.juxin.predestinate.ui.mail.base.CustomOtherMailItem;

/**
 * Created by Kind on 16/2/2.
 */
public class CustomMailItem extends LinearLayout {

    private Context context;
    private CustomFrameLayout customFrameLayout;

    public CustomMailItem(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomMailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomMailItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.p1_custom_mail_item, this);
        customFrameLayout = (CustomFrameLayout) view.findViewById(R.id.customMailFrameLayout);
        customFrameLayout.setList(new int[]{R.id.chat_fragment_item_letter, R.id.chat_fragment_item_act});
    }

    public void onCreateView() {
        getItemLetterView();
        getItemActView();
    }

    /**
     * 模板1
     * 获得私聊view
     *
     * @return
     */
    CustomLetterMailItem customLetterMailItem;
    public CustomBaseMailItem getItemLetterView() {
        customLetterMailItem = (CustomLetterMailItem) customFrameLayout.findViewById(R.id.chat_fragment_item_letter);
        customLetterMailItem.init();
        return customLetterMailItem;
    }

    /**
     * 显示私聊类型
     */
    public void showItemLetter(BaseMessage msgData) {
        customFrameLayout.show(R.id.chat_fragment_item_letter);
        customLetterMailItem.showData(msgData);
    }

    public void showLetterGap() {
        customLetterMailItem.showGap();
    }

    /**
     * 模板2
     *
     * @return
     */
    CustomOtherMailItem customActMailItem;

    public CustomBaseMailItem getItemActView() {
        customActMailItem = (CustomOtherMailItem) customFrameLayout.findViewById(R.id.chat_fragment_item_act);
        customActMailItem.init();
        return customActMailItem;
    }

    /**
     * 显示私聊类型
     */
    public void showItemAct(BaseMessage msgData) {
        customFrameLayout.show(R.id.chat_fragment_item_act);
        customActMailItem.showData(msgData);
    }

    public void showActGap() {
        customActMailItem.showGap();
    }

    /**
     * 隐藏所有
     */
    public void GoneAll() {
        customFrameLayout.GoneAll();
    }
}
