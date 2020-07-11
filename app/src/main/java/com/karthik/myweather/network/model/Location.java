package com.karthik.myweather.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {

    @SerializedName("title")
    public String title;

    @SerializedName("woeid")
    public int locationId;
}
