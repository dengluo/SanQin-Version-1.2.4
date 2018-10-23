package com.pbids.sanqin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:数据库manager对象
 * @remark 备注:使用的greengao
 * @see
 */

public class DBManager {
    public final static String dbName = "sanqin_db";
    private static DBManager mInstance;
    //private DaoMaster.DevOpenHelper openHelper;
    private static DBOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        //openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        //openHelper = new DBHelper(context, dbName, null);
        getDBHelper(context, dbName, null);
    }

    public static DBOpenHelper getDBHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory) {
        if (openHelper == null) {
            openHelper = new DBOpenHelper(context, dbName, factory);
        }
        return openHelper;
    }
	public static DBOpenHelper getDBHelper( ) {
		return openHelper;
	}

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
	public SQLiteDatabase getReadableDatabase() {
        getDBHelper(context, dbName, null);
//        if (openHelper == null) {
//            //openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
//            openHelper = new DBHelper(context, dbName, null);
//        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    protected SQLiteDatabase getWritableDatabase() {
        getDBHelper(context, dbName, null);
//        if (openHelper == null) {
//            //openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
//            openHelper = getDBHelper(context, dbName, null);
//        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }



}

