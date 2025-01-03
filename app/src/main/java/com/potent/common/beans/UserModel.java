package com.potent.common.beans;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO< 说明 >
 * @date: 2014/11/23 15:36
 */
public class UserModel {
    @DatabaseField(generatedId = true,index = true)
    private int ID;
    @DatabaseField
    private int model;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
