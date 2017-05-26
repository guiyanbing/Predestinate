package com.juxin.predestinate.ui.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.FileUtil;
import com.juxin.library.utils.InputUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.WebUtil;

/**
 * 用户搜索测试彩蛋
 */
public class SearchTestActivity extends BaseActivity {

    private EditText url_edit, uid_edit, db_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_search_test_activity);
        setBackView("测试彩蛋");

        initView();
    }

    private void initView() {
        url_edit = (EditText) findViewById(R.id.url_edit);
        uid_edit = (EditText) findViewById(R.id.uid_edit);
        db_edit = (EditText) findViewById(R.id.db_edit);

        url_edit.setText(PSP.getInstance().getString(FinalKey.TESTING_WEB_STORE, ""));
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputUtils.forceClose(this);
    }

    /**
     * 进入测试用的H5页面
     */
    public void startWeb(View v) {
        final String url = url_edit.getText().toString();
        if (TextUtils.isEmpty(url)) {
            PToast.showShort("请输入测试网址");
            return;
        }
        if (!WebUtil.isUrl(url)) {
            PToast.showShort("请输入正确的url网址");
            return;
        }
        PSP.getInstance().put(FinalKey.TESTING_WEB_STORE, url);
        UIShow.showWebActivity(this, url);
    }

    /**
     * 搜索用户
     */
    public void searchUID(View v) {
        final String uid = uid_edit.getText().toString();
        if (TextUtils.isEmpty(uid)) {
            PToast.showShort("请输入UID");
            return;
        }
        UIShow.showCheckOtherInfoAct(this, Long.parseLong(uid));
    }

    /**
     * 拷贝数据库
     */
    public void copyDB(View v) {
        final String DB_NAME = db_edit.getText().toString();
        if (TextUtils.isEmpty(DB_NAME)) {
            PToast.showShort("请输入数据库名称");
            return;
        }
        final String path = App.context.getDatabasePath(DB_NAME).getPath();
        final String storePath = DirType.getCacheDir() + DB_NAME + ".bak";
        if (FileUtil.fileCopy(path, storePath)) {
            PToast.showLong("请到" + storePath + "查看");
        } else {
            PToast.showShort("数据库文件不存在或复制失败");
        }
    }

    /**
     * 切换服务器地址
     */
    public void switchServer(View v) {
        // TODO: 2017/5/25
    }
}
