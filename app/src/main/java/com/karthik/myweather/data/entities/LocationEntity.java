package com.karthik.myweather.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class LocationEntity {

    public String title;

    @PrimaryKey
    public int locationId;
}
