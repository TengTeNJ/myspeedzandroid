package com.potent.server;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;


import java.io.File;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO< APP服务,软件更新，异常日志上传 >
 * @date: 2014/10/15 11:37
 */
public class PotentService extends Service {
    public static final String ACTION = "com.ruanyun.motk.POTENTERVICE";
    private static final String SDCardRoot = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator;

    @Override
    public void onStart(Intent intent, final int startId) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(null==intent){
            //new CrashTogThread().start();
            return START_STICKY;
        }else{
            return super.onStartCommand(intent,flags,startId);
        }


    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
