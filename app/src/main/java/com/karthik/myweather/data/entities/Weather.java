package com.karthik.myweather.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather")
public class Weather {
    public int locationId;
    @NonNull
    @PrimaryKey
    public String date;
    public double minTemperature;
    public double maxTemperature;
    public double windSpeed;
    public String weatherCode;
    public String weatherDescription;
}
