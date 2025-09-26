package com.karthik.myweather.data.dao

import androidx.room.*
import com.karthik.myweather.data.entities.City
import com.karthik.myweather.data.entities.CityWeather
import com.karthik.myweather.data.entities.Weather
import io.reactivex.Single

@Dao
abstract class CityWeatherDao {

    @Transaction
    @Query("SELECT * FROM cities WHERE locationId = :locationId")
    abstract fun get(locationId: Int): Single<CityWeather?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun add(weather: City?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addAll(weather: List<Weather?>?)

    @Query("DELETE FROM weather")
    abstract fun deleteAll()

    @Transaction
    open fun add(cityWeather: CityWeather) {
        add(cityWeather.city)
        addAll(cityWeather.weatherList)
    }
}