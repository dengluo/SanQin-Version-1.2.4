package com.pbids.sanqin.model.db;

import android.database.sqlite.SQLiteDatabase;

import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.model.entity.FriendGroupDbDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * @author caiguoliang
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:系统消息数据操作类
 * @remark 备注:包含了增删改查
 * @see
 */

public class FriendGroupManager extends DbManagerBase<FriendGroupDbDao,FriendGroupDb> {


	@Override
	protected DbDao<FriendGroupDbDao> getDao(boolean isWrite) {
		SQLiteDatabase sqLiteDatabase;
		if (isWrite) {
			sqLiteDatabase = DBManager.getInstance(MyApplication.getApplication()).getWritableDatabase();
		} else {
			sqLiteDatabase = DBManager.getInstance(MyApplication.getApplication()).getReadableDatabase();
		}
		DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
		DaoSession daoSession = daoMaster.newSession();
		FriendGroupDbDao dao = daoSession.getFriendGroupDbDao();
		return new DbDao(sqLiteDatabase, daoMaster, daoSession, dao);
	}

    public static FriendGroupManager newInstance() {
        FriendGroupManager self = new FriendGroupManager();
        return self;
    }


	//分类查找
	public List<FriendGroupDb> queryByType(int type) {
		DbDao<FriendGroupDbDao> dbDao = getDao(true);
		QueryBuilder<FriendGroupDb> builder = dbDao.dao.queryBuilder()
				.where(FriendGroupDbDao.Properties.Type.eq(type))
				.where(FriendGroupDbDao.Properties.Pid.eq(""));
		List<FriendGroupDb> glist = builder.list();
        dbDao.sqLiteDatabase.close();
		return glist;
	}

    //查询 数据
    public FriendGroupDb queryByGroupName(String name){
        DbDao<FriendGroupDbDao>  dbDao = getDao();
        QueryBuilder<FriendGroupDb> builder = dbDao.dao.queryBuilder()
                .where(FriendGroupDbDao.Properties.GroupName.eq(name))
                .where(FriendGroupDbDao.Properties.Pid.eq(""))
                .limit(1);
        List<FriendGroupDb> glist = builder.list();
        dbDao.sqLiteDatabase.close();
        if(glist!=null&&glist.size()>0){
            return glist.get(0);
        }
        return null;
    }

	//组id查找
	public FriendGroupDb queryByGroupId(String gid) {
		DbDao<FriendGroupDbDao> dbDao = getDao(true);
		QueryBuilder<FriendGroupDb> builder = dbDao.dao.queryBuilder()
				.where(FriendGroupDbDao.Properties.GroupId.eq(gid))
                .limit(1);
		List<FriendGroupDb> glist = builder.list();
        dbDao.sqLiteDatabase.close();
		if(glist!=null&&glist.size()>0){
			return glist.get(0);
		}
		return null;
	}

	public void updateGroupName(String groupId,String name){
		DbDao<FriendGroupDbDao> dbDao = getDao(true);
		String sql = String.format("UPDATE %s set %s='%s' WHERE %s='%s' ",
				FriendGroupDbDao.TABLENAME,
				FriendGroupDbDao.Properties.GroupName.columnName,name,
				FriendGroupDbDao.Properties.GroupId.columnName,groupId);
		dbDao.dao.getDatabase().execSQL(sql);
		dbDao.sqLiteDatabase.close();
	}

	// 取二级分组
    public List<FriendGroupDb> queryListByPId(String pid) {
        DbDao<FriendGroupDbDao> dbDao = getDao(true);
        QueryBuilder<FriendGroupDb> builder = dbDao.dao.queryBuilder()
                .where(FriendGroupDbDao.Properties.Pid.eq(pid))
                .orderAsc(FriendGroupDbDao.Properties.Id);
        List<FriendGroupDb> glist = builder.list();
        dbDao.sqLiteDatabase.close();
        return glist;
    }

}
