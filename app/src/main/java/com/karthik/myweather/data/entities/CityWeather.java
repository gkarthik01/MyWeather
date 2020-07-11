package com.karthik.myweather.data.entities;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CityWeather {
    @Embedded
    public City city;

    @Relation(
            parentColumn = "locationId",
            entityColumn = "locationId")
    public List<Weather> weatherList;
}
