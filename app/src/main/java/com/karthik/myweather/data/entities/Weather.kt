package com.karthik.myweather.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather (
        var locationId: Int = 0,
        @PrimaryKey
        var date: String,
        var minTemperature: Double = 0.0,
        var maxTemperature: Double = 0.0,
        var windSpeed: Double = 0.0,
        var weatherCode: String? = null,
        var weatherDescription: String? = null
)