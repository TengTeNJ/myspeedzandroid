package com.potent.common.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO<说明>
 * @date: 2014/11/23 15:39
 */
public class DateData {
    @DatabaseField(generatedId = true, index = true)
    private int ID;
    @DatabaseField(foreign = true)
    private UserBeans userBeans;
    @DatabaseField
    private String dataTime;
    @ForeignCollectionField(eager = false)
    private Collection<SpeedData> OptionGroups;

    public UserBeans getUserBeans() {
        return userBeans;
    }

    public void setUserBeans(UserBeans userBeans) {
        this.userBeans = userBeans;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public Collection<SpeedData> getOptionGroups() {
        return OptionGroups;
    }

    public ArrayList<SpeedData> getOptionGroupsList() {
        ArrayList<SpeedData> Options = new ArrayList<SpeedData>();
        for (SpeedData group : OptionGroups) {
            Options.add(group);
        }
        return Options;
    }

    public ArrayList<SpeedData> getOptionGroupsListDesc() {
        ArrayList<SpeedData> Options = new ArrayList<SpeedData>();
        if (OptionGroups != null) {
            for (SpeedData group : OptionGroups) {
                Options.add(0, group);
            }
        }
        return Options;
    }

    public void setOptionGroups(Collection<SpeedData> optionGroups) {
        OptionGroups = optionGroups;
    }
}
