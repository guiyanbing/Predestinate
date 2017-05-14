package com.juxin.predestinate.ui.user.my.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.bean.my.WithdrawList;


/**
 * 提现记录
 * Created by zm on 2017/4/13.
 */
public class WithDrawTabAdapter extends BaseRecyclerViewAdapter<WithdrawList.WithdrawInfo>{

    private Context mContext;

    public WithDrawTabAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.f1_withdraw_panel_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {

        MyViewHolder vh = new MyViewHolder(viewHolder);
        final WithdrawList.WithdrawInfo info = getList().get(position);
        vh.llSuccess.setVisibility(View.GONE);
        vh.llError.setVisibility(View.GONE);
        if (info.getStatus() == 1 || info.getStatus() == 2){//根据提现状态设置展示信息
            vh.llSuccess.setVisibility(View.VISIBLE);
        }else {
            vh.llError.setVisibility(View.VISIBLE);
        }
        String time = info.getCreate_time();
        if (!TextUtils.isEmpty(time)) {//时间处理
            String[] tempArr = time.split(" ");
            vh.tvData.setText(tempArr[0].replace("-", ".") + "\n" + tempArr[1].substring(0, 5));
        }
        vh.tvMoney.setText(info.getMoney()+"");
        switch (info.getStatus()){
            case 1:
                vh.tvStatus.setText(R.string.untreated);
                vh.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_FFAE35));
                vh.tvStatus.setBackgroundResource(0);
                break;
            case 2:
                vh.tvStatus.setText(R.string.processed);
                vh.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_D6D6D6));
                vh.tvStatus.setBackgroundResource(0);
                break;
            case 3:
                vh.tvStatus.setText(R.string.click_on_the_modify);
                vh.tvStatus.setTextColor(mContext.getResources().getColor(R.color.blue));
                vh.tvStatus.setBackgroundResource(0);
                vh.tvStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //红包提现--银行卡界面
                        if (mContext != null) {
                            UIShow.showWithDrawApplyAct((int) info.getId(), info.getMoney(), true, mContext);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {

        TextView tvData,tvMoney,tvStatus;
        LinearLayout llSuccess,llError;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            tvData = convertView.findViewById(R.id.withdraw_item_tv_date);
            tvMoney = convertView.findViewById(R.id.withdraw_item_tv_money);
            tvStatus = convertView.findViewById(R.id.withdraw_item_tv_status);
            llSuccess = convertView.findViewById(R.id.withdraw_item_ll_success);
            llError = convertView.findViewById(R.id.withdraw_item_ll_error);
        }
    }
}