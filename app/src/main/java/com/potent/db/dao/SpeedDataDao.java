package com.potent.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SpeedData;
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
public class SpeedDataDao {
    private Dao<SpeedData, Integer> dao;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public SpeedDataDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper();
            dao = helper.getDao(SpeedData.class);
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
    public void add(SpeedData resTopicDao)
    {
        try
        {
            dao.create(resTopicDao);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public List<SpeedData> queryForAll(){
        List<SpeedData> listRes=null;
        try {
            listRes=dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
    public void deleteByID(int id){
        try {
            SpeedData dateData=dao.queryForId(id);
            dao.delete(dateData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public SpeedData queryByID(int id){
        SpeedData listRes=null;
        try {
            listRes=dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
}
