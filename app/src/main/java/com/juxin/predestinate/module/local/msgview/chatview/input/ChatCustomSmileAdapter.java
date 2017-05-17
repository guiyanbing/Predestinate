package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import java.util.List;

public class ChatCustomSmileAdapter extends ExBaseAdapter<SmileItem> {
    public ChatCustomSmileAdapter(Context context, List<SmileItem> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflate(R.layout.p1_chat_custom_smile_grid_item);
            vh.chat_custom_smile = (ImageView) convertView.findViewById(R.id.chat_custom_smile);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        SmileItem smileItem = getItem(position);
        if("custom".equals(smileItem.getPic())){
            vh.chat_custom_smile.setBackgroundResource(R.drawable.f1_bt_add_emoji);
        }else {
            ImageLoader.loadAvatar(getContext(), smileItem.getPic(), vh.chat_custom_smile);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView chat_custom_smile;
    }
}