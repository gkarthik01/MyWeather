package com.karthik.myweather.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ConsolidatedWeather (
        @SerializedName("consolidated_weather")
        var consolidatedWeather: List<LocationWeather>?,

        @SerializedName("woeid")
        var locationId: Int = 0,

        @SerializedName("title")
        var cityName: String? = null) : Parcelable {
        constructor() : this(null, 0, null)
}