package com.karthik.myweather.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import javax.inject.Inject

class LocationLiveData @Inject constructor(private val context: Context) : LiveData<Location?>(), LocationListener {
    private var locationManager: LocationManager? = null
    private fun requestLocation() {
        locationManager = ContextCompat.getSystemService(context, LocationManager::class.java)
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
    }

    override fun onActive() {
        requestLocation()
    }

    override fun onInactive() {
        locationManager!!.removeUpdates(this)
        locationManager = null
    }

    override fun onLocationChanged(location: Location) {
        postValue(location)
        locationManager!!.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

}