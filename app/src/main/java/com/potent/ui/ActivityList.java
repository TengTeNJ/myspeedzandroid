package com.potent.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.potent.R;
import com.potent.common.Constants;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SpeedBeans;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.UserBeans;
import com.potent.db.dao.DateDataDao;
import com.potent.db.dao.UserDao;
import com.potent.ui.adapter.ListAdapter;
import com.potent.ui.base.BaseActivity;
import com.potent.ui.fragment.login.other.ListFragment;
import com.potent.ui.view.CustomDialog;
import com.potent.util.SPUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;
import butterknife.BindView;

public class ActivityList extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager pager;


    private SharedPreferences instance;
    private String Model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__list);
        ButterKnife.bind(this);
        setTile(R.string.list);
        showBack(getString(R.string.title_activity_activity_main), null);
        instance = SPUtils.getInstance(getApplicationContext(),
                Constants.SPNAME, Context.MODE_PRIVATE);
        Model = instance.getString(Constants.Model, "Single Model");
        List<ListFragment> fragments = new ArrayList<ListFragment>();
        //2.将前面定义的三个Fragment类对应的实例放入Fragment列表
        fragments.add(ListFragment.newInstance(false));

        if ("Battle Model".equals(Model)) {
            fragments.add(ListFragment.newInstance(true));
        }


        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        //4.创建ViewPager实例，并绑定适配器
        pager.setAdapter(adapter);
    }


    public class FragAdapter extends FragmentStatePagerAdapter {
        //1.创建Fragment数组
        private List<ListFragment> mFragments;

        //2.接收从Activity页面传递过来的Fragment数组
        public FragAdapter(FragmentManager fm, List<ListFragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
