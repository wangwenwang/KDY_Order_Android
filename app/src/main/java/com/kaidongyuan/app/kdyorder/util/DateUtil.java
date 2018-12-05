package com.kaidongyuan.app.kdyorder.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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


}
