package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 发现筛选
 * Created by zhang on 2017/4/28.
 */

public class DiscoverSelectDialog extends BaseDialogFragment implements View.OnClickListener {

    private ListView listView;

    private View btn_cancle;

    private ArrayAdapter<String> adapter;

    private OnDialogItemClick onDialogItemClick;

    public DiscoverSelectDialog() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(1, 0);
        setCancelable(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_discover_select_dialog);
        initView();
        initData();
        return getContentView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.discover_select_list);
        btn_cancle = findViewById(R.id.discover_select_cancle);
        btn_cancle.setOnClickListener(this);
    }

    private void initData() {
        String[] selectData = getResources().getStringArray(R.array.discover_select_item);
        List<String> stringList = new ArrayList<>();
        Collections.addAll(stringList, selectData);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.f1_discover_select_item, R.id.discover_select_item_text, stringList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (onDialogItemClick != null) {
                    onDialogItemClick.onDialogItemCilck(adapterView, view, i);
                }
            }
        });
    }

    public void setOnItemClick(final OnDialogItemClick onItemClick) {
        onDialogItemClick = onItemClick;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discover_select_cancle:
                dismiss();
                break;
        }
    }

    public interface OnDialogItemClick {
        /**
         * @param
         */
        void onDialogItemCilck(AdapterView<?> parent, View view, int position);
    }

}
