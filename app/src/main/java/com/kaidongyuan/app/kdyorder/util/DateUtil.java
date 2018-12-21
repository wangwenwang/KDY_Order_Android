package com.kaidongyuan.app.kdyorder.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2015/9/10.
 */
public class DateUtil {
    public static String formateWithoutTime(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String formateWithTime(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    /**
     * 获取指定日期所在月份第一天的Date
     * @param date
     * @return
     */
    public static Date getDateYYYYMM(Date date){
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DATE));

        return calendar.getTime();
    }

    /**
     * 获取今天是星期几
     * @return
     */
    public static String GetCurrWeek(){
        String mWay;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="星期日";
        }else if("2".equals(mWay)){
            mWay ="星期一";
        }else if("3".equals(mWay)){
            mWay ="星期二";
        }else if("4".equals(mWay)){
            mWay ="星期三";
        }else if("5".equals(mWay)){
            mWay ="星期四";
        }else if("6".equals(mWay)){
            mWay ="星期五";
        }else if("7".equals(mWay)){
            mWay ="星期六";
        }
        return mWay;
    }

}
