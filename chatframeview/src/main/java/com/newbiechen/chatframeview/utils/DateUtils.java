package com.newbiechen.chatframeview.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by PC on 2017/1/12.
 * 日期转换类
 */

public final class DateUtils {

    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORAMT_DATE = "yyyy-MM-dd";

    public static Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }

    public static Calendar getCurrentCal(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        return calendar;
    }

    public static String getTime(){
        String timeStr = getDateStr(
                System.currentTimeMillis(),FORMAT_TIME);
        return timeStr;
    }
    public static String getDayOfWeek(){
        Calendar calender = getCurrentCal();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        if (day == 1){
            day = 7;
        }
        else {
            day = day - 1;
        }
        return day+"";
    }

    public static String getDate(){
        Calendar calender = getCurrentCal();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH) + 1;
        int day = calender.get(Calendar.DATE);
        return year+"-"+month+"-"+day;
    }

    public static String getDateStr(String format){
        return getDateStr(System.currentTimeMillis(),format);
    }

    public static String getDateStr(long time,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time));
    }
}
