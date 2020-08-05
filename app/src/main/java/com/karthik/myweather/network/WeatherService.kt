package com.karthik.myweather.network

import com.karthik.myweather.network.model.ConsolidatedWeather
import com.karthik.myweather.network.model.Location
import com.karthik.myweather.network.model.LocationWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("location/search/")
    fun getQueryResults(@Query("query") query: String?): Single<List<Location?>?>?

    @GET("location/{location}/")
    fun getConsolidatedWeather(@Path("location") locationId: Long): Single<ConsolidatedWeather?>?

    @GET("location/search/")
    fun getLocationResults(@Query("lattlong") latLong: String?): Single<List<Location?>?>?

    @GET("location/{location}/{date}")
    fun getConsolidatedWeather(
            @Path("location") locationId: Long, @Path("date") date: String?): Single<List<LocationWeather?>?>?
}