package com.juxin.predestinate.ui.mail.item;

/**
 * 显示样式种类
 * Created by Kind on 16/2/2.
 */
public enum MailItemType {

    /**普通，目前是在私聊中用**/
    Mail_Item_Ordinary(0),

    /**其他**/
    Mail_Item_Other(1),

    ;

    public int type;
    MailItemType(int type) {
        this.type = type;
    }

    public static MailItemType getMailMsgType(int type) {
        for (MailItemType mailItemType : MailItemType.values()) {
            if (mailItemType.type == type) {
                return mailItemType;
            }
        }
        return null;
    }
}
