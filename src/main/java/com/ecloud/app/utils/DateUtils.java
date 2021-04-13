package com.ecloud.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 日期格式化 **/
public class DateUtils {
    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
