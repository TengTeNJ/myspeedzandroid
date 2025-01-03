package com.potent.common.event;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO< 说明 >
 * @date: 2014/12/28 14:40
 */
public class SpeedUpdate {
    private int UserID;
    private boolean userSecent;
    public SpeedUpdate(int userID) {
        UserID = userID;
    }

    public SpeedUpdate(int userID, boolean userSecent) {
        UserID = userID;
        this.userSecent = userSecent;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public boolean isUserSecent() {
        return userSecent;
    }

    public void setUserSecent(boolean userSecent) {
        this.userSecent = userSecent;
    }
}
