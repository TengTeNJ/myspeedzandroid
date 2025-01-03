package com.potent.common.event;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO< 说明 >
 * @date: 2014/10/16 15:08
 */
public class DownLoadEvent {
    private long mileage;
    private mileageType actionType;

    public DownLoadEvent(long mileage, mileageType TYPE) {
        this.mileage = mileage;
        actionType = TYPE;
    }

    public long getMileage() {
        return mileage;
    }

    public mileageType getActionType() {
        return actionType;
    }

    public enum mileageType {
        START,
        DOWNLOAD,
        SUCCESS,
        ERRO,
        STOP,
    }
}
