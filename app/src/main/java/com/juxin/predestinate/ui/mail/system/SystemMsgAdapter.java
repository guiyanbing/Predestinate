package com.juxin.predestinate.ui.mail.system;

import android.widget.ImageView;
import android.widget.TextView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * Created by Kind on 2017/4/11.
 */

public class SystemMsgAdapter extends BaseRecyclerViewAdapter {

    private static final int SYSTEMMSGACT_ITEM_TYPE_TXT = 0;
    private static final int SYSTEMMSGACT_ITEM_TYPE_IMG = 1;

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_mail_systemmsgact_item_text, R.layout.p1_mail_systemmsgact_item_img};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        int itemType = this.getRecycleViewItemType(position);
        BaseMessage baseMessage = (BaseMessage) this.getItem(position);
        TextView sys_item_time = viewHolder.findViewById(R.id.sys_item_time);
        switch (itemType) {
            case SYSTEMMSGACT_ITEM_TYPE_TXT: {
                TextView sys_item_txt_title = viewHolder.findViewById(R.id.sys_item_txt_title);
                TextView sys_item_txt_time = viewHolder.findViewById(R.id.sys_item_txt_time);
                TextView sys_item_txt_content = viewHolder.findViewById(R.id.sys_item_txt_content);
                TextView sys_item_text_title_but = viewHolder.findViewById(R.id.sys_item_text_title_but);


                break;
            }
            case SYSTEMMSGACT_ITEM_TYPE_IMG: {
                TextView sys_item_img_title = viewHolder.findViewById(R.id.sys_item_img_title);
                TextView sys_item_img_time = viewHolder.findViewById(R.id.sys_item_img_time);
                ImageView sys_item_img_pic = viewHolder.findViewById(R.id.sys_item_img_pic);
                TextView sys_item_img_content = viewHolder.findViewById(R.id.sys_item_img_content);
                TextView sys_item_img_but = viewHolder.findViewById(R.id.sys_item_img_but);

                break;
            }

        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
         if (position % 2 == 0) {
            return SYSTEMMSGACT_ITEM_TYPE_IMG;
        } else {
            return SYSTEMMSGACT_ITEM_TYPE_TXT;
        }
    }
}