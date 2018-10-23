package com.pbids.sanqin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.model.entity.FriendGroupDbDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:数据管理基类
 * @remark 备注:包含了增删改查
 * @see
 */

public abstract class DbManagerBase<T extends AbstractDao,P> {

	protected abstract DbDao<T> getDao(boolean isWrite) ;

	protected DbDao getDao() {
		return (getDao(false));
	}

	//查询所有数据
	public List<P> queryAll(){
		DbDao<T>  dbDao = getDao();
		QueryBuilder<P> builder = dbDao.dao.queryBuilder();
		List<P> glist = builder.list();
		dbDao.sqLiteDatabase.close();
		return glist;
	}

	//插入数据
	public void insert( P item){
		DbDao<T>  dbDao = getDao(true);
		try {
			dbDao.dao.insert(item);
		}catch (SQLiteException e){
			System.out.print("insert:"+dbDao.dao.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
		dbDao.sqLiteDatabase.close();
	}

	//清除消息
	public void clear(){
		DbDao<T>  dbDao = getDao(true);
		dbDao.dao.deleteAll();
		dbDao.sqLiteDatabase.close();
	}

    //删除数据
    public void del(P item){
		DbDao<T>  dbDao = getDao(true);
		dbDao.dao.delete(item);
		dbDao.sqLiteDatabase.close();
    }

    //按主键删除
	public void delById( long id){
		DbDao<T>  dbDao = getDao(true);
		dbDao.dao.deleteByKey(id);
		dbDao.sqLiteDatabase.close();
	}

    //更新数据
    public void update(P item){
		DbDao<T>  dbDao = getDao(true);
		dbDao.dao.update(item);
		dbDao.sqLiteDatabase.close();
    }

}
