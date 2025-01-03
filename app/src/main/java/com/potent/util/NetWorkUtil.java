package com.potent.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject MoTKAPP
 * @description: TODO< 网络相关的工具 >
 * @date: 2014/10/24 18:34
 */
public class NetWorkUtil {
    /**
     * 对网络连接状态进行判断
     * @return  true, 可用； false， 不可用
     */
    public  boolean isOpenNetwork(Context context) {

        if(null!=context){
            ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ant=connManager.getActiveNetworkInfo();
            if(null!=ant) {
                boolean isAvailable=connManager.getActiveNetworkInfo().isAvailable();
                return isAvailable;
            }
        }
        return false;
    }
}
