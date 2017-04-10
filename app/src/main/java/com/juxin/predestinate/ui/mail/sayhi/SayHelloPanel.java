package com.juxin.predestinate.ui.mail.sayhi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.view.CircleImageView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.pagerecyeler.PageIndicatorView;
import com.juxin.predestinate.third.pagerecyeler.PageRecyclerView;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.juxin.library.observe.MsgType.MT_Say_Hello_Notice;

/**
 * Created by zhang on 2017/3/30.
 */

public class SayHelloPanel extends BaseViewPanel implements RequestComplete {

    /**
     * 封装的RecyclerView
     */
    private CustomRecyclerView customRecyclerView;

    /**
     * 横向滑动的 分页RecyclerView
     */
    private PageRecyclerView pageRecyclerView;

    /**
     * 滑动的页码指示器
     */
    private PageIndicatorView pageIndicatorView;

    private PageRecyclerView.PageAdapter adapter;


    /**
     * 一键打招呼的按钮
     */
    private Button sure_button;

    private List<UserInfoLightweight> listData = new ArrayList<>();

    public SayHelloPanel(Context context) {
        super(context);
        setContentView(com.juxin.predestinate.R.layout.p1_say_hello);
        initView();
        getContentData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        customRecyclerView = (CustomRecyclerView) findViewById(com.juxin.predestinate.R.id.say_hello_content);
        pageRecyclerView = customRecyclerView.getPageRecyclerView();
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.say_hello_content_indicator);
        sure_button = (Button) findViewById(R.id.say_hello_btn);
        // 设置指示器
        pageRecyclerView.setIndicator(pageIndicatorView);
        customRecyclerView.showPageRecyclerView();
    }


    private void getContentData() {
        ModuleMgr.getCommonMgr().getSayHiList(this);
    }

    private void setViewData(MyHolder holder, int position) {
        holder.tv_nickName.setText(listData.get(position).getNickname());
        holder.iv_vipState.setVisibility(listData.get(position).isVip() ? View.VISIBLE : View.GONE);
        holder.iv_authState.setVisibility(listData.get(position).isAuth() ? View.VISIBLE : View.GONE);
        ImageLoader.loadAvatar(getContext(), listData.get(position).getAvatar(), holder.iv_avatar);
    }

    private void setContentData() {
        if (listData.size() != 0) {

            adapter = pageRecyclerView.new PageAdapter(listData, new PageRecyclerView.CallBack() {

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.p1_say_hello_item, parent, false);
                    return new MyHolder(view);
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                    MyHolder myHolder = (MyHolder) holder;
                    setViewData(myHolder, position);
                }

                @Override
                public void onItemClickListener(View view, int position) {

                }

                @Override
                public void onItemLongClickListener(View view, int position) {

                }
            });
            pageRecyclerView.setAdapter(adapter);
            if (listData.size() > 6) {
                pageRecyclerView.setPageSize(3, 3);
            } else if (listData.size() <= 6) {
                pageRecyclerView.setPageSize(2, 3);
            }
            pageRecyclerView.setPageMargin(30);
        }
        setToSayHello();
    }


    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            UserInfoLightweightList infoLightweightList = new UserInfoLightweightList();
            infoLightweightList.parseJson(response.getResponseString());
            listData = infoLightweightList.getUserInfos();
            setContentData();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_nickName;

        public CircleImageView iv_avatar;
        public ImageView iv_vipState, iv_authState;

        public MyHolder(View itemView) {
            super(itemView);
            tv_nickName = (TextView) itemView.findViewById(R.id.say_hello_item_name);
            iv_avatar = (CircleImageView) itemView.findViewById(R.id.say_hello_item_avatar);
            iv_vipState = (ImageView) itemView.findViewById(R.id.say_hello_item_vip_state);
            iv_authState = (ImageView) itemView.findViewById(R.id.say_hello_item_auth_state);
        }
    }


    public void setToSayHello() {
        sure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 一键打招呼
                MsgMgr.getInstance().sendMsg(MT_Say_Hello_Notice, "");
            }
        });
    }
}
