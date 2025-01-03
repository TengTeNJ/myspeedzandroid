package com.potent.common.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO< UserModel >
 * @date: 2014/11/16 20:15
 */
public class UserBeans   {
    @DatabaseField(generatedId = true,index = true)
    private int ID;
    @DatabaseField
    private String userName;
    @DatabaseField
    private boolean doubleModel;
    @DatabaseField
    private int ID1;
    @DatabaseField
    private String userName1;
    @ForeignCollectionField(eager = false)
    private Collection<DateData> dates;

    public Collection<DateData> getDates() {
        return dates;
    }
    public ArrayList<DateData> getDatesList() {
        ArrayList<DateData> dateDatas=new ArrayList<DateData>();
        for(DateData dateData:dates){
            dateDatas.add(0,dateData);
        }
        return dateDatas;
    }
    public DateData getTopDate(){
        ArrayList<DateData> dateDatas=new ArrayList<DateData>();
        for(DateData dateData:dates){
            dateDatas.add(dateData);
        }
        return dateDatas.size()==0?null:dateDatas.get(0);
    }
    public void setDates(Collection<DateData> dates) {
        this.dates = dates;
    }

    public UserBeans() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isDoubleModel() {
        return doubleModel;
    }

    public void setDoubleModel(boolean doubleModel) {
        this.doubleModel = doubleModel;
    }

    public int getID1() {
        return ID1==ID?ID+1:ID1;
    }

    public void setID1(int ID1) {
        this.ID1 = ID1;
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }
}
