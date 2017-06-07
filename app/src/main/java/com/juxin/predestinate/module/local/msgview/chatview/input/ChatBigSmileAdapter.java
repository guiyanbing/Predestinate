package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import java.util.List;

/**
 * 根据表情的文件名，将表情图片显示到表情面板。
 */
public class ChatBigSmileAdapter extends ExBaseAdapter<SmileItem> {
    public ChatBigSmileAdapter(Context context, List<SmileItem> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflate(R.layout.p1_chat_big_smile_grid_item);

            vh.smile = (ImageView) convertView.findViewById(R.id.chat_smile);
            vh.name = (TextView) convertView.findViewById(R.id.chat_smile_name);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        SmileItem smileItem = getItem(position);
        //  ModuleMgr.getSmileMgr().reqImage(vh.smile, smileItem.getIcon());
        vh.name.setText(smileItem.getName());
        return convertView;
    }

    private class ViewHolder {
        public ImageView smile;
        public TextView name;
    }
}