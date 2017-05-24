package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatViewPanel;
import com.juxin.predestinate.module.local.msgview.smile.GiftItem;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.local.msgview.smile.SmilePackage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.HorizontalListView;

import java.util.List;

/**
 * 表情
 * Created by Kind on 2017/3/31.
 */

public class ChatSmilePanel extends ChatViewPanel implements AdapterView.OnItemClickListener, View.OnClickListener {

    private TextView tv_custom_face_del;
    private HorizontalListView chat_smile_tab;
    private FrameLayout smilePackageLayouts = null;
    private ChatSmileAdapter chatSmileAdapter;
    private ChatCustomSmilePanel chatSmilePanel;

    public ChatSmilePanel(Context context, ChatAdapter.ChatInstance chatInstance) {
        super(context, chatInstance);

        chatInstance.chatSmilePanel = this;

        setContentView(R.layout.p1_chat_smile);
        initView();
    }

    public void initView() {
        chat_smile_tab = (HorizontalListView) findViewById(R.id.chat_smile_tab);
        smilePackageLayouts = (FrameLayout) findViewById(R.id.chat_smile_package_layouts);
        tv_custom_face_del = (TextView) findViewById(R.id.tv_custom_face_del);

        chatSmileAdapter = new ChatSmileAdapter(getContext(), ModuleMgr.getPhizMgr().getSmilePacks().getPackages());
        chat_smile_tab.setAdapter(chatSmileAdapter);
        chat_smile_tab.setOnItemClickListener(this);
        chatSmileAdapter.setCheckPosition(0);
        tv_custom_face_del.setOnClickListener(this);
        addView_Package_Default();

        show(false);
    }

    public void resetSmilePackages() {
        if (chatSmileAdapter == null) {
            return;
        }

        // chatSmileAdapter.setList(ModuleMgr.getPhizMgr().getSmilePacks().getPackages());

//        if (ChatListMgr.Folder.sys_notice == getChatInstance().chatAdapter.getFolder()) {
//             chatSmileAdapter.setList(ModuleMgr.getSmileMgr().getSmilePacks().getPackages(new String[]{"gift"}));
//        } else {
//              chatSmileAdapter.setList(ModuleMgr.getSmileMgr().getSmilePacks().getPackages());
//        }
    }

    private void addView_Package_Default() {
        smilePackageLayouts.removeAllViews();

        ChatDefaultSmilePanel chatSmilePanel = new ChatDefaultSmilePanel(getContext(), getChatInstance());

        smilePackageLayouts.addView(chatSmilePanel.getContentView());
    }

    private void addView_Package_Custom(List<SmileItem> items) {
        smilePackageLayouts.removeAllViews();

        chatSmilePanel = new ChatCustomSmilePanel(getContext(), items, getChatInstance());

        smilePackageLayouts.addView(chatSmilePanel.getContentView());
    }

    private void addView_Package_Gift(List<GiftItem> items) {
        smilePackageLayouts.removeAllViews();

        ChatGiftSmilePanel chatSmilePanel = new ChatGiftSmilePanel(getContext(), items, getChatInstance());

        smilePackageLayouts.addView(chatSmilePanel.getContentView());
    }

    private void addView(SmilePackage smilePackage) {
        if (smilePackage == null) {
            return;
        }

        if ("smallface".equals(smilePackage.getType())) {
            tv_custom_face_del.setVisibility(View.GONE);
            addView_Package_Default();
        } else if ("customface".equals(smilePackage.getType())) {
            tv_custom_face_del.setVisibility(View.VISIBLE);
            addView_Package_Custom(smilePackage.getItems());
        }


//
//        if ("smallface".equals(smilePackage.getType())) {
//            addView_Package_Default();
//        } else if ("bigface".equals(smilePackage.getType())) {
//            addView_Package_Big(smilePackage.getItems(), 0);
//        } else if ("minigame".equals(smilePackage.getType())) {
//            addView_Package_Big(smilePackage.getItems(), 1);
//        } else if ("gift".equals(smilePackage.getType())) {
        //      ModuleMgr.getSmileMgr().reqGiftLevel(getChatInstance().chatAdapter.getLWhisperId(), null);
        //        addView_Package_Gift(ModuleMgr.getSmileMgr().getGiftShop().getItems());
        //  }
    }

    /**
     * 选中指定表情显示。
     *
     * @param name
     */
    public void selectSmile(String name) {
        try {
            int position = 0;
            List<SmilePackage> smilePackages = chatSmileAdapter.getList();

            for (SmilePackage smilePackage : smilePackages) {
                if (name.equals(smilePackage.getName())) {
                    chatSmileAdapter.setCheckPosition(position);
                    addView(smilePackage);
                }

                ++position;
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SmilePackage smilePackage = null;

        try {
            smilePackage = (SmilePackage) parent.getAdapter().getItem(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (smilePackage == null) {
            return;
        }

        chatSmileAdapter.setCheckPosition(position);//设置选中效果
        addView(smilePackage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_custom_face_del:

                String del = tv_custom_face_del.getText().toString();
                if (v.getTag() == null || (int) v.getTag() == 0 || del.equals("删除")) {
                    v.setTag(1);
                    chatSmilePanel.setDeleteClick(true);
                    tv_custom_face_del.setText("取消");
                } else {
                    v.setTag(0);
                    chatSmilePanel.setDeleteClick(false);
                    tv_custom_face_del.setText("删除");
                }
                break;

            default:
                break;
        }
    }
}
