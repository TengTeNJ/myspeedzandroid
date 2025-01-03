package com.potent.common.beans;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO< 说明 >
 * @date: 2014/11/23 21:16
 */
public class SetingBeans {
    @DatabaseField(generatedId = true,index = true)
    private int ID;
    @DatabaseField
    private String SpeedUnits;
    @DatabaseField
    private String model;
    @DatabaseField
    private String Language;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSpeedUnits() {
        return SpeedUnits;
    }

    public void setSpeedUnits(String speedUnits) {
        SpeedUnits = speedUnits;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }
}
