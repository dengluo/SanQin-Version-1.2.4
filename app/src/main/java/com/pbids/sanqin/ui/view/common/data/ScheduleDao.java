package com.pbids.sanqin.ui.view.common.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.NewsArticleManager;
import com.pbids.sanqin.ui.view.common.bean.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class ScheduleDao {

    private JeekSQLiteHelper mHelper;
    private Context mContext;

    private ScheduleDao(Context context) {
        mContext = context;
        mHelper = new JeekSQLiteHelper(context);
    }

    public static ScheduleDao getInstance(Context context) {
        return new ScheduleDao(context);
    }

    public int addSchedule(Schedule schedule) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JeekDBConfig.SCHEDULE_TITLE, schedule.getTitle());
        values.put(JeekDBConfig.SCHEDULE_COLOR, schedule.getColor());
        values.put(JeekDBConfig.SCHEDULE_DESC, schedule.getDesc());
        values.put(JeekDBConfig.SCHEDULE_STATE, schedule.getState());
        values.put(JeekDBConfig.SCHEDULE_LOCATION, schedule.getLocation());
        values.put(JeekDBConfig.SCHEDULE_TIME, schedule.getTime());
        values.put(JeekDBConfig.SCHEDULE_YEAR, schedule.getYear());
        values.put(JeekDBConfig.SCHEDULE_MONTH, schedule.getMonth());
        values.put(JeekDBConfig.SCHEDULE_DAY, schedule.getDay());
        values.put(JeekDBConfig.SCHEDULE_EVENT_SET_ID, schedule.getEventSetId());
        long row = db.insert(JeekDBConfig.SCHEDULE_TABLE_NAME, null, values);
        db.close();
        return row > 0 ? getLastScheduleId() : 0;
    }

    private int getLastScheduleId() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(JeekDBConfig.SCHEDULE_TABLE_NAME, null, null, null, null, null, null, null);
        int id = 0;
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_ID));
        }
        cursor.close();
        db.close();
        mHelper.close();
        return id;
    }

    public List<Schedule> getScheduleByDate(int year, int month, int day) {
        List<Schedule> schedules = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(JeekDBConfig.SCHEDULE_TABLE_NAME, null,
                String.format("%s=? and %s=? and %s=?", JeekDBConfig.SCHEDULE_YEAR,
                        JeekDBConfig.SCHEDULE_MONTH, JeekDBConfig.SCHEDULE_DAY), new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)}, null, null, null);
        Schedule schedule;
        while (cursor.moveToNext()) {
            schedule = new Schedule();
            schedule.setId(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_ID)));
            schedule.setColor(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_COLOR)));
            schedule.setTitle(cursor.getString(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_TITLE)));
            schedule.setLocation(cursor.getString(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_LOCATION)));
            schedule.setDesc(cursor.getString(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_DESC)));
            schedule.setState(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_STATE)));
            schedule.setYear(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_YEAR)));
            schedule.setMonth(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_MONTH)));
            schedule.setDay(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_DAY)));
            schedule.setTime(cursor.getLong(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_TIME)));
            schedule.setEventSetId(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_EVENT_SET_ID)));
            schedules.add(schedule);
        }
        cursor.close();
        db.close();
        mHelper.close();
        return schedules;
    }

    public List<Integer> getTaskHintByMonth(int year, int month) {
//        List<Integer> taskHint = new ArrayList<>();
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(JeekDBConfig.SCHEDULE_TABLE_NAME, new String[]{JeekDBConfig.SCHEDULE_DAY},
//                String.format("%s=? and %s=?", JeekDBConfig.SCHEDULE_YEAR,
//                        JeekDBConfig.SCHEDULE_MONTH), new String[]{String.valueOf(year), String.valueOf(month)}, null, null, null);
//        while (cursor.moveToNext()) {
//            taskHint.insert(cursor.getInt(0));
//        }
//        cursor.close();
//        db.close();
//        mHelper.close();
//        List<Integer> taskHint = new ArrayList<>();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
//        String newMonth = (month+1)+"";
//        if(newMonth.length()<2){
//            newMonth = "0"+newMonth;
//        }
//        String historyTime = ""+year+""+newMonth;
//        Date date=null;
//        Calendar calendar = new GregorianCalendar();
//        try {
//            date = simpleDateFormat.parse(historyTime);
//            calendar.setTime(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if(date!=null){
//
////            SQLiteDatabase db = mHelper.getReadableDatabase();
////            Cursor cursor = db.query("NewsArticle", new String[]{"browseTime"},
////                    String.format("%s>=? and %s<?", "browseTime", "browseTime")
////                    , new String[]{String.valueOf(date.getTime())
////                            ,String.valueOf(date.getTime()+(60 * 1000*60*24*calendar.getActualMaximum(Calendar.DAY_OF_MONTH)))}, null, null, null);
////
////            Log.i("wzh","calendar.getActualMaximum(Calendar.DAY_OF_MONTH): "+calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
////            while (cursor.moveToNext()) {
////                Log.i("wzh","cursor: "+cursor.getInt(0));
////                taskHint.insert(cursor.getInt(0));
////            }
////            cursor.close();
////            db.close();
////            mHelper.close();
//        }
        Log.i("wzh","getTaskHintByMonth");
        return NewsArticleManager.queryNewsArticleByMonth(mContext,year,month);
    }

    public List<Integer> getTaskHintByWeek(int firstYear, int firstMonth, int firstDay, int endYear, int endMonth, int endDay) {
        List<Integer> taskHint = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor1 = db.query(JeekDBConfig.SCHEDULE_TABLE_NAME, new String[]{JeekDBConfig.SCHEDULE_DAY},
                String.format("%s=? and %s=? and %s>=?", JeekDBConfig.SCHEDULE_YEAR, JeekDBConfig.SCHEDULE_MONTH, JeekDBConfig.SCHEDULE_DAY),
                new String[]{String.valueOf(firstYear), String.valueOf(firstMonth), String.valueOf(firstDay)}, null, null, null);
        while (cursor1.moveToNext()) {
            taskHint.add(cursor1.getInt(0));
        }
        cursor1.close();
        Cursor cursor2 = db.query(JeekDBConfig.SCHEDULE_TABLE_NAME, new String[]{JeekDBConfig.SCHEDULE_DAY},
                String.format("%s=? and %s=? and %s<=?", JeekDBConfig.SCHEDULE_YEAR, JeekDBConfig.SCHEDULE_MONTH, JeekDBConfig.SCHEDULE_DAY),
                new String[]{String.valueOf(endYear), String.valueOf(endMonth), String.valueOf(endDay)}, null, null, null);
        while (cursor2.moveToNext()) {
            taskHint.add(cursor2.getInt(0));
        }
        cursor2.close();
        db.close();
        mHelper.close();
        return taskHint;
    }

    public boolean removeSchedule(long id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int row = db.delete(JeekDBConfig.SCHEDULE_TABLE_NAME, String.format("%s=?", JeekDBConfig.SCHEDULE_ID), new String[]{String.valueOf(id)});
        db.close();
        mHelper.close();
        return row != 0;
    }

    public void removeScheduleByEventSetId(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(JeekDBConfig.SCHEDULE_TABLE_NAME, String.format("%s=?", JeekDBConfig.SCHEDULE_EVENT_SET_ID), new String[]{String.valueOf(id)});
        db.close();
        mHelper.close();
    }

    public boolean updateSchedule(Schedule schedule) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(JeekDBConfig.SCHEDULE_TITLE, schedule.getTitle());
        values.put(JeekDBConfig.SCHEDULE_COLOR, schedule.getColor());
        values.put(JeekDBConfig.SCHEDULE_DESC, schedule.getDesc());
        values.put(JeekDBConfig.SCHEDULE_STATE, schedule.getState());
        values.put(JeekDBConfig.SCHEDULE_LOCATION, schedule.getLocation());
        values.put(JeekDBConfig.SCHEDULE_YEAR, schedule.getYear());
        values.put(JeekDBConfig.SCHEDULE_MONTH, schedule.getMonth());
        values.put(JeekDBConfig.SCHEDULE_TIME, schedule.getTime());
        values.put(JeekDBConfig.SCHEDULE_DAY, schedule.getDay());
        values.put(JeekDBConfig.SCHEDULE_EVENT_SET_ID, schedule.getEventSetId());
        int row = db.update(JeekDBConfig.SCHEDULE_TABLE_NAME, values, String.format("%s=?", JeekDBConfig.SCHEDULE_ID), new String[]{String.valueOf(schedule.getId())});
        db.close();
        mHelper.close();
        return row > 0;
    }

    public List<Schedule> getScheduleByEventSetId(int id) {
        List<Schedule> schedules = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(JeekDBConfig.SCHEDULE_TABLE_NAME, null,
                String.format("%s=?", JeekDBConfig.SCHEDULE_EVENT_SET_ID), new String[]{String.valueOf(id)}, null, null, null);
        Schedule schedule;
        while (cursor.moveToNext()) {
            schedule = new Schedule();
            schedule.setId(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_ID)));
            schedule.setColor(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_COLOR)));
            schedule.setTitle(cursor.getString(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_TITLE)));
            schedule.setDesc(cursor.getString(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_DESC)));
            schedule.setLocation(cursor.getString(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_LOCATION)));
            schedule.setState(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_STATE)));
            schedule.setYear(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_YEAR)));
            schedule.setMonth(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_MONTH)));
            schedule.setDay(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_DAY)));
            schedule.setTime(cursor.getLong(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_TIME)));
            schedule.setEventSetId(cursor.getInt(cursor.getColumnIndex(JeekDBConfig.SCHEDULE_EVENT_SET_ID)));
            schedules.add(schedule);
        }
        cursor.close();
        db.close();
        mHelper.close();
        return schedules;
    }

    public static long getCurrentDayZeroTime(long currentTime){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String s = simpleDateFormat.format(currentTime);

            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strZeroClok = s+" 00:00:00";
            Date date = simpleDateFormat1.parse(strZeroClok);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
