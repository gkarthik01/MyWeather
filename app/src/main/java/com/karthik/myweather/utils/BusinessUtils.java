package com.karthik.myweather.utils;

import com.karthik.myweather.data.entities.City;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.network.model.ConsolidatedWeather;

import java.util.stream.Collectors;

public class BusinessUtils {

    public CityWeather convertToDatabaseEntiry(int locationIdToSearch, ConsolidatedWeather consolidatedWeather) {
        CityWeather cityWeather = new CityWeather();
        cityWeather.city = new City(){{
            locationId = consolidatedWeather.locationId;
            cityName = consolidatedWeather.cityName;
        }};
        cityWeather.weatherList = consolidatedWeather
                .consolidatedWeather
                .stream()
                .map(locationWeather -> new Weather(){{
                    locationId = locationIdToSearch;
                    date = locationWeather.date;
                    minTemperature = locationWeather.minTemperature;
                    maxTemperature = locationWeather.maxTemperature;
                    windSpeed = locationWeather.windSpeed;
                    weatherCode = locationWeather.weatherStateAbbr;
                    weatherDescription = locationWeather.weatherStateName;
                }})
                .collect(Collectors.toList());
        return cityWeather;
    }

}
