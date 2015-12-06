package com.moon.myreadapp.util;

import com.moon.appframework.common.log.XLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by moon on 15/12/4.
 */
public class Conver {

    //把日期转为字符串
    public static String ConverToString(Date date)
    {
        if (date == null){
            return "";
        }
        XLog.d("execute !" + date.getTime());
        DateFormat df = new SimpleDateFormat("MM月dd日HH:mm");
        XLog.d("execute !" + df.format(date));
        return df.format(date);
    }
    //把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }
}
