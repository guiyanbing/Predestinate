package com.juxin.predestinate.module.local.mail;

/**
 *  小友客服-特殊号
 * Created by Kind on 16/3/25.
 */
public enum MailSpecialID {

    /**小友客服**/
    customerService(9999, "小友客服"),

    activity(8000, "活动");

    public long specialID;
    public String specialIDName;
    MailSpecialID(long specialID, String specialIDName) {
        this.specialID = specialID;
        this.specialIDName = specialIDName;
    }

    public static boolean getMailSpecialID(long specialID) {
        for (MailSpecialID tmp : MailSpecialID.values()) {
            if (tmp.specialID == specialID) {
                return true;
            }
        }
        return false;
    }

    public long getSpecialID() {
        return this.specialID;
    }

    public String getSpecialIDName() {
        return this.specialIDName;
    }
}
