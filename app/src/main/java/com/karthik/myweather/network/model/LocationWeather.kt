package com.karthik.myweather.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class LocationWeather(
        @SerializedName("id")
        var id: String? = null,

        @SerializedName("weather_state_name")
        var weatherStateName: String? = null,

        @SerializedName("weather_state_abbr")
        var weatherStateAbbr: String? = null,

        @SerializedName("min_temp")
        var minTemperature: Double = 0.0,

        @SerializedName("max_temp")
        var maxTemperature: Double = 0.0,

        @SerializedName("wind_speed")
        var windSpeed : Double= 0.0,

        @SerializedName("applicable_date")
        var date: String? = null
) : Parcelable