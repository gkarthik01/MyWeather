package com.karthik.myweather.utils;

import com.karthik.myweather.data.entities.City;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.network.model.ConsolidatedWeather;

import java.util.stream.Collectors;

public class BusinessUtils {

    public CityWeather convertToDatabaseEntiry(int locationIdToSearch, ConsolidatedWeather consolidatedWeather) {
        CityWeather cityWeather = new CityWeather();
        City city = new City();
        city.setLocationId(consolidatedWeather.getLocationId());
        city.setCityName(consolidatedWeather.getCityName());
        cityWeather.setCity(city);
        cityWeather.setWeatherList(consolidatedWeather
                .getConsolidatedWeather()
                .stream()
                .map(locationWeather -> new Weather(locationIdToSearch,
                                locationWeather.getDate(),
                                locationWeather.getMinTemperature(),
                                locationWeather.getMaxTemperature(),
                                locationWeather.getWindSpeed(),
                                locationWeather.getWeatherStateAbbr(),
                                locationWeather.getWeatherStateName()))
                .collect(Collectors.toList()));
        return cityWeather;
    }

}
