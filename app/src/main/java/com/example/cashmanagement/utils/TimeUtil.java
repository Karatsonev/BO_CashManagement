package com.example.cashmanagement.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  sdf.format(new Date());
    }

    public static String getDateOnly(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return  sdf.format(new Date());
    }
}
