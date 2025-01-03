package com.potent.common.listener;

/**
 * TODO<返回异步提交数据的结果>
 *
 * @author Gaohaosk
 * @data: 2014年8月1日 上午12:26:38
 * @version: V1.0
 */
public interface PostLisener {
    /**
     * 成功接收数据库返回信息
     */
    public void success(String res);

    /**
     * 未收到任何信息
     */
    public void erro(String res);
}
