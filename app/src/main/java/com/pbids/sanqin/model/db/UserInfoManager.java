package com.pbids.sanqin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.UserInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:用户数据操作类
 * @remark 备注:包含了增删改查
 * @see
 */

public class UserInfoManager {
    //插入用户数据
    public static void insertUserInfo(Context context,UserInfo userInfo){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.insert(userInfo);
        sqLiteDatabase.close();
    }

    //删除用户数据
    public static void deleteUserInfo(Context context,UserInfo userInfo){
        if (userInfo == null) {
            return;
        }
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.delete(userInfo);
        sqLiteDatabase.close();
    }

    //查询用户数据
    public static UserInfo queryUserInfo(Context context){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        QueryBuilder<UserInfo> builder = userInfoDao.queryBuilder();
        UserInfo userInfo = null;
        if(builder.list().size()!=0){
            userInfo = builder.list().get(0);
        }
        sqLiteDatabase.close();
        return userInfo;
    }

    //更新用户数据
    public static void updateUserInfo(Context context,UserInfo userInfo){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        userInfoDao.update(userInfo);
        sqLiteDatabase.close();
    }

}
