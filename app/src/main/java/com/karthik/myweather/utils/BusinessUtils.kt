package com.karthik.myweather.utils

import com.karthik.myweather.data.entities.City
import com.karthik.myweather.data.entities.CityWeather
import com.karthik.myweather.data.entities.Weather
import com.karthik.myweather.network.model.ConsolidatedWeather
import java.util.stream.Collectors
import javax.inject.Inject

class BusinessUtils {
    fun convertToDatabaseEntiry(locationIdToSearch: Int, consolidatedWeather: ConsolidatedWeather): CityWeather {
        val cityWeather = CityWeather()
        val city = City(consolidatedWeather.locationId, consolidatedWeather.cityName)
        cityWeather.city = city
        cityWeather.weatherList = consolidatedWeather
                .consolidatedWeather
                ?.map { (_, weatherStateName, weatherStateAbbr, minTemperature, maxTemperature, windSpeed, date) ->
                    Weather(locationIdToSearch,
                            date!!,
                            minTemperature,
                            maxTemperature,
                            windSpeed,
                            weatherStateAbbr,
                            weatherStateName)
                }
        return cityWeather
    }
}