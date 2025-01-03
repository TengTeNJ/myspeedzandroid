package com.potent.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.potent.R;
import com.potent.ui.ActivityLungcher;
import com.potent.util.AppLanguageUtils;
import com.potent.util.SPUtils;
import com.zyq.easypermission.EasyPermissionHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO< 说明 >
 * @date: 2014/10/16 12:42
 */
public class PotentApplication extends Application {
    private static Context m_wcontext;
    private SharedPreferences m_wsp;

    @Override
    public void onCreate() {
        super.onCreate();
        m_wcontext = getApplicationContext();
        EasyPermissionHelper.getInstance().init(this);
        m_wsp = SPUtils.getInstance(m_wcontext, Constants.SPNAME, Context.MODE_PRIVATE);
        SPUtils.setSharedPreferences(m_wsp, getDeviceInfo());//将设备信息存入SharedPreferences

        Resources resources =getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        AppLanguageUtils.changeAppLanguage(m_wcontext);
        resources.updateConfiguration(config, dm);
    }
    public static Context getContext(){

        return m_wcontext;

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AppLanguageUtils.changeAppLanguage(m_wcontext);
    }

    /**
     * 获取设备信息
     *
     * @return 设备信息
     * @author Young
     * @since 2014-7-2 <br/> version:4.0
     */
    private List<String> getDeviceInfo() {
        List<String> infos = new ArrayList<String>();
        //1.设备型号
        infos.add(Constants.DEVICETYPEKEY + ":" + android.os.Build.MODEL);
        /*//2.设备系统版本
        infos.add(Constants.DEVICEOSVERSIONKEY + ":" + android.os.Build.VERSION.RELEASE);
        //3.设备IMEI码
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        infos.add(Constants.DEVICEIMEIKEY + ":" + tm.getDeviceId());
        SIMCardInfo cardInfo = new SIMCardInfo(getApplicationContext());
        infos.add(Constants.CALLNUMKEY + ":" + cardInfo.getNativePhoneNumber());
        infos.add(Constants.PROVIDERS + ":" + cardInfo.getProvidersName());*/
        //4.设备分辨率
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        infos.add(Constants.DEVICESCREENKEY + ":" + screenWidth + "*" + screenHeight);
        infos.add(Constants.DEVICESCREENKEY_W + ":" + screenWidth);
        infos.add(Constants.DEVICESCREENKEY_H + ":" + screenHeight);
        //5.安装时间
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (PackageInfo p : packageInfoList) {
            if (p.packageName.contains(this.getPackageName())) {
                infos.add(Constants.APKINSTALLTIMEKEY + ":" +
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                new Date(getDir(p.packageName, 0).lastModified())));
                break;
            }
        }
        return infos;
    }
}
