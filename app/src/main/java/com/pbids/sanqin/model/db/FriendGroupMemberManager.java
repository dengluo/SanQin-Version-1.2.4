package com.pbids.sanqin.model.db;

import android.database.sqlite.SQLiteDatabase;

import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;
import com.pbids.sanqin.model.entity.FriendGroupMemberDb;
import com.pbids.sanqin.model.entity.FriendGroupMemberDbDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * @author caiguoliang
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:分组成员管理
 * @remark 备注:包含了增删改查
 * @see
 */

public class FriendGroupMemberManager extends DbManagerBase<FriendGroupMemberDbDao,FriendGroupMemberDb> {

    @Override
	protected DbDao<FriendGroupMemberDbDao> getDao(boolean isWrite) {
		SQLiteDatabase sqLiteDatabase;
		if (isWrite) {
			sqLiteDatabase = DBManager.getInstance(MyApplication.getApplication()).getWritableDatabase();
		} else {
			sqLiteDatabase = DBManager.getInstance(MyApplication.getApplication()).getReadableDatabase();
		}
		DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
		DaoSession daoSession = daoMaster.newSession();
        FriendGroupMemberDbDao dao = daoSession.getFriendGroupMemberDbDao();
		return new DbDao(sqLiteDatabase, daoMaster, daoSession, dao);
	}

    public static FriendGroupMemberManager newInstance() {
        FriendGroupMemberManager self = new FriendGroupMemberManager();
        return self;
    }

    //查询 数据
    public List<FriendGroupMemberDb> queryByGroupId( String groupId){
		DbDao<FriendGroupMemberDbDao>  dbDao = getDao();
        QueryBuilder<FriendGroupMemberDb> builder = dbDao.dao.queryBuilder()
				.where(FriendGroupMemberDbDao.Properties.Gid.eq(groupId))
                .orderAsc(FriendGroupMemberDbDao.Properties.Id);
		List<FriendGroupMemberDb> glist = builder.list();
		dbDao.sqLiteDatabase.close();
		return glist;
    }

    //删除成员
    public void delMember( String groupId , String account ){
        DbDao<FriendGroupMemberDbDao>  dbDao = getDao(true);
        String sql = String.format("DELETE from %s WHERE %s='%s' and %s='%s' ",
                FriendGroupMemberDbDao.TABLENAME,
                FriendGroupMemberDbDao.Properties.Gid.columnName,groupId,
                FriendGroupMemberDbDao.Properties.Account.columnName,account);
        dbDao.dao.getDatabase().execSQL(sql);
        dbDao.sqLiteDatabase.close();
    }

    //删除一个级的所有成员
    public void delGroupMember( String groupId){
        DbDao<FriendGroupMemberDbDao>  dbDao = getDao(true);
        String sql = String.format("DELETE from %s WHERE %s='%s' ",
                FriendGroupMemberDbDao.TABLENAME,
                FriendGroupMemberDbDao.Properties.Gid.columnName,groupId);
        dbDao.dao.getDatabase().execSQL(sql);
        dbDao.sqLiteDatabase.close();
    }

    //添加成员
    public FriendGroupMemberDb addMember( String groupId , String account,String alias ){
        FriendGroupMemberDb one = new FriendGroupMemberDb();
        one.setGid(groupId);
        one.setAccount(account);
        one.setAlias(alias);
        insert(one);
        return one;
    }

}
