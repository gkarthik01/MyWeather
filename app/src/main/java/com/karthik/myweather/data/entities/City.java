package com.karthik.myweather.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {

    @PrimaryKey
    public int locationId;
    public String cityName;

}
