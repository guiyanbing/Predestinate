package com.juxin.predestinate.ui.user.update;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.request.DownloadListener;
import com.juxin.library.utils.APKUtil;
import com.juxin.mumu.bean.utils.DirUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;

/**
 * 软件升级dialog
 */
public class UpdateDialog extends BaseDialogFragment implements DownloadListener, View.OnClickListener {

    private FragmentActivity activity;
    private DownloadingDialog downloadingDialog;
    private TextView title_version, update_info, update_cancel, update_submit;

    private AppUpdate appUpdate;    //软件更新信息
    private Runnable runnable;      //非强制更新时点击取消按钮执行的操作

    public UpdateDialog() {
        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
        setGravity(Gravity.CENTER);
        setDialogSizeRatio(0.8, 0);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.p1_app_update_dialog);

        View contentView = getContentView();
        initView(contentView);
        return contentView;
    }

    private void initView(View view) {
        downloadingDialog = new DownloadingDialog();

        title_version = (TextView) view.findViewById(R.id.title_version);
        update_info = (TextView) view.findViewById(R.id.update_info);

        update_cancel = (TextView) view.findViewById(R.id.update_cancel);
        update_submit = (TextView) view.findViewById(R.id.update_submit);

        update_cancel.setOnClickListener(this);
        update_submit.setOnClickListener(this);

        initData();
    }

    private void initData() {
        if (appUpdate == null) return;

        title_version.setText("发现新版本 " + appUpdate.getTitle());
        update_info.setText(appUpdate.getSummary());
        update_cancel.setText(appUpdate.getForce() == 1 ? "退出" : "取消");
    }

    /**
     * 初始化数据
     *
     * @param appUpdate 软件升级信息
     */
    public void setData(AppUpdate appUpdate) {
        this.appUpdate = appUpdate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_cancel:
                dismiss();
                if (runnable != null) runnable.run();
                break;
            case R.id.update_submit:
                update_cancel.setEnabled(false);
                update_submit.setEnabled(false);

                dismiss();
                downloadingDialog.showDialog(activity);

                String fileName = DirUtils.getDir(DirUtils.DirType.DT_SD_EXT_Cache_APK) +
                        "xiaou_" + appUpdate.getVersion() + ".apk";
                ModuleMgr.getHttpMgr().download(appUpdate.getUrl(), fileName, this);
                break;
        }
    }

    @Override
    public void onStart(String url, String fileName) {
    }

    @Override
    public void onProcess(String url, int process, long size) {
        downloadingDialog.updateProgress(process);
    }

    @Override
    public void onSuccess(String url, String filePath) {
        downloadingDialog.dismiss();
        if (appUpdate.getForce() == 1) showDialog(activity);// 重新调起升级弹窗
        APKUtil.installAPK(activity, filePath);
    }

    @Override
    public void onFail(String url, Throwable throwable) {
        PToast.showShort("安装包下载失败，请重试");
        update_cancel.setEnabled(true);
        update_submit.setEnabled(true);

        // 重新调起升级弹窗
        downloadingDialog.dismiss();
        showDialog(activity);
    }

    @Override
    public void showDialog(FragmentActivity context) {
        super.showDialog(context);
        this.activity = context;
    }
}
