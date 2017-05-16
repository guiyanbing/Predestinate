package com.juxin.predestinate.module.util.my;

import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.bean.my.SendGiftResultInfo;


/**
 * Created by zm on 2017/5/9.
 */

public class GiftHelper {

    public interface OnSendGiftCallback{
        void onSendGiftCallback(boolean isOk,GiftsList.GiftInfo giftInfo,SendGiftResultInfo resultInfo);
    }

    public interface OnRequestGiftListCallback{
        void onRequestGiftListCallback(boolean isOk);
    }
}
