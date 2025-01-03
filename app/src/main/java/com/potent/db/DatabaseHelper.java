package com.potent.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.potent.common.PotentApplication;
import com.potent.common.beans.DateData;
import com.potent.common.beans.SetingBeans;
import com.potent.common.beans.SpeedData;
import com.potent.common.beans.UserBeans;
import com.potent.common.beans.UserModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO< SQLITE数据库配置类，负责创建和更新表结构，返回Daos >
 * @date: 2014/11/21 15:47
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	protected AndroidConnectionSource connectionSource = new AndroidConnectionSource(this);

	//数据库存在于手机文件的名字
	private static final String DATABASE_NAME = "Potent.db";
	// 数据库表结构版本，用于app更新表结构
	private static final int DATABASE_VERSION = 2;
    //Dao层的堆栈，方便管理。
    private Map<String, Dao> daos = new HashMap<String, Dao>();
    //单例
    private static DatabaseHelper instance;
	// 构造函数
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    /**
     * 单例获取该Helper
     *
     * @return this
     */
    public static synchronized DatabaseHelper getHelper()
    {
       Context context = PotentApplication.getContext();
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }
	/**
     * 第一次创建database时启动此方法,创建表
	 *
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		DatabaseConnection conn = connectionSource.getSpecialConnection();
		boolean clearSpecial = false;
		if (conn == null) {
			conn = new AndroidDatabaseConnection(db, true);
			try {
				connectionSource.saveSpecialConnection(conn);
				clearSpecial = true;
			} catch (SQLException e) {
				throw new IllegalStateException("Could not save special connection", e);
			}
		}
		try {
			onCreate();
		} finally {
			if (clearSpecial) {
				connectionSource.clearSpecialConnection(conn);
			}
		}
	}

    /**
     * 数据库表结构有更新的版本时调用此方法
     * @param db 数据库
     * @param oldVersion 当前运行的版本
     * @param newVersion 更新版本
     */
	@Override
	public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DatabaseConnection conn = connectionSource.getSpecialConnection();
		boolean clearSpecial = false;
		if (conn == null) {
			conn = new AndroidDatabaseConnection(db, true);
			try {
				connectionSource.saveSpecialConnection(conn);
				clearSpecial = true;
			} catch (SQLException e) {
				throw new IllegalStateException("Could not save special connection", e);
			}
		}
		try {
			onUpgrade(oldVersion, newVersion);
		} finally {
			if (clearSpecial) {
				connectionSource.clearSpecialConnection(conn);
			}
		}
	}

	/**
	 * 销毁时将所有的Dao数据清空
	 */
	@Override
	public void close() {
		super.close();
        for (String key : daos.keySet())
        {
            Dao dao = daos.get(key);
            dao = null;
        }
	}

    /**
     * 建立表机构
     */
    private void onCreate() {
		try {
            TableUtils.createTable(connectionSource, SpeedData.class);
            TableUtils.createTable(connectionSource, DateData.class);
            TableUtils.createTable(connectionSource, UserBeans.class);
            TableUtils.createTable(connectionSource, SetingBeans.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 更新前删除要更新的表
	 */
	private void onUpgrade(int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, UserModel.class, true);
            TableUtils.dropTable(connectionSource, SpeedData.class, true);
            TableUtils.dropTable(connectionSource, DateData.class, true);
            TableUtils.dropTable(connectionSource, UserBeans.class, true);
            TableUtils.dropTable(connectionSource, SetingBeans.class, true);
			// after we drop the old databases, we create the new ones
			onCreate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

    /**
     *
     * @param clazz 对象类
     * @param <D> Dao
     * @param <T> 对象类
     * @return 返回Dao层对象
     * @throws java.sql.SQLException
     */
    public synchronized  <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
		Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
		if (dao == null) {
			// 使用反射将数据表反射成对象
			DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
			if (tableConfig == null) {
				dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
			} else {
				dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, tableConfig);
			}
		}

		@SuppressWarnings("unchecked")
		D castDao = (D) dao;
		return castDao;
	}
}
