package com.karthik.myweather.utils

object UIUtils {
    private const val url = "https://www.metaweather.com/static/img/weather/png/%s.png"
    @JvmStatic
    fun getImgUrl(weatherCondition: String?): String {
        return String.format(url, weatherCondition)
    }
}