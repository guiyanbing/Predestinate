package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.smile.GiftItem;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import java.util.List;

/**
 * 根据表情的文件名，将表情图片显示到表情面板。
 */
public class ChatGiftSmileAdapter extends ExBaseAdapter<GiftItem> {
    private ChatAdapter chatAdapter = null;

    public ChatGiftSmileAdapter(Context context, List<GiftItem> datas, ChatAdapter chatAdapter) {
        super(context, datas);

        this.chatAdapter = chatAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;

        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflate(R.layout.p1_chat_gift_smile_grid_item);

            vh.gift = (ImageView) convertView.findViewById(R.id.chat_gift);
            vh.name = (TextView) convertView.findViewById(R.id.chat_gift_name);
            vh.price = (TextView) convertView.findViewById(R.id.chat_gift_price);
            vh.info = (TextView) convertView.findViewById(R.id.chat_gift_info);
            vh.mask = convertView.findViewById(R.id.mask_view);

            vh.mask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        GiftItem item = getItem(position);

//        ModuleMgr.getSmileMgr().reqImage(vh.gift, item.getImg());
//        vh.name.setText(item.getName());
//        vh.price.setText("" + item.getPrice() + "钻石");
//        vh.info.setText(item.getInfo());
//
//
//        if (ModuleMgr.getSmileMgr().getGiftLevel(chatAdapter.getLWhisperId()) < item.getLevel()) {
//            vh.mask.setVisibility(View.VISIBLE);
//        } else {
//            vh.mask.setVisibility(View.GONE);
//        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView gift;
        public TextView name;
        public TextView price;
        public TextView info;
        public View mask;
    }
}
