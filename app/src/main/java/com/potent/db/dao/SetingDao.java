package com.potent.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.potent.common.beans.SetingBeans;
import com.potent.common.beans.UserBeans;
import com.potent.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject GsonCache
 * @description: TODO< 错题明细DAO层 >
 * @date: 2014/11/22 16:23
 */
public class SetingDao {
    private Dao<SetingBeans, Integer> dao;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public SetingDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper();
            dao = helper.getDao(SetingBeans.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个小题
     *
     * @param resTopicDao 错题明细
     */
    public void add(SetingBeans resTopicDao)
    {
        try
        {
            dao.create(resTopicDao);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public List<SetingBeans> queryForAll(){
        List<SetingBeans> listRes=null;
        try {
            listRes=dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
    public SetingBeans queryByID(int id){
        SetingBeans listRes=null;
        try {
            listRes=dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
    public SetingBeans queryTop(){
        List<SetingBeans> listRes=null;
        try {
            listRes=dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null==listRes||listRes.size()==0?null:listRes.get(listRes.size()-1);
    }
}
