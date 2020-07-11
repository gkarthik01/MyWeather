package com.karthik.myweather.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationWeather implements Serializable {

    @SerializedName("id")
    public String id;

    @SerializedName("weather_state_name")
    public String weatherStateName;

    @SerializedName("weather_state_abbr")
    public String weatherStateAbbr;

    @SerializedName("min_temp")
    public double minTemperature;

    @SerializedName("max_temp")
    public double maxTemperature;

    @SerializedName("wind_speed")
    public double windSpeed;

    @SerializedName("applicable_date")
    public String date;
}
