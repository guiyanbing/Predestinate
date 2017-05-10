package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.smile.SmilePackage;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import java.util.List;

/**
 * 根据表情的文件名，将表情图片显示到表情面板。
 */
public class ChatSmileAdapter extends ExBaseAdapter<SmilePackage> {
    private int selectPos = -1;

    public ChatSmileAdapter(Context context, List<SmilePackage> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;

        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflate(R.layout.p1_chat_smile_item);

            vh.smile_item = convertView.findViewById(R.id.smile_item);
            vh.smile_package_ico = (ImageView) convertView.findViewById(R.id.smile_package_ico);
            vh.smile_package_name = (TextView) convertView.findViewById(R.id.smile_package_name);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        SmilePackage smileItem = getItem(position);

        ImageLoader.loadAvatar(getContext(), smileItem.getIcon(), vh.smile_package_ico);

        vh.smile_package_name.setText(smileItem.getName());

        vh.smile_item.setSelected(position == selectPos ? true : false);
        return convertView;
    }

    /**
     * 设置当前选中的位置
     */
    public void setCheckPosition(int position) {
        selectPos = position;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private View smile_item;
        public ImageView smile_package_ico;
        public TextView smile_package_name;
    }
}
