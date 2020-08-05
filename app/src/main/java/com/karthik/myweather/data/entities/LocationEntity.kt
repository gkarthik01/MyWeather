package com.karthik.myweather.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity (
    var title: String? = null,
    @PrimaryKey
    var locationId: Int = 0)