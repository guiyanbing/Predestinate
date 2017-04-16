package com.juxin.predestinate.ui.user.complete;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;

import java.util.List;

/**
 * 聊天面板多媒体选择扩展panel
 * Created by Kind on 2017/3/31.
 */

public class CommonGridBtnPanel extends BaseViewPanel{

    public CommonGridBtnPanel(Context context, List<BTN_KEY> list) {
        super(context);
        setContentView(R.layout.p1_grid_btn_panel);
        initView(list);
    }

    private void initView(List<BTN_KEY> list) {
        GridView gridView = (GridView) findViewById(R.id.gridview);
        BtnAdapter adapter = new BtnAdapter(getContext(), list);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static class BtnAdapter extends ExBaseAdapter<BTN_KEY> {

        private BtnClickListener btnClickListener = null;

        public BtnAdapter(Context context, List<BTN_KEY> datas) {
            super(context, datas);
        }

        public void setBtnClickListener(BtnClickListener btnClickListener) {
            this.btnClickListener = btnClickListener;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflate(R.layout.p1_grid_btn_item);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.txt = (TextView) convertView.findViewById(R.id.txt);
                holder.btn = (LinearLayout) convertView.findViewById(R.id.btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final BTN_KEY item = getItem(position);
            holder.icon.setImageResource(item.getIcon());
            holder.txt.setText(item.getTitle());
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnClickListener != null) {
                        btnClickListener.onClick(item);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView txt;
            LinearLayout btn;
        }
    }

    public enum BTN_KEY {
        IMG("图片", R.drawable.chat_input_grid_img_selector),           //发送图片按钮
        VIDEO("视频聊天", R.drawable.chat_input_grid_video_selector),       //视频聊天
        VOICE("语音聊天", R.drawable.chat_input_grid_voice_selector),    //语音聊天
        GAME_WITH_FISH("配鱼", R.drawable.chat_input_grid_voice_selector),    //配鱼
        ;

        String title;
        int icon;

        BTN_KEY(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

    public interface BtnClickListener {
        void onClick(BTN_KEY key);
    }

}
