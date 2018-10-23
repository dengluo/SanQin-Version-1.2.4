package com.pbids.sanqin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;
import com.pbids.sanqin.model.entity.FriendGroupDbDao;
import com.pbids.sanqin.model.entity.NewsArticleDao;
import com.pbids.sanqin.model.entity.SystemMessageDao;
import com.pbids.sanqin.model.entity.UserInfoDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

/**
 * Created by liang on 2018/3/7.
 */

public class DBOpenHelper extends DaoMaster.DevOpenHelper {

	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		//super.onUpgrade(db, oldVersion, newVersion); 调用这个会删除旧数据 生成新数据
		if (oldVersion < newVersion) {
			//保留数据数据库
			//更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
			DBMigrationHelper.migrate(sqLiteDatabase,
					NewsArticleDao.class,
					UserInfoDao.class ,
					FriendGroupDbDao.class,
					SystemMessageDao.class);
		}
	}

	/**
	 * 取得DaoMaster
	 *
	 * @param context
	 * @return
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,
					DBManager.dbName, null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	/**
	 * 取得DaoSession
	 *
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
}
