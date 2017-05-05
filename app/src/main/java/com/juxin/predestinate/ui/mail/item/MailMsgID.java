package com.juxin.predestinate.ui.mail.item;

/**
 * 特殊消息消息ID等
 */
public enum MailMsgID {

	/**谁关注我**/
	WhoAttentionMe_Msg(1),

	/**我的好友**/
	MyFriend_Msg(2),

	;

	public long type;
	MailMsgID(long type) {
		this.type = type;
	}
	
	public static MailMsgID getMailMsgID(long type) {
		for (MailMsgID mailMsgID : MailMsgID.values()) {
			if (mailMsgID.type == type) {
				return mailMsgID;
			}
		}
		return null;
	}
}