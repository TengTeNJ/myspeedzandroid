package com.potent.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.potent.common.beans.DateData;
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
public class DateDataDao {
    private Dao<DateData, Integer> dao;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public DateDataDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper();
            dao = helper.getDao(DateData.class);
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
    public void add(DateData resTopicDao)
    {
        try
        {
            dao.create(resTopicDao);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public List<DateData> queryForAll(){
        List<DateData> listRes=null;
        try {
            listRes=dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
    public void deleteAll(){
        try {
            List<DateData> listRes=dao.queryForAll();
            for (DateData dateData:listRes)
            dao.delete(dateData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteByID(int id){
        try {
            DateData dateData=dao.queryForId(id);
            dao.delete(dateData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public DateData queryByID(int id){
        DateData listRes=null;
        try {
            listRes=dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
    public List<DateData> queryByDate(String date){
        List<DateData> listRes=null;
        try {
            listRes=dao.queryForEq("dataTime",date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
    public DateData queryTop(int userId){
        List<DateData> listRes=null;
        try {
            listRes=dao.queryForEq("userBeans_id",userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null==listRes||listRes.size()==0?null:listRes.get(listRes.size()-1);
    }
}
