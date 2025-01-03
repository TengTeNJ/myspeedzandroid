package com.potent.ui.adapter;


import com.potent.common.beans.TabViewBeans;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @author gaohaosk
 * @Copyright: ©2014 MeWifi
 * @Description: TODO<二级页面TabViewPager适配>
 * @data: 2014/8/6 14:29
 * @version: V1.0
 */
public class TabViewPageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<TabViewBeans> m_w_views;

    public ArrayList<TabViewBeans> getM_w_views() {
        return m_w_views;
    }

    public void setM_w_views(ArrayList<TabViewBeans> m_w_views) {
        this.m_w_views = m_w_views;
    }

    public TabViewPageAdapter(FragmentManager fm) {
        super(fm);
        m_w_views=new ArrayList<TabViewBeans>();
    }
    public void setListView(ArrayList<TabViewBeans> views){
        if(null!=m_w_views){
            m_w_views.clear();
        }
        this.m_w_views=views;
    }
    @Override
    public Fragment getItem(int i) {
        return m_w_views.get(i).getFragView();
    }

    @Override
    public int getCount() {
        return m_w_views.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return m_w_views.get(position).getViewTitle();
    }
}
