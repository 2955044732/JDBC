package com.qwh.utils;

import java.sql.Date;

/**
 * @auther qwh
 * @create 2023-06-2023/6/1 15:06
 */
public class DateUtils {
    public static Date getTime()
    {
        java.util.Date date = new java.util.Date();

        return new Date(date.getTime());
    }


}
