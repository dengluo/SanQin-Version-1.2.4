package com.pbids.sanqin.model.db;

import android.database.sqlite.SQLiteDatabase;

import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;

public class DbDao<T> {
    public SQLiteDatabase sqLiteDatabase ;
    public DaoSession daoSession;
    public DaoMaster daoMaster;
    public T dao;

    public DbDao( SQLiteDatabase sqldb,DaoMaster daoMaster,DaoSession dbsession,T dao ){
        this.sqLiteDatabase = sqldb;
        this.daoMaster = daoMaster;
        this.daoSession = dbsession;
        this.dao = dao;
    }

}
