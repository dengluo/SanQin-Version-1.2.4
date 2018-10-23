package com.pbids.sanqin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by pbids903 on 2017/12/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:40
 * @desscribe 类描述:时间工具类
 * @remark 备注:
 * @see
 */
public class TimeUtil {
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    public final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @return
     */
    public static String getTimeFormatText(long time) {
        if (time == 0) {
            return null;
        }
        long diff = new Date().getTime() - time;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }
//    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
    public static String getGenealogyTimeFormat(long time){
        return new SimpleDateFormat("yyyy年").format(new Date(time));
    }

    public static String getGiftTimeFormat(long time){
        return new SimpleDateFormat("yyyy.MM.dd").format(new Date(time));
    }

    public static String getMessageTimeFormat(long time){
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(time));
    }

    public static boolean isSameDay(Date date, Date sameDate) {
        if (null == date || null == sameDate) {
            return false;
        }
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(sameDate);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
                && nowCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
                && nowCalendar.get(Calendar.DATE) == dateCalendar.get(Calendar.DATE)) {
            return true;
        }
        // if (date.getYear() == sameDate.getYear() && date.getMonth() == sameDate.getMonth()
        // && date.getDate() == sameDate.getDate()) {
        // return true;
        // }
        return false;
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

    /**
     * 判断是否为今天(效率比较高)
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static String getTodayForSign(){
        Date todate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(todate);
    }

    //取一个星期的天数 开始的天数
    public static List<String> getAWeekDays(String time,int prevDay){
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM.dd");
        /*long today= 0;
        try {
            today= sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(today != 0){
            for(int i=0;i<7;i++){
                list.insert(sdf1.format(new Date(today+(day*(i-startDay)))));
            }
        }*/
        //使用Calendar的add(int field, int amount)方法
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DAY_OF_MONTH, prevDay); //转到前几天
        for(int i=0;i<7;i++){
            ca.add(Calendar.DAY_OF_MONTH, 1); //加1天
            list.add(sdf1.format( ca.getTime() ));
        }
        //Date lastMonth = ca.getTime(); //结果
        return list;
    }

    public static SimpleDateFormat getDateFormat() {
//        if (null == DateLocal.get()) {
//            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
//        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }

}
