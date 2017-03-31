package com.juxin.predestinate.module.local.msgview.utils;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.juxin.android.framework.JXToast;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.mumu.bean.utils.DeviceUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.location.LocationMgr;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.TimeUtil;

public class EggUtil {
    private static EggUtil ourInstance = new EggUtil();

    public static EggUtil getInstance() {
        return ourInstance;
    }

    private EggUtil() {
    }

    private long lastTime = 0;
    private int count = 1;
    private String eggContent = "";


    public boolean showEgg(ChatAdapter chatAdapter) {
        if (showEgg()) {
            if (chatAdapter != null) {
                chatAdapter.getChatInstance().chatViewLayout.setDisableIntercept(true);

                JXToast.show("准备数据中");
                createEgg();
//                CustomMessage customMessage = new CustomMessage(chatAdapter.getChannelId(), chatAdapter.getWhisperId(), getEggContent());
//                customMessage.eggMsg = true;
//                chatAdapter.addClientCustomTip(customMessage);
            }

            return true;
        }

        return false;
    }

    /**
     * 是否显示彩蛋。
     *
     * @return true显示。
     */
    private synchronized boolean showEgg() {
        if (System.currentTimeMillis() - lastTime < 500) {
            lastTime = System.currentTimeMillis();
            ++count;
        } else {
            lastTime = System.currentTimeMillis();
            count = 1;
            return false;
        }

        if (count == 5) {
            lastTime = 0;
            count = 1;
            return true;
        }

        return false;
    }

    /**
     * 获取需要显示的内容。
     *
     * @return 显示内容。
     */
    private String getEggContent() {
        return eggContent;
    }


    /**
     * 生成彩蛋内容。
     */
    private void createEgg() {
        eggContent = "";

        try {
            StringBuilder sb = new StringBuilder();
//
            sb.append("").append("uid: ").append(App.uid);
            sb.append("\n").append("cookie: ").append(App.cookie);
            sb.append("\n").append("islogin: ").append(App.isLogin);

            UserDetail userInfo = ModuleMgr.getCenterMgr().getMyInfo();

            if (userInfo != null) {
                sb.append("\n").append("nickname: ").append(userInfo.getNickname());
                sb.append("\n").append("sex: ").append(userInfo.getGender() == 1 ? "男" : "女");
            }

           // sb.append("\n").append("hosturl: ").append(CommonConfig.getInstance().getLogic_server());

            sb.append("\n").append("vercode: ").append(ModuleMgr.getAppMgr().getVerCode());
            sb.append("\n").append("vername: ").append(ModuleMgr.getAppMgr().getVerName());
            sb.append("\n").append("packagename: ").append(ModuleMgr.getAppMgr().getPackageName());
            sb.append("\n").append("mainchannel: ").append(ModuleMgr.getAppMgr().getMainChannel());
            sb.append("\n").append("subchannel: ").append(ModuleMgr.getAppMgr().getSubChannel());
            sb.append("\n").append("imei: ").append(ModuleMgr.getAppMgr().getIMEI());
            sb.append("\n").append("imsi: ").append(ModuleMgr.getAppMgr().getIMSI());
            sb.append("\n").append("mac: ").append(ModuleMgr.getAppMgr().getMAC());
          //  sb.append("\n").append("forground: ").append(ModuleMgr.getAppMgr().isForground());

            sb.append("\n").append("debug: ").append(ModuleMgr.getAppMgr().isDebug());
            sb.append("\n").append("curaddr: ").append(LocationMgr.getInstance().getPointD().addr);

            PackageInfo packageInfo = App.context.getPackageManager().getPackageInfo(App.context.getPackageName(), 0);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
                sb.append("\n").append("firstinstalltime:  ").append(TimeUtil.getFormatTimeEgg(packageInfo.firstInstallTime));
                sb.append("\n").append("lastupdatetime: ").append(TimeUtil.getFormatTimeEgg(packageInfo.lastUpdateTime));
            }

            sb.append("\n").append("型号: ").append(DeviceUtil.model);
            sb.append("\n").append("版本号: ").append(DeviceUtil.display);
            sb.append("\n").append("生产商: ").append(DeviceUtil.manufacturer);
            sb.append("\n").append("Android版本号: ").append(Build.VERSION.RELEASE);
            sb.append("\n").append("系统版本号: ").append(Build.VERSION.INCREMENTAL);

          //  sb.append("\n").append("前台: ").append(ModuleMgr.getAppMgr().isForground());
          //  sb.append("\n").append("运行次数: ").append(App.runCount);

            sb.append("\n").append("使用固定GPS位置: ").append(LocationMgr.getInstance().isUseFixPlace());
            sb.append("\n").append("经度: ").append(LocationMgr.getInstance().getPointD().latitude);
            sb.append("\n").append("维度: ").append(LocationMgr.getInstance().getPointD().longitude);

            eggContent = sb.toString();
        } catch (Throwable t) {

        }
    }

    public View getEggView() {
        TableLayout layout = new TableLayout(App.context);
        layout.setColumnStretchable(0, true);
        layout.setColumnStretchable(1, true);
        TableRow row = null;

        row = new TableRow(App.context);
        row.addView(useFixPlace());
        layout.addView(row);

        row = new TableRow(App.context);
        layout.addView(useGPSPlace());
        layout.addView(row);

        return layout;
    }


    private ViewGroup newFunView(int index) {
        CustomFrameLayout customFrameLayout = (CustomFrameLayout) LayoutInflater.from(App.context).inflate(R.layout.p1_egg_item, null);
        customFrameLayout.showOfIndex(index);
        return customFrameLayout;
    }

    private View useFixPlace() {
        ViewGroup viewGroupParent = newFunView(0);
        ViewGroup viewGroup = (ViewGroup) viewGroupParent.findViewById(R.id.set_place);

        final EditText editTextlon = (EditText) viewGroup.findViewById(R.id.longitude);
        final EditText editTextlat = (EditText) viewGroup.findViewById(R.id.latitude);

        viewGroup.findViewById(R.id.set_place_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LocationMgr.getInstance().setFixPlace(Double.valueOf(editTextlon.getText().toString()), Double.valueOf(editTextlat.getText().toString()));
                    JXToast.show("设置成功");
                } catch (Exception e) {
                    JXToast.show("设置失败");
                }
            }
        });

        return viewGroupParent;
    }

    private View useGPSPlace() {
        ViewGroup viewGroupParent = newFunView(1);
        ViewGroup viewGroup = (ViewGroup) viewGroupParent.findViewById(R.id.set_place_setting);

        viewGroup.findViewById(R.id.set_place_setting_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LocationMgr.getInstance().setUseFixPlace(false);
                    JXToast.show("设置成功");
                } catch (Exception e) {
                    JXToast.show("设置失败");
                }
            }
        });

        return viewGroupParent;
    }
}
