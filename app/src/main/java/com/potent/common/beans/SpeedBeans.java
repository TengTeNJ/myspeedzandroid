package com.potent.common.beans;

/**
 * Created by 高鹏 on 2014/10/26.
 */
public class SpeedBeans {
    private int ID;
    private String date;
    private String speed;
    private boolean secend;
    public SpeedBeans() {
    }

    public SpeedBeans(String date, String speed) {
        this.date = date;
        this.speed = speed;
    }

    public SpeedBeans(int ID, String date, String speed) {
        this.ID = ID;
        this.date = date;
        this.speed = speed;
    }

    public SpeedBeans(int ID, String date, String speed, boolean secend) {
        this.ID = ID;
        this.date = date;
        this.speed = speed;
        this.secend = secend;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public boolean isSecend() {
        return secend;
    }

    public void setSecend(boolean secend) {
        this.secend = secend;
    }
}
