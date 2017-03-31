package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/3/30.
 */

public class ChatBigSmileMatchingPanel extends ChatBaseSmilePanel implements AdapterView.OnItemClickListener {

    private ViewPager vp = null;
    private List<SmileItem> items = null;

    public ChatBigSmileMatchingPanel(Context context, ChatAdapter.ChatInstance chatInstance) {
        super(context, chatInstance);

        setContentView(R.layout.p1_chat_matching_smile);
        initView();
    }

    public void initView() {
        vp = (ViewPager) findViewById(R.id.chat_panel_viewpager);
    }

    /**
     * 调用前，key必须用TextUtils.isEmpty(key)进行判断。
     *
     * @param key
     */
    public void reset(CharSequence key) {
        //reset(ModuleMgr.getSmileMgr().matchingSmileItems(key));
    }

    private void reset(List<SmileItem> items) {
        this.items = items;

        if (items == null) {
            show(false);
        } else {
            show(true);
        }

        //       ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getAllViews());
//        vp.setAdapter(viewPagerAdapter);
//
//        initPointsView(vp, viewPagerAdapter.getCount());
    }

    private List<View> getAllViews() {
        List<View> views = new ArrayList<View>();
        View view = null;
        int index = 0;

        while ((view = getChildView(index++)) != null) {
            views.add(view);
        }

        return views;
    }

    private View getChildView(int index) {
        List<SmileItem> listTemp = getPageRes(index);

        if (listTemp == null) {
            return null;
        }

        View view = View.inflate(getContext(), R.layout.p1_chat_smile_grid, null);
        GridView gv = (GridView) view.findViewById(R.id.chat_panel_gridview);
        gv.setNumColumns(pageResNum);

        final ChatBigSmileAdapter chatSmileAdapter = new ChatBigSmileAdapter(getContext(), listTemp);
        gv.setAdapter(chatSmileAdapter);
        gv.setOnItemClickListener(this);

        return view;
    }

    /**
     * 保存表情资源的列表。
     */
    private static int pageResNum = 5;

    /**
     * 根据每页需要显示的表情数，截取对应的文件名List。
     *
     * @param index 对应页。
     * @return 指定页的资源信息。
     */
    public List<SmileItem> getPageRes(int index) {
        if (items == null) {
            return null;
        }

        List<SmileItem> listTemp = items;
        int start = index * pageResNum;
        int offset = listTemp.size() - start;

        if (offset < 0) {
            return null;
        }

        if (offset > pageResNum) {
            offset = pageResNum;
        }

        return listTemp.subList(start, start + offset);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id == -1) {
            return;
        }

//        if (!ModuleMgr.getCommonMgr().headRemindOnChat()) {
//            return;
//        }

        getChatInstance().chatInputPanel.clearInputEdit();

        SmileItem item = (SmileItem) parent.getAdapter().getItem(position);
        ChatAdapter chatAdapter = getChatInstance().chatAdapter;

//        ModuleMgr.getChatMgr().sendMiniGameMsg(chatAdapter.getFolder(), chatAdapter.getChannelId(), chatAdapter.getWhisperId(),
//                ModuleMgr.getCenterMgr().getMyInfo().getUser().getUid(),
//                item.getId(), item.getName(), item.getIcon(), item.getPic(),
//                "");
    }
}
