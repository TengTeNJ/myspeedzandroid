package com.potent.util;


import com.potent.common.event.DownLoadEvent;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO< HTTP工具，如下载文件，访问服务器等 >
 * @date: 2014/10/16 15:16
 */
public class HttpTool {
    private URL url = null;

    /**
     * 下载文件
     * @param urlStr 文件链接
     * @param path 存放位置
     * @param fileName 存放名字
     */
    public void downFile(String urlStr, String path, String fileName) {

        InputStream in = null;
        long filelenth = 0;
        try {
            FileUtils fileUtils = new FileUtils();
            //如果文件存在，则删除文件
            fileUtils.deleteFileExist(fileName, path);
            // 创建一个URL对象
            url = new URL(urlStr);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            filelenth = urlConn.getContentLength();
            EventBus.getDefault().post(new DownLoadEvent(filelenth, DownLoadEvent.mileageType.START));
            in = urlConn.getInputStream();
            // 使用IO流读取数据
            int resultFile = fileUtils.write2SDFromInput(path, fileName, in);
            if (resultFile == -1) {
                EventBus.getDefault().post(new DownLoadEvent(filelenth, DownLoadEvent.mileageType.ERRO));
                return;
            } else if (resultFile == 1) {
                EventBus.getDefault().post(new DownLoadEvent(filelenth, DownLoadEvent.mileageType.SUCCESS));
            }
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new DownLoadEvent(filelenth, DownLoadEvent.mileageType.ERRO));
            return;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
