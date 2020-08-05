package com.karthik.myweather.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CityWeather(
    @Embedded
    var city: City? = null,
    @Relation(parentColumn = "locationId", entityColumn = "locationId")
    var weatherList: List<Weather>? = null)
