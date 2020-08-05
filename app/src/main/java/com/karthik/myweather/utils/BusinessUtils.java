package com.karthik.myweather.utils;

import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.network.model.ConsolidatedWeather;

import java.util.stream.Collectors;

public class BusinessUtils {

    public CityWeather convertToDatabaseEntiry(int locationIdToSearch, ConsolidatedWeather consolidatedWeather) {
        CityWeather cityWeather = new CityWeather();
        cityWeather.city = new City(){{
            locationId = consolidatedWeather.getLocationId();
            cityName = consolidatedWeather.getCityName();
        }};
        cityWeather.weatherList = consolidatedWeather
                .getConsolidatedWeather()
                .stream()
                .map(locationWeather -> new Weather(){{
                    locationId = locationIdToSearch;
                    date = locationWeather.getDate();
                    minTemperature = locationWeather.getMinTemperature();
                    maxTemperature = locationWeather.getMaxTemperature();
                    windSpeed = locationWeather.getWindSpeed();
                    weatherCode = locationWeather.getWeatherStateAbbr();
                    weatherDescription = locationWeather.getWeatherStateName();
                }})
                .collect(Collectors.toList());
        return cityWeather;
    }

}
