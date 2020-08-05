package com.karthik.myweather.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.karthik.myweather.data.dao.CityWeatherDao
import com.karthik.myweather.data.dao.LocationEntityDao
import com.karthik.myweather.data.entities.City
import com.karthik.myweather.data.entities.LocationEntity
import com.karthik.myweather.data.entities.Weather

@Database(entities = [LocationEntity::class, City::class, Weather::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun locationEntityDao(): LocationEntityDao?
    abstract fun cityWeatherDao(): CityWeatherDao?
}