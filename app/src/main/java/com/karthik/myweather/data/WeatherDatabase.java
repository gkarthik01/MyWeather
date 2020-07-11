package com.karthik.myweather.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.karthik.myweather.data.dao.CityWeatherDao;
import com.karthik.myweather.data.dao.LocationEntityDao;
import com.karthik.myweather.data.entities.City;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.data.entities.LocationEntity;

@Database(
        entities = {LocationEntity.class, City.class, Weather.class},
        version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    public abstract LocationEntityDao locationEntityDao();

    public abstract CityWeatherDao cityWeatherDao();
}
