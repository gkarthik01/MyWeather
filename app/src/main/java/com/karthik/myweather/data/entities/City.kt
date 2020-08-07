package com.karthik.myweather.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
        @PrimaryKey var locationId: Int = 0,
        var cityName: String? = null)