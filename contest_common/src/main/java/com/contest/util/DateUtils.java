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
}
