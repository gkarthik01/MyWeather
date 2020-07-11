package com.karthik.myweather.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ConsolidatedWeather implements Serializable {

    @SerializedName("consolidated_weather")
    public List<LocationWeather> consolidatedWeather;

    @SerializedName("woeid")
    public int locationId;

    @SerializedName("title")
    public String cityName;
}
