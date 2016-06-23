package com.moon.myreadapp.util;

import android.content.Context;
import android.text.format.Time;

import com.moon.appframework.common.log.XLog;
import com.moon.myreadapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by moon on 15/12/4.
 */
public class Conver {

    private static final Calendar CALENDAR = Calendar.getInstance();

    //把日期转为字符串
    public static String ConverToString(Date date)
    {
        if (date == null){
            return "";
        }
        /*
        XLog.d("execute !" + date.getTime());
        DateFormat df = new SimpleDateFormat("MM/dd/HH:mm");
        XLog.d("execute !" + df.format(date));
        return df.format(date);
        */
        return formatDateTime(date);
    }
    public static String ConverToString(Date date,String style)
    {
        if (date == null){
            return "";
        }
        XLog.d("execute !" + date.getTime());
        DateFormat df = new SimpleDateFormat(style);
        XLog.d("execute !" + df.format(date));
        return df.format(date);
    }


    //把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }


    public static String formatDateTime(Date date) {
        StringBuffer text = new StringBuffer("");
        long dateTime = date.getTime();
        if (isSameDay(dateTime)) {
            Calendar calendar = GregorianCalendar.getInstance();
            if (inOneMinute(dateTime, calendar.getTimeInMillis())) {
                return BuiltConfig.getString(R.string.time_monment_ago);
            } else if (inOneHour(dateTime, calendar.getTimeInMillis())) {
                return BuiltConfig.getString(R.string.time_monment_ago_by_minute,Math.abs(dateTime - calendar.getTimeInMillis()) / 60000);
            } else {
                calendar.setTime(date);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                if (hourOfDay > 17) {
                    text.append(BuiltConfig.getString(R.string.time_night));
                } else if (hourOfDay >= 0 && hourOfDay <= 6) {
                    text.append(BuiltConfig.getString(R.string.time_midnight));
                } else if (hourOfDay > 11 && hourOfDay <= 17) {
                    text.append(BuiltConfig.getString(R.string.time_afternoon));
                } else {
                    text.append(BuiltConfig.getString(R.string.time_morning));
                }
                text.append("hh:mm");
            }
        } else if (isYesterday(dateTime)) {
            text.append(BuiltConfig.getString(R.string.time_yesterday));
            text.append(" HH:mm");
        } else if (isSameYear(dateTime)) {
            text.append("M/d HH:mm");
        } else {
            text.append("yyyy-M-d HH:mm");
        }

        // 注意，如果使用android.text.format.DateFormat这个工具类，在API 17之前它只支持adEhkMmszy
        return new SimpleDateFormat(text.toString(), Locale.CHINA).format(date);
    }

    private static boolean inOneMinute(long time1, long time2) {
        return Math.abs(time1 - time2) < 60000;
    }

    private static boolean inOneHour(long time1, long time2) {
        return Math.abs(time1 - time2) < 3600000;
    }

    private static boolean isSameDay(long time) {
        long startTime = floorDay(CALENDAR).getTimeInMillis();
        long endTime = ceilDay(CALENDAR).getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isYesterday(long time) {
        Calendar startCal;
        startCal = floorDay(CALENDAR);
        startCal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = startCal.getTimeInMillis();

        Calendar endCal;
        endCal = ceilDay(CALENDAR);
        endCal.add(Calendar.DAY_OF_MONTH, -1);
        long endTime = endCal.getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isSameYear(long time) {
        Calendar startCal;
        startCal = floorDay(CALENDAR);
        startCal.set(Calendar.MONTH, Calendar.JANUARY);
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        return time >= startCal.getTimeInMillis();
    }

    private static Calendar floorDay(Calendar startCal) {
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        return startCal;
    }

    private static Calendar ceilDay(Calendar endCal) {
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        return endCal;
    }
}
