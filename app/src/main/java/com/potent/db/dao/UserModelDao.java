package com.potent.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.potent.common.beans.UserModel;
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
public class UserModelDao {
    private Dao<UserModel, Integer> dao;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public UserModelDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper();
            dao = helper.getDao(UserModel.class);
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
    public void add(UserModel resTopicDao)
    {
        try
        {
            dao.create(resTopicDao);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public List<UserModel> queryForAll(){
        List<UserModel> listRes=null;
        try {
            listRes=dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
    public UserModel queryByID(int id){
        UserModel listRes=null;
        try {
            listRes=dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRes;
    }
}
