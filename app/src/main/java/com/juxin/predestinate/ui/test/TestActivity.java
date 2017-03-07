package com.juxin.predestinate.ui.test;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.User;
import com.juxin.predestinate.bean.db.UserDao;
import com.juxin.predestinate.module.logic.base.BaseActivity;
import com.juxin.predestinate.module.logic.db.DBManager;

import java.util.List;

/**
 * Created by ZRP on 2017/2/7.
 */
public class TestActivity extends BaseActivity implements View.OnClickListener {

    private EditText editText;
    private ListView listView;

    private UserDao userDao;
    private List<User> userList;
    private TestAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        initView();
        initData();
    }

    private void initView() {
        Button btn_add = (Button) findViewById(R.id.btn_add);
        Button btn_query = (Button) findViewById(R.id.btn_query);
        Button btn_update = (Button) findViewById(R.id.btn_update);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_add.setOnClickListener(this);
        btn_query.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.et_name);
        listView = (ListView) findViewById(R.id.lv_user);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteUser(userList.get(position).getNickname());
            }
        });
    }

    private void initData() {
        userDao = DBManager.getInstance().getSession().getUserDao();
        userList = userDao.queryBuilder().build().list();
        userAdapter = new TestAdapter(this, userList);
        listView.setAdapter(userAdapter);
    }

    private void refreshListView() {
        editText.setText("");
        userList.clear();
        userList.addAll(userDao.queryBuilder().build().list());
        userAdapter.notifyDataSetChanged();
    }

    //查询
    private void queryUser(String name) {
        List result = userDao.queryBuilder().where(UserDao.Properties.Nickname.eq(name)).build().list();
        PToast.showShort("查询到：" + result.size() + "条结果");
    }

    //更新
    private void updateUser(String prevName, String newName) {
        User findWine = userDao.queryBuilder().where(UserDao.Properties.Nickname.eq(prevName)).build().unique();
        if (findWine != null) {
            findWine.setNickname(newName);
            userDao.update(findWine);
            PToast.showShort("修改成功");
        } else {
            PToast.showShort("用户不存在");
        }
        refreshListView();
    }

    //删除
    private void deleteUser(String name) {
        List<User> list = userDao.queryBuilder().where(UserDao.Properties.Nickname.eq(name)).list();
        if (list != null && !list.isEmpty()) {
            for (User user : list) {
                userDao.deleteByKey(user.getId());
            }
            PToast.showShort("删除成功，共删除" + list.size() + "条数据");
        } else {
            PToast.showShort("用户不存在");
        }

        refreshListView();
    }

    //插入
    private void insertUser(String name, String avatar) {
        User user = new User();
        user.setUid(System.currentTimeMillis());
        user.setNickname(name);
        user.setAvatar(avatar);
        userDao.insertOrReplace(user);//尽量使用该方法去插入，以避免unique键重复而报错

        refreshListView();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_add:
                insertUser(editText.getText().toString(), "" + System.currentTimeMillis());
                break;
            case R.id.btn_query:
                queryUser(editText.getText().toString());
                break;
            case R.id.btn_update:
                updateUser(editText.getText().toString(), "" + System.currentTimeMillis());
                break;
            case R.id.btn_delete:
                deleteUser(editText.getText().toString());
                break;
            default:
                break;
        }
    }
}