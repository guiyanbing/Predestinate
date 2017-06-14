package com.juxin.predestinate.ui.mail;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.SysNoticeMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.module.logic.invoke.Invoker;
import com.juxin.predestinate.module.util.TimeUtil;

import java.util.List;


/**
 * 系统消息
 * Created by zm on 2017/6/13.
 */
public class SysMessAdapter extends ExBaseAdapter<SysNoticeMessage> {

    private Context mContext;

    public SysMessAdapter(Context mContext, List<SysNoticeMessage> datas) {
        super(mContext, datas);
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mHolder;
        if (convertView == null) {
            convertView = inflate(R.layout.p1_sys_mess_item);
            mHolder = new MyViewHolder(convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }
        final SysNoticeMessage info = getItem(position);
        if (info == null)
            return convertView;

        mHolder.tvTitle.setText(info.getMsgDesc() + "");
        mHolder.tvContent.setText(info.getInfo());
        if (!TextUtils.isEmpty(info.getPic())) {
            mHolder.imgPic.setVisibility(View.VISIBLE);
            ImageLoader.loadCenterCrop(mContext, info.getPic(), mHolder.imgPic, 0, 0);
            mHolder.tvJump.setTextColor(ContextCompat.getColor(mContext, R.color.color_zhuyao));
        } else {
            mHolder.imgPic.setVisibility(View.GONE);
            mHolder.tvJump.setTextColor(ContextCompat.getColor(mContext, R.color.color_45A3EC));
        }
        mHolder.tvTips.setText(TimeUtil.millisecondToFormatString(info.getTime()));
        mHolder.tvJump.setText(info.getBtn_text());
        mHolder.tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(info.getPic())) {
                    Invoker.getInstance().doInJS(info.getBtn_action(), null);
                    return;
                }
                if (mContext instanceof FragmentActivity)
                    ModuleMgr.getCommonMgr().checkUpdate((FragmentActivity) mContext, true);
            }
        });

        return convertView;
    }

    class MyViewHolder {

        TextView tvTips, tvTitle, tvContent, tvJump;
        ImageView imgPic;

        public MyViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            tvTips = (TextView) convertView.findViewById(R.id.sys_mess_item_tv_tips);
            tvTitle = (TextView) convertView.findViewById(R.id.sys_mess_item_tv_title);
            tvContent = (TextView) convertView.findViewById(R.id.sys_mess_item_tv_content);
            tvJump = (TextView) convertView.findViewById(R.id.sys_mess_item_tv_jump);
            imgPic = (ImageView) convertView.findViewById(R.id.sys_mess_item_img_pic);
        }
    }
}