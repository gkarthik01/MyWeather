package com.karthik.myweather.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Location (
        @SerializedName("title")
        val title: String,

        @SerializedName("woeid")
        val locationId: Int) : Parcelable