package com.juxin.predestinate.ui.mail.item;

/**
 * 特殊消息消息ID等
 */
public enum MailMsgID {

	/**推荐的人**/
	recommend_msg(1),

	/**最近来访**/
	visitors_msg(2),

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