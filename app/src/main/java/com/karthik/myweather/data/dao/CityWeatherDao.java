package com.karthik.myweather.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.karthik.myweather.data.entities.City;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.data.entities.Weather;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class CityWeatherDao {

    @Transaction
    @Query("SELECT * FROM cities WHERE locationId = :locationId")
    public abstract Single<CityWeather> get(int locationId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void add(City weather);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addAll(List<Weather> weather);

    @Query("DELETE FROM weather")
    public abstract void deleteAll();

    @Transaction
    public void add(CityWeather cityWeather){
        add(cityWeather.city);
        addAll(cityWeather.weatherList);
    }

}
