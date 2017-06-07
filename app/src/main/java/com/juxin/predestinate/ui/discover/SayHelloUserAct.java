package com.juxin.predestinate.ui.discover;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.db.utils.RxUtil;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.baseui.xlistview.XListView;
import com.juxin.predestinate.module.logic.swipemenu.SwipeListView;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenu;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenuCreator;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.item.MailMsgID;
import com.juxin.predestinate.ui.utils.CheckIntervalTimeUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static com.juxin.predestinate.R.id.say_hello_users_all_ignore;
import static com.juxin.predestinate.module.logic.application.App.getActivity;

/**
 * 打招呼的人
 * Created by zhang on 2017/5/22.
 */

public class SayHelloUserAct extends BaseActivity implements AdapterView.OnItemClickListener,
        PObserver, XListView.IXListViewListener, View.OnClickListener,
        SwipeListView.OnSwipeItemClickedListener, AbsListView.OnScrollListener {

    private CustomFrameLayout customFrameLayout;
    private SwipeListView exListView;

    private List<BaseMessage> data = new ArrayList<>();

    private SayHelloUserAdapter adapter;

    private TextView mail_title_right_text;
    private View bottom_view;
    private Button del_btn, ignore_btn;
    private boolean isGone = false;//是否首面底部，默认是false
    private List<BaseMessage> delList = new ArrayList<>();
    private CheckIntervalTimeUtil timeUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_say_hello_user_act);
        initView();
        initData();
        MsgMgr.getInstance().attach(this);
    }

    private void initView() {
        setTitle(getString(R.string.say_hello_user_act_title));
        setBackView();
        onTitleRight();

        timeUtil = new CheckIntervalTimeUtil();
        customFrameLayout = (CustomFrameLayout) findViewById(R.id.say_hello_users_frame_layput);
        customFrameLayout.setList(new int[]{R.id.say_hello_users_data, R.id.common_nodata});
        exListView = (SwipeListView) findViewById(R.id.say_hello_users_list);
        View mViewTop = LayoutInflater.from(this).inflate(R.layout.layout_margintop, null);
        View listview_footer = LayoutInflater.from(getActivity()).inflate(R.layout.common_footer_distance, null);
        exListView.setPullLoadEnable(true);
        exListView.setPullLoadEnable(false);
        exListView.setXListViewListener(this);
        exListView.addHeaderView(mViewTop);
        exListView.addFooterView(listview_footer);
        adapter = new SayHelloUserAdapter(this, data);
        exListView.setAdapter(adapter);
        exListView.setOnItemClickListener(this);

        exListView.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                if (menu.getViewType() == 1) {
                    menu.setTitle("忽略");
                } else {
                    menu.setTitle("删除");
                }
                menu.setTitleSize(18);
                menu.setTitleColor(Color.WHITE);
                menu.setViewHeight(adapter.getItemHeight());
            }
        });

        exListView.setSwipeItemClickedListener(this);
        exListView.setOnScrollListener(this);


        bottom_view = findViewById(R.id.say_hello_users_bottom);
        del_btn = (Button) findViewById(R.id.say_hello_users_delete);
        ignore_btn = (Button) findViewById(say_hello_users_all_ignore);
        del_btn.setOnClickListener(this);
        ignore_btn.setOnClickListener(this);
    }

    private void initData() {
        if (data.size() != 0) {
            data.clear();
        }
        data.addAll(ModuleMgr.getChatListMgr().getGeetList());
        if (data.size() > 0) {
            showHasData();
        } else {
            showNoData();
        }

        //发送清消息列表打招呼的人角标
        if (ModuleMgr.getChatListMgr().getStrangerNew()) {
            PLogger.printObject("1111111111111111 SayHelloUserAct = " + ModuleMgr.getChatListMgr().getStrangerNew());
            ModuleMgr.getChatListMgr().setStrangerNew(false);
        }
    }

    private void onTitleRight() {
        setTitleRightImgGone();
        View title_right = LayoutInflater.from(getActivity()).inflate(R.layout.f1_mail_title_right, null);
        mail_title_right_text = (TextView) title_right.findViewById(R.id.mail_title_right_text);
        mail_title_right_text.setText("编辑");
        setTitleRightContainer(title_right);
        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGone) {
                    editContent();
                } else {
                    cancleEdit();
                }
            }
        });
    }


    private void onShowTitleLeft() {
        View title_left = LayoutInflater.from(getActivity()).inflate(R.layout.f1_mail_title_left, null);
        title_left.findViewById(R.id.mail_title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAll();
            }
        });
        setBackViewGone();
        setTitleLeftContainer(title_left);
    }

    private void onHidTitleLeft() {
        setTitleLeftContainerRemoveAll();
        setBackView();
    }


    // 全选
    public void selectAll() {
        delList.clear();
        if (adapter == null) return;
        if (del_btn != null) {
            del_btn.setEnabled(true);
        }
        delList.addAll(adapter.getList());
        exListView.selectAllChooseView();
    }

    public void cancleEdit() {
        delList.clear();
        del_btn.setEnabled(false);
        exListView.smoothCloseChooseView();
    }

    public void editContent() {
        delList.clear();
        del_btn.setEnabled(false);
        if (adapter.getList().size() > 0) {
            exListView.smoothOpenChooseView();
        } else {
            onHidTitleLeft();
            PToast.showCenterShort("没有可编辑选项");
        }
    }


    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BaseMessage message = (BaseMessage) adapterView.getAdapter().getItem(i);
        if (message != null) {
            MailMsgID mailMsgID = MailMsgID.getMailMsgID(message.getLWhisperID());
            if (mailMsgID == null) {
                UIShow.showPrivateChatAct(this, message.getLWhisperID(), message.getName(), message.getKfID());
            }
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_User_List_Msg_Change:
                initData();
                break;
            default:
                break;
        }
    }

    /**
     * 显示有数据状态
     */
    public void showHasData() {
        adapter.notifyDataSetChanged();
        detectInfo(exListView);
        exListView.stopRefresh();
        customFrameLayout.show(R.id.say_hello_users_data);
    }

    /**
     * 显示无数据状态
     */
    public void showNoData() {
        customFrameLayout.show(R.id.common_nodata);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.say_hello_users_delete:
                del_btn.setEnabled(false);
                ModuleMgr.getChatListMgr().deleteBatchMessage(delList);
                delList.clear();
                onHidTitleLeft();
                exListView.smoothCloseChooseView();
                break;
            case R.id.say_hello_users_all_ignore:
                //忽略所有未读消息
                PickerDialogUtil.showSimpleAlertDialog(this, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        onHidTitleLeft();
                        ModuleMgr.getChatListMgr().updateToBatchRead(data);
                        exListView.smoothCloseChooseView();
                        PToast.showShort("忽略成功!");
                    }
                }, "忽略未读消息,但消息不会删除.", "忽略消息");
                break;
        }
    }

    @Override
    public void onSwipeChooseOpened() {
        bottom_view.setVisibility(View.VISIBLE);
        onShowTitleLeft();
        mail_title_right_text.setText("取消");
        isGone = true;
    }

    @Override
    public void onSwipeChooseClosed() {
        bottom_view.setVisibility(View.GONE);
        mail_title_right_text.setText("编辑");
        onHidTitleLeft();
        isGone = false;
    }

    @Override
    public void onSwipeChooseChecked(int position, boolean isChecked) {
        int size = adapter.getList().size();
        if (size <= 0 || position >= size) {
            return;
        }
        BaseMessage message = adapter.getItem(position);
        if (isChecked) {
            delList.add(message);
        } else {
            delList.remove(message);
        }
        del_btn.setEnabled(delList.size() > 0);
    }

    @Override
    public void onSwipeMenuClick(int position, SwipeMenu swipeMenu, View contentView) {
        List<BaseMessage> baseMessageList = adapter.getList();
        final BaseMessage item = baseMessageList.get(position);
        if (item != null) {
            MailMsgID mailMsgID = MailMsgID.getMailMsgID(item.getLWhisperID());
            if (mailMsgID == null) {
                ModuleMgr.getChatListMgr().deleteMessage(item.getLWhisperID());
            }
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: {//停止滚动
                //设置为停止滚动
                TimerUtil.beginTime(new TimerUtil.CallBack() {
                    @Override
                    public void call() {
                        detectInfo(view);
                    }
                }, 200);
                break;
            }
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING: {//滚动做出了抛的动作
                break;
            }
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: {//正在滚动
                break;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}


    /**
     * 检测个人资料
     *
     * @param view
     */
    private void detectInfo(AbsListView view) {
        if (adapter == null) return;
        if (!timeUtil.check(10 * 1000)) {
            return;
        }
        final List<Long> stringList = new ArrayList<>();

        int firs = view.getFirstVisiblePosition();
        int last = view.getLastVisiblePosition();

        for (int i = firs; i < last; i++) {
            BaseMessage message = adapter.getItem(i);
            if (message == null || MailMsgID.getMailMsgID(message.getLWhisperID()) != null) {
                continue;
            }

            if (TextUtils.isEmpty(message.getName()) && TextUtils.isEmpty(message.getAvatar())) {
                stringList.add(message.getLWhisperID());
            }
        }

        if (stringList.size() > 0) {
            Observable<List<UserInfoLightweight>> observable = ModuleMgr.getChatMgr().getUserInfoList(stringList);
            observable.compose(RxUtil.<List<UserInfoLightweight>>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER))
            .subscribe(new Action1<List<UserInfoLightweight>>() {
                @Override
                public void call(List<UserInfoLightweight> lightweights) {
                    if(lightweights != null && lightweights.size() > 0){
                        ModuleMgr.getChatMgr().updateUserInfoList(lightweights);
                    }
                }
            });

            TimerUtil.beginTime(new TimerUtil.CallBack() {
                @Override
                public void call() {
                    ModuleMgr.getChatMgr().getProFile(stringList);
                }
            }, 800);
        }
    }
}
