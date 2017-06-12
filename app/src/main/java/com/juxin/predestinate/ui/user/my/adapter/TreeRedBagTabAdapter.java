package com.juxin.predestinate.ui.user.my.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.RedbagList;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.user.my.TreeRedBagRecordPanel;


/**
 * 红包列表
 * Created by zm on 2017/4/13.
 */
public class TreeRedBagTabAdapter extends ExBaseAdapter<RedbagList.RedbagInfo>{

    private String[] strPaths ;
    private int mPosition = -1;
    private Context mContext;
    private TreeRedBagRecordPanel mTreeRedBagRecordPanel;

    public TreeRedBagTabAdapter(Context context, TreeRedBagRecordPanel treeRedBagRecordPanel){
        super(context, null);
        this.mContext = context;
        this.mTreeRedBagRecordPanel = treeRedBagRecordPanel;
        strPaths = context.getResources().getStringArray(R.array.red_path);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.f1_wode_withdraw_item);
            vh = new MyViewHolder(convertView);

            convertView.setTag(vh);
        } else {
            vh = (MyViewHolder) convertView.getTag();
        }

        final RedbagList.RedbagInfo info = getItem(position);
        String time = info.getCreate_time();//获取获得红包的时间
        if (!TextUtils.isEmpty(time)) {
            String[] tempArr = time.split(" ");
            vh.tvData.setText(tempArr[0].replace("-", ".") + "\n" + tempArr[1].substring(0, 5));
        }
        int type = info.getType();
        //路径
        //红包类型 1 水果游戏红包(旧) 2 打擂奖励/排行奖励 3 聊天红包 4 聊天排名奖励(旧) 5 礼物折现 6. 摇钱树红包 7.8 好友邀请
        //红包版 摇钱树红包 打擂奖励 礼物折现 聊天红包 好友邀请
        if (type >=1 && type <= strPaths.length){
            vh.tvPath.setText(strPaths[type-1]);
        }
        vh.tvMoney.setText(info.getMoney()+"");
        //操作
        if (info.getFrozen() == 2) {
            vh.tvStatus.setText("已解冻");
            vh.tvStatus.setTextColor(Color.parseColor("#fd698c"));
        } else {
            vh.tvStatus.setText("冻结");
            vh.tvStatus.setTextColor(Color.parseColor("#9fa0a0"));
        }
        return convertView;
    }

    class MyViewHolder {
        TextView tvData,tvPath,tvMoney,tvStatus;

        public MyViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            tvData = (TextView) convertView.findViewById(R.id.withdraw_tv_date);
            tvPath = (TextView) convertView.findViewById(R.id.withdraw_tv_path);
            tvMoney = (TextView) convertView.findViewById(R.id.withdraw_tv_money);
            tvStatus = (TextView) convertView.findViewById(R.id.withdraw_tv_status);
        }
    }
}