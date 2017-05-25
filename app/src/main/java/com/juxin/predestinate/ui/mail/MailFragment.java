package com.juxin.predestinate.ui.mail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.swipemenu.SwipeListView;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenu;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenuCreator;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.item.MailMsgID;
import com.juxin.predestinate.ui.main.MainActivity;
import com.juxin.predestinate.ui.utils.CheckIntervalTimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 * Created by Kind on 2017/3/20.
 */

public class MailFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        SwipeListView.OnSwipeItemClickedListener, PObserver, View.OnClickListener, AbsListView.OnScrollListener {

    private CheckIntervalTimeUtil timeUtil;
    private MailFragmentAdapter mailFragmentAdapter;
    private SwipeListView listMail;
    private View mail_bottom;
    private Button mail_delete, mail_all_ignore;
    private TextView mail_title_right_text;

    private boolean isGone = false;//是否首面底部，默认是false
    private List<BaseMessage> mailDelInfoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.mail_fragment);
        setTitle(getResources().getString(R.string.main_btn_mail));
        onTitleRight();
        initView();

        MsgMgr.getInstance().attach(this);
        ModuleMgr.getCommonMgr().getFriendsSize();
        return getContentView();
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
                onTitleLeft();
                if (!isGone) {
                    editContent();
                } else {
                    cancleEdit();
                }
            }
        });
    }

    private void onTitleLeft() {
        if (!isGone) {
            View title_left = LayoutInflater.from(getActivity()).inflate(R.layout.f1_mail_title_left, null);
            title_left.findViewById(R.id.mail_title_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectAll();
                }
            });
            setTitleLeftContainer(title_left);
        } else {
            setTitleLeftContainerRemoveAll();
        }
    }

    private void initView() {
        timeUtil = new CheckIntervalTimeUtil();
        listMail = (SwipeListView) findViewById(R.id.mail_list);
        //   View mViewTop = LayoutInflater.from(getContext()).inflate(R.layout.layout_margintop, null);

        mail_bottom = findViewById(R.id.mail_bottom);
        mail_delete = (Button) findViewById(R.id.mail_delete);
        mail_all_ignore = (Button) findViewById(R.id.mail_all_ignore);
        mail_delete.setOnClickListener(this);
        mail_all_ignore.setOnClickListener(this);

        View listview_footer = LayoutInflater.from(getActivity()).inflate(R.layout.common_footer_distance, null);
        listMail.addFooterView(listview_footer);
        listview_footer.setOnClickListener(null);
        mailFragmentAdapter = new MailFragmentAdapter(getContext(), null);
        listMail.setAdapter(mailFragmentAdapter);
        mailFragmentAdapter.updateAllData();
        TimerUtil.beginTime(new TimerUtil.CallBack() {
            @Override
            public void call() {
                detectInfo(listMail);
            }
        }, 800);

        listMail.setPullLoadEnable(false);
        listMail.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                if (menu.getViewType() == 1) {
                    menu.setTitle("忽略");
                } else {
                    menu.setTitle("删除");
                }
                menu.setTitleSize(18);
                menu.setTitleColor(Color.WHITE);
                menu.setViewHeight(mailFragmentAdapter.getItemHeight());
            }
        });

        listMail.setPullLoadEnable(false);
        listMail.setPullRefreshEnable(false);
        listMail.setOnItemClickListener(this);
        listMail.setSwipeItemClickedListener(this);

        listMail.setOnScrollListener(this);
    }

    @Override
    public void onSwipeChooseOpened() {
        ((MainActivity) getActivity()).onGoneBottom(false);
        mail_bottom.setVisibility(View.VISIBLE);
        mail_title_right_text.setText("取消");
        isGone = true;
    }

    @Override
    public void onSwipeChooseClosed() {
        ((MainActivity) getActivity()).onGoneBottom(true);
        mail_bottom.setVisibility(View.GONE);
        mail_title_right_text.setText("编辑");
        isGone = false;
    }

    @Override
    public void onSwipeChooseChecked(int position, boolean isChecked) {
        int index = position - mailFragmentAdapter.mailItemOtherSize();
        if (index < 0 || index >= mailFragmentAdapter.getList().size())
            return;
        BaseMessage message = mailFragmentAdapter.getItem(index);
        if (isChecked) {
            mailDelInfoList.add(message);
        } else {
            mailDelInfoList.remove(message);
        }
        mail_delete.setEnabled(mailDelInfoList.size() > 0);
    }

    @Override
    public void onSwipeMenuClick(int position, SwipeMenu swipeMenu, View contentView) {
        List<BaseMessage> baseMessageList = mailFragmentAdapter.getList();
        final BaseMessage item = baseMessageList.get(position);
        if (item != null) {
            MailMsgID mailMsgID = MailMsgID.getMailMsgID(item.getLWhisperID());
            if (mailMsgID != null) {
                switch (mailMsgID) {
                    case Follow_Msg:
                        ModuleMgr.getChatListMgr().updateFollow();
                        break;
                    case MyFriend_Msg:

                        break;
                    case Greet_Msg:

                        break;
                }
            } else {
                ModuleMgr.getChatListMgr().deleteMessage(item.getLWhisperID());

//                ModuleMgr.getChatListMgr().deleteWhisperChatUser(strings, new ChatMsgInterface.DelChatUserComplete() {
//                    @Override
//                    public void onReqChatUser(boolean ret) {
//                    }
//                });
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseMessage message = (BaseMessage) parent.getAdapter().getItem(position);
        if (message != null) {
            MailMsgID mailMsgID = MailMsgID.getMailMsgID(message.getLWhisperID());
            if (mailMsgID != null) {
                switch (mailMsgID) {
                    case Follow_Msg://谁关注我
                        UIShow.showMyAttentionAct(getContext());
                        break;
                    case MyFriend_Msg://我的好友
                        UIShow.showMyFriendsAct(getActivity());
                        break;
                    case Greet_Msg://打招呼的人
                        UIShow.showSayHelloUserAct(getActivity());
                        break;
                }
            } else {
                UIShow.showPrivateChatAct(getActivity(), message.getLWhisperID(), message.getName(), message.getKfID());
            }
        }
    }

    public void selectAll() {
        // 全选
        mail_delete.setEnabled(true);
        mailDelInfoList.addAll(mailFragmentAdapter.mailItemOrdinary());
        listMail.selectAllChooseView();
    }

    public void cancleEdit() {
        mailDelInfoList.clear();
        mail_delete.setEnabled(false);
        listMail.smoothCloseChooseView();
    }

    public void editContent() {
        mailDelInfoList.clear();
        if (mailFragmentAdapter.mailItemOrdinarySize() > 0) {
            listMail.smoothOpenChooseView();
        } else {
            setTitleLeftContainerRemoveAll();
            PToast.showCenterShort("没有可编辑选项");
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        if (mailFragmentAdapter == null) return;
        switch (key) {
            case MsgType.MT_User_List_Msg_Change:
            case MsgType.MT_Friend_Num_Notice:
                PLogger.printObject("xxxxxxxxxxxxxxx");
                mailFragmentAdapter.updateAllData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mail_delete:
                mail_delete.setEnabled(false);
                ModuleMgr.getChatListMgr().deleteBatchMessage(mailDelInfoList);
                mailDelInfoList.clear();
                listMail.smoothCloseChooseView();
                break;
            case R.id.mail_all_ignore:
                //忽略所有未读消息
                PickerDialogUtil.showSimpleAlertDialog(getActivity(), new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        ModuleMgr.getChatListMgr().updateToReadAll();
                        listMail.smoothCloseChooseView();
                        PToast.showShort("忽略成功!");
                    }
                }, "忽略未读消息,但消息不会删除.", "忽略消息");
                break;
        }
    }

    /**
     * 检测个人资料
     *
     * @param view
     */
    private void detectInfo(AbsListView view) {
        if (!timeUtil.check(10 * 1000)) {
            return;
        }
        List<Long> stringList = new ArrayList<>();
        stringList.clear();
        int firs = view.getFirstVisiblePosition();
        int last = view.getLastVisiblePosition();

        PLogger.printObject("detectInfo-----" + "firs=" + firs + "--last=" + last);
        for (int i = firs; i < last; i++) {
            BaseMessage message = mailFragmentAdapter.getItem(i);
            if (message == null || MailMsgID.getMailMsgID(message.getLWhisperID()) != null) {
                continue;
            }

            if (TextUtils.isEmpty(message.getName()) && TextUtils.isEmpty(message.getAvatar())) {
                stringList.add(message.getLWhisperID());
            }
        }

        if (stringList.size() > 0) {
            ModuleMgr.getChatMgr().getProFile(stringList);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: {//停止滚动
                //设置为停止滚动
                mailFragmentAdapter.setScrollState(false);
                detectInfo(view);
                break;
            }
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING: {//滚动做出了抛的动作
                //设置为正在滚动
                mailFragmentAdapter.setScrollState(true);
                break;
            }
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: {//正在滚动
                //设置为正在滚动
                mailFragmentAdapter.setScrollState(true);
                break;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }
}