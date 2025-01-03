package com.potent.util;

import android.app.Activity;
import android.content.Context;


import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO<根据CPU性能创建线程池，创建异步任务>
 *
 * @author Gaohaosk
 * @data: 2014年8月1日 上午12:21:55
 * @version: V1.0
 */
public class AsyncExecut {
    /**
     * 线程池
     */
    private static AsyncExecut post = null;
    //获取设备的CPU个数，在配置线程池，在需要开辟线程的时候从线程池中取
    int cpuNums = Runtime.getRuntime().availableProcessors();
    private ExecutorService mExeCutorService = Executors.newFixedThreadPool(cpuNums * 10);
    private Activity mActivity;

    /**
     * 构造时初始化httpclient
     */
    private AsyncExecut() {

    }

    /**
     * 单例构造
     * @return
     */
    public static AsyncExecut getAsyncPost() {
        if (null == post) {
            synchronized (AsyncExecut.class) {
                if (null == post) {
                    post = new AsyncExecut();
                }
            }
        }
        return post;
    }

    /**
     * @return 返回线程池
     */
    public ExecutorService getmExeCutorService(){
        return mExeCutorService;
    }
    /**
     * TODO<异步下载数据>
     *
     * @param url      服务器文件URL
     * @param path     要存放的本地路径
     * @param filename 本地命名
     * @param lisener  下载过程中下载进度的回调
     * @return void
*/    public void downLoad(final String url, final String path, final String filename) {
        mExeCutorService.submit(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                new HttpTool().downFile(url, path, filename);
            }

        });
    }
}
