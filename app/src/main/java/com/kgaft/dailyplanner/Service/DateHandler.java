package com.kgaft.dailyplanner.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHandler {
    public static String getDayName() {
        SimpleDateFormat sdf = new SimpleDateFormat("u");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        switch(dayOfTheWeek){
            case "1":
                return "Monday";
            case "2":
                return  "Tuesday";
            case "3":
                return  "Wednesday";
            case "4":
                return  "Thursday";
            case "5":
                return "Friday";
            case "6":
                return  "Saturday";
            case "7":
                return  "Sunday";
            default:
                return "Monday";
        }

    }
    public static String getHours(){
        SimpleDateFormat sdf = new SimpleDateFormat("H");
        Date d = new Date();
        String time = sdf.format(d);
        return time;
    }
    public static String getMinutes(){
        SimpleDateFormat sdf = new SimpleDateFormat("m");
        Date d = new Date();
        String time = sdf.format(d);
        return time;
    }
}
