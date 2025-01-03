package com.potent.common.beans;


import androidx.fragment.app.Fragment;

/**
 * @author gaohaosk
 * @Copyright: ©2014 MeWifi
 * @Description: TODO<ViewPage 的句柄>
 * @data: 2014/8/6 14:33
 * @version: V1.0
 */
public class TabViewBeans {
    private String viewTitle;
    private Fragment fragView;
    private int UserId;

    public TabViewBeans(){}

    public TabViewBeans(String viewTitle, Fragment fragView, int userId) {
        this.viewTitle = viewTitle;
        this.fragView = fragView;
        UserId = userId;
    }

    public String getViewTitle() {
        return viewTitle;
    }

    public Fragment getFragView() {
        return fragView;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setViewTitle(String viewTitle) {
        this.viewTitle = viewTitle;
    }

    public void setFragView(Fragment fragView) {
        this.fragView = fragView;
    }
}
