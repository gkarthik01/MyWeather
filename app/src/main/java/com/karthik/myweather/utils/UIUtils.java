package com.karthik.myweather.utils;

public class UIUtils {

    private static final String url = "https://www.metaweather.com/static/img/weather/png/%s.png";

    public static String getImgUrl(String weatherCondition){
        return String.format(url, weatherCondition);
    }
}
