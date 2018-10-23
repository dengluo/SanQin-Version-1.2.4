package com.pbids.sanqin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.model.entity.FriendGroupDbDao;
import com.pbids.sanqin.model.entity.SystemMessage;
import com.pbids.sanqin.model.entity.SystemMessageDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:系统消息数据操作类
 * @remark 备注:包含了增删改查
 * @see
 */

public class SystemMessageManager extends DbManagerBase<SystemMessageDao,SystemMessage> {

	//标记 1 为系统
	public static final int TYPE_SYSTEM = 1;
	//标记 2 为公众号
	public static final int TYPE_TOPIC = 2;
	//标记  1 ：系统消息 2：支付消息 3：打赏消息 100：其它
	public static final int MSG_TYPE_SYSTEM = 1;
	public static final int MSG_TYPE_PAY = 2;


    @Override
    protected DbDao<SystemMessageDao> getDao(boolean isWrite) {
        SQLiteDatabase sqLiteDatabase;
        if (isWrite) {
            sqLiteDatabase = DBManager.getInstance(MyApplication.getApplication()).getWritableDatabase();
        } else {
            sqLiteDatabase = DBManager.getInstance(MyApplication.getApplication()).getReadableDatabase();
        }
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        SystemMessageDao dao = daoSession.getSystemMessageDao();
        return new DbDao(sqLiteDatabase, daoMaster, daoSession, dao);
    }

    public static SystemMessageManager instance() {
        SystemMessageManager self = new SystemMessageManager();
        return self;
    }

/*	//插入数据
	public static void add(Context context,SystemMessage message){
		SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
		DaoSession daoSession = daoMaster.newSession();
		SystemMessageDao messageDao = daoSession.getSystemMessageDao();
		try {
			messageDao.insert(message);
		}catch (SQLiteException e){
			System.out.print("insert:"+message.toString());
		}catch (Exception e){

		}
		sqLiteDatabase.close();
	}*/

/*
	//清除消息
	public static void clear(Context context){
		SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
		DaoSession daoSession = daoMaster.newSession();
		SystemMessageDao messageDao = daoSession.getSystemMessageDao();
		messageDao.deleteAll();
		sqLiteDatabase.close();
	}
*/
    //删除数据
    public static void del(Context context,SystemMessage message){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
		DaoSession daoSession = daoMaster.newSession();
		SystemMessageDao messageDao = daoSession.getSystemMessageDao();
		messageDao.delete(message);
        sqLiteDatabase.close();
    }

    //查询所有数据
    public static List<SystemMessage> query(Context context,int type){
		if(MyApplication.getUserInfo()==null){
			return new ArrayList<>();
		}
		Long uid = MyApplication.getUserInfo().getUserId();
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
		SystemMessageDao messageDao = daoSession.getSystemMessageDao();
        QueryBuilder<SystemMessage> builder = messageDao.queryBuilder()
				.where(SystemMessageDao.Properties.Type.eq(type))
				.where(SystemMessageDao.Properties.Uid.eq(uid))
				.orderDesc(SystemMessageDao.Properties.Time) ;
        return builder.list();
    }

	//查询所有数据 读取状态
	public static List<SystemMessage> query(Context context,int type,boolean isread){
		if(MyApplication.getUserInfo()==null){
			return new ArrayList<>();
		}
		Long uid = MyApplication.getUserInfo().getUserId();
		SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getReadableDatabase();
		DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
		DaoSession daoSession = daoMaster.newSession();
		SystemMessageDao messageDao = daoSession.getSystemMessageDao();
		QueryBuilder<SystemMessage> builder = messageDao.queryBuilder()
				.where(SystemMessageDao.Properties.Type.eq(type))
				.where(SystemMessageDao.Properties.Isread.eq(false))
				.where(SystemMessageDao.Properties.Uid.eq(uid))
				.orderDesc(SystemMessageDao.Properties.Time) ;
		return builder.list();
	}

/*    //更新数据
    public static void update(Context context,SystemMessage message){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
		SystemMessageDao messageDao = daoSession.getSystemMessageDao();
		messageDao.update(message);
        sqLiteDatabase.close();
    }*/

}
