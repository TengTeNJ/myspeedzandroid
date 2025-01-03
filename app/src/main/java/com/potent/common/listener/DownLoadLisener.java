package com.potent.common.listener;

/**
 * TODO<返回下载文件结果的回调>
 *
 * @author Gaohaosk
 * @data: 2014年8月1日 上午12:26:21
 * @version: V1.0
 */
public interface DownLoadLisener {
    /**
     * 下载成功
     */
    public void success();

    /**
     * 下载失败
     */
    public void erro();
}
