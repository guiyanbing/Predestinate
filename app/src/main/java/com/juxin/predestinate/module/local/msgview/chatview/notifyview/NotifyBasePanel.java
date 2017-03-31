package com.juxin.predestinate.module.local.msgview.chatview.notifyview;

import android.content.Context;

import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

public abstract class NotifyBasePanel extends BaseViewPanel {
    public NotifyBasePanel(Context context) {
        super(context);
    }

    /**
     * 初始化所有需要显示的View。
     */
    public abstract void initView();

    /**
     * 用消息数据重新初始化显示View。
     *
     * @param msgData 最新的消息数据。
     */
    public abstract void reset(SimpleMsg msgData);
}
