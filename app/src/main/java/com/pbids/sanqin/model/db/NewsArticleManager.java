package com.pbids.sanqin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pbids.sanqin.model.entity.DaoMaster;
import com.pbids.sanqin.model.entity.DaoSession;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsArticleDao;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.UserInfoDao;
import com.pbids.sanqin.ui.view.calendar.CalendarUtils;
import com.pbids.sanqin.utils.TimeUtil;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:38
 * @desscribe 类描述:文章数据库管理
 * @remark 备注:包含了增删改查
 * @see
 */
public class NewsArticleManager {
    //插入一条文章数据到数据库，当插入的_id和原文章数据相同时，进行更新操作
    public static void insertNewsArticle(Context context,NewsArticle newsArticle){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        NewsArticleDao newsArticleDao = daoSession.getNewsArticleDao();
        newsArticleDao.insertOrReplace(newsArticle);
//        newsArticleDao.insert(newsArticle);
        sqLiteDatabase.close();
    }

    //删除一条文章数据
    public static void deleteNewsArticle(Context context,NewsArticle newsArticle){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        NewsArticleDao newsArticleDao = daoSession.getNewsArticleDao();
        newsArticleDao.delete(newsArticle);
        sqLiteDatabase.close();
    }

    //删除一个文章集合
    public static void deleteNewsArticles(Context context,List<NewsArticle> newsArticles){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        NewsArticleDao newsArticleDao = daoSession.getNewsArticleDao();
        newsArticleDao.deleteInTx(newsArticles);
        sqLiteDatabase.close();
    }

    //查询一个文章集合
    public static List<NewsArticle> queryNewsArticle(Context context,long time){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        NewsArticleDao newsArticleDao = daoSession.getNewsArticleDao();
        QueryBuilder<NewsArticle> builder = newsArticleDao.queryBuilder();
//        Log.i("wzh","time :"+time);
//        Log.i("wzh","TimeUtil.getCurrentDayZeroTime(time) :"+TimeUtil.getCurrentDayZeroTime(time));
//        Log.i("wzh","(TimeUtil.getCurrentDayZeroTime(time)+TimeUtil.day)) :"+(TimeUtil.getCurrentDayZeroTime(time)+TimeUtil.day));
        List<NewsArticle> newsArticles1 = builder.where(
                NewsArticleDao.Properties.BrowseTime.ge(TimeUtil.getCurrentDayZeroTime(time)),
                NewsArticleDao.Properties.BrowseTime.lt((TimeUtil.getCurrentDayZeroTime(time)+TimeUtil.day))).list();

//        Log.i("wzh","list.(0):"+newsArticles.get(0).getBrowseTime());
        sqLiteDatabase.close();
        return newsArticles1;
    }

    //通过年月来查询一个文章集合
    public static List<Integer> queryNewsArticleByMonth(Context context,int year,int month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String newMonth = (month+1)+"";
        String nextMonth = (month+2)+"";
        String mothDayStr = "";
        if(newMonth.length()<2){
            newMonth = "0"+newMonth;
            nextMonth = "0"+nextMonth;
        }
        int mothDay = CalendarUtils.getMonthDays(year,(month));
        if(mothDay<10){
            mothDayStr = "0"+mothDay;
        }else{
            mothDayStr = ""+mothDay;
        }
        String startTime = ""+year+""+newMonth+"01000000";
        String endTime = ""+year+""+newMonth+mothDayStr+"235959";
//        String endTime = ""+year+""+nextMonth+"00000000";
        Log.i("wzw","startTime: "+startTime);
        Log.i("wzw","endTime: "+endTime);
        Date dateStart=null;
        Date dateEnd=null;
        Calendar calendar = new GregorianCalendar();
        try {
            dateStart = simpleDateFormat.parse(startTime);
            Log.i("wzw","dateStart.getTime(): "+dateStart.getTime());
            calendar.setTime(dateStart);
            dateEnd = simpleDateFormat.parse(endTime);
            Log.i("wzw","dateEnd.getTime(): "+dateEnd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        TimeUtil
        List<Integer> taskHint = new ArrayList<>();
        if(dateStart!=null && dateEnd!=null){
            SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getReadableDatabase();
            DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
            DaoSession daoSession = daoMaster.newSession();
            NewsArticleDao newsArticleDao = daoSession.getNewsArticleDao();
            QueryBuilder<NewsArticle> builder = newsArticleDao.queryBuilder();

            List<NewsArticle> newsArticles = builder.where(
                    NewsArticleDao.Properties.BrowseTime.ge(dateStart.getTime()),
                    NewsArticleDao.Properties.BrowseTime.lt(dateEnd.getTime())).list();
            Log.i("wzw","newsArticles.size(): "+newsArticles.size());
            for(int i=0;i<newsArticles.size();i++){
                long browseTime = newsArticles.get(i).getBrowseTime();
                SimpleDateFormat daySimpleDateFormat = new SimpleDateFormat("dd");
                String day = daySimpleDateFormat.format(browseTime);
                if(!taskHint.contains(Integer.valueOf(day))){
                    taskHint.add(Integer.valueOf(day));
                }
                Log.i("wzw","browseTime: "+browseTime);
                Log.i("wzw","day: "+Integer.valueOf(day));
            }
            if(taskHint.size()>0){
                return taskHint;
            }
        }
        return taskHint;
    }

    //更新一条文章数据
    public static void updateNewsArticle(Context context,NewsArticle newsArticle){
        SQLiteDatabase sqLiteDatabase = DBManager.getInstance(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        NewsArticleDao newsArticleDao = daoSession.getNewsArticleDao();
        newsArticleDao.update(newsArticle);
        sqLiteDatabase.close();
    }

}
