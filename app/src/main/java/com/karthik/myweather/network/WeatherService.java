package com.karthik.myweather.network;

import com.karthik.myweather.network.model.ConsolidatedWeather;
import com.karthik.myweather.network.model.Location;
import com.karthik.myweather.network.model.LocationWeather;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("location/search/")
    Single<List<Location>> getQueryResults(@Query("query") String query);

    @GET("location/{location}/")
    Single<ConsolidatedWeather> getConsolidatedWeather(@Path("location") long locationId);

    @GET("location/search/")
    Single<List<Location>> getLocationResults(@Query("lattlong") String latLong);

    @GET("location/{location}/{date}")
    Single<List<LocationWeather>> getConsolidatedWeather(
            @Path("location") long locationId, @Path("date") String date);
}
