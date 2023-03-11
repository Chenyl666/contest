package com.contest.util;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @SneakyThrows
    public static Date getDateByString(String timeStr){
        return simpleDateFormat.parse(timeStr);
    }

    public static Boolean dateRangeCompare(Date dateRange1,Date dateRange2,Date date){
        long timeStamp1 = dateRange1.getTime();
        long timeStamp2 = dateRange2.getTime();
        if(timeStamp1 > timeStamp2){
            long tmp = timeStamp1;
            timeStamp1 = timeStamp2;
            timeStamp2 = tmp;
        }
        long timeStamp = date.getTime();
        return timeStamp >= timeStamp1 && timeStamp <= timeStamp2;
    }

    public static String getTimeStr(Date date){
        return simpleDateFormat.format(date);
    }
}
