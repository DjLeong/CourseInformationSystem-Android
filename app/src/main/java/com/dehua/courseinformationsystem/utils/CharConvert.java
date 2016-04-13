package com.dehua.courseinformationsystem.utils;

/**
 * Created by dehua on 16/4/13 013.
 */
public class CharConvert {
    public static String ConvertDay(String day) {
        String Day = null;
        switch (day) {
            case "0":
                Day = "星期一";
                break;
            case "1":
                Day = "星期二";
                break;
            case "2":
                Day = "星期三";
                break;
            case "3":
                Day = "星期四";
                break;
            case "4":
                Day = "星期五";
                break;
            case "5":
                Day = "星期六";
            case "6":
                Day = "星期天";
                break;
        }
        return Day;
    }
}
