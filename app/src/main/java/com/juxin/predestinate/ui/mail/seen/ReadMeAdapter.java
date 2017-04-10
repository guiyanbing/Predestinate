package com.juxin.predestinate.ui.mail.seen;

import android.widget.ImageView;
import android.widget.TextView;
import com.juxin.library.view.CircularCoverView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * Created by Kind on 2017/4/10.
 */

public class ReadMeAdapter extends BaseRecyclerViewAdapter {


    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_mail_readmeact_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        BaseMessage baseMessage = (BaseMessage) this.getItem(position);

        CircularCoverView readme_item_head = viewHolder.findViewById(R.id.readme_item_head);
        TextView readme_item_nickname = viewHolder.findViewById(R.id.readme_item_nickname);
        ImageView readme_item_identity = viewHolder.findViewById(R.id.readme_item_identity);
        TextView readme_item_online_time = viewHolder.findViewById(R.id.readme_item_online_time);
        TextView readme_item_introduction = viewHolder.findViewById(R.id.readme_item_introduction);
        TextView readme_item_sign = viewHolder.findViewById(R.id.readme_item_sign);

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}
