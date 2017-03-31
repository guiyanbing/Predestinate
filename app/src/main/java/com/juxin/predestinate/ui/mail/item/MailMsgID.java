package com.juxin.predestinate.ui.mail.item;

/**
 * 特殊消息消息ID等
 */
public enum MailMsgID {

	/**邀请好友**/
	invite_friends_msg(1),

	/**活动**/
	act_msg(8000),

	/**新朋友**/
	new_friend_msg(2),

	/**最近来访**/
	visitors_msg(3);

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