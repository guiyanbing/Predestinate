package com.juxin.predestinate.ui.user.my.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.my.RedBagRecordPanel;
import com.juxin.predestinate.ui.user.my.RedBoxRecordAct;
import com.juxin.predestinate.bean.my.AddredTotalInfo;
import com.juxin.predestinate.bean.my.RedbagList;


/**
 * 红包列表
 * Created by zm on 2017/4/13.
 */
public class RedBagTabAdapter extends BaseRecyclerViewAdapter<RedbagList.RedbagInfo> implements RequestComplete{

    private String[] strPaths ;
    private int mPosition = -1;
    private Context mContext;
    private RedBagRecordPanel mRedBagRecordPanel;

    public RedBagTabAdapter(Context context,RedBagRecordPanel mRedBagRecordPanel){
        this.mContext = context;
        this.mRedBagRecordPanel = mRedBagRecordPanel;
        strPaths = context.getResources().getStringArray(R.array.red_path);
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.f1_wode_withdraw_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {

        MyViewHolder vh = new MyViewHolder(viewHolder);
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
        vh.tvStatus.setText(R.string.imburse);
        vh.tvStatus.setTextColor(Color.parseColor("#ea5413"));
        vh.tvStatus.setBackgroundResource(R.drawable.f1_shape_red_bag_border);
        vh.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.show((FragmentActivity)mContext);
                mPosition = position;
                ModuleMgr.getCommonMgr().reqAddredTotal(ModuleMgr.getCenterMgr().getMyInfo().getUid(), info.getMoney() * 100f, info.getId(), info.getType(), RedBagTabAdapter.this);
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        AddredTotalInfo info = new AddredTotalInfo();
        info.parseJson(response.getResponseString());
        if (response.isOk()) {
            //更新可提现金额
            getList().remove(mPosition);
            notifyDataSetChanged();
            mRedBagRecordPanel.showCollect();
            ((RedBoxRecordAct)mContext).refreshView(info.getSum());
        }
        PToast.showShort(info.getMsg()+"");//展示提示
    }

    class MyViewHolder {
        TextView tvData,tvPath,tvMoney,tvStatus;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            tvData = convertView.findViewById(R.id.withdraw_tv_date);
            tvPath = convertView.findViewById(R.id.withdraw_tv_path);
            tvMoney = convertView.findViewById(R.id.withdraw_tv_money);
            tvStatus = convertView.findViewById(R.id.withdraw_tv_status);
        }
    }
}