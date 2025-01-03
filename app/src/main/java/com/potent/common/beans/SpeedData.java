package com.potent.common.beans;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO<说明>
 * @date: 2014/11/23 15:38
 */
public class SpeedData {
    @DatabaseField(foreign = true)
    private DateData date;
    @DatabaseField(generatedId = true, index = true)
    private int ID;
    @DatabaseField
    private String dateTime;
    @DatabaseField
    private int speed;
    @DatabaseField
    private boolean isSecend;

    public DateData getDate() {
        return date;
    }

    public void setDate(DateData date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getSpeed(boolean isKPH) {
        return isKPH ? speed : (int) (speed * 0.6214 + 0.5);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isSecend() {
        return isSecend;
    }

    public void setSecend(boolean isSecend) {
        this.isSecend = isSecend;
    }
}
