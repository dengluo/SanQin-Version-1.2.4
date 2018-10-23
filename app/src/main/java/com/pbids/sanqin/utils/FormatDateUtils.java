package com.pbids.sanqin.utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDateUtils {
    /**
     //        一小时以内显示几分钟前；
     //        24小时内显示几小时前；
     //        超过24小时三天内（含三天）显示几天前；
     //        3天以上显示具体日期，到天即可。
     //        超过一年显示年份，年内不显示年份
     * @param
     * @return
     */
    public static String formatDate(String para){

        //现在的时间
        Integer nowYear =  Integer.parseInt(DateTime.now().toString("yyyy"));

        //时间格式化
        //org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
        org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

        //传进来的时间解析
        DateTime paraDate = DateTime.parse(para,format);//年月日 时分秒
        Integer paraYear = Integer.parseInt(paraDate.toString("yyyy"));//年
        String paraDay = paraDate.toString("yyyy/MM/dd");


        DateTime nowDate = new DateTime().now();   //当前
        Interval hours1 = new Interval(nowDate.minusHours(1), nowDate);
        Interval hours24 = new Interval(nowDate.minusHours(24), nowDate);
        Interval day = new Interval(nowDate.minusDays(4), nowDate);
        Interval years = new Interval(nowDate.minusYears(1), nowDate);


        if (hours1.contains(paraDate)) { //一小时以内显示几分钟前；
            Interval minute  = new  Interval(paraDate, nowDate);
            int res = minute.toPeriod().getMinutes();
            if(res == 0 || res == 1 ){
                return "刚刚";
            }
            return res+"分钟前";
            // return "几分钟前";
        } else if (hours24.contains(paraDate)) { //24小时内显示几小时前；
            Interval hours  = new  Interval(paraDate, nowDate);
            int res = hours.toPeriod().getHours();
            return res+"小时前";
            //return "几小时前";
        } else if (day.contains(paraDate)) { //超过24小时三天内（含三天）显示几天前；
            Interval Day  = new  Interval(paraDate, nowDate);
            int res = Day.toPeriod().getDays();
            return res+"天前";
        }else if(paraYear < nowYear ){
            return paraDay;
        }else {
            return paraDate.toString("yyyy/MM/dd");
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = s;// new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
