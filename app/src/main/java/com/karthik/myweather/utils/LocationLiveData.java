package com.karthik.myweather.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import javax.inject.Inject;

public class LocationLiveData extends LiveData<Location> implements LocationListener {
    private Context context;
    private LocationManager locationManager;

    @Inject
    public LocationLiveData(Context context){
        this.context = context;
    }


    private void requestLocation() {
        locationManager = ContextCompat.getSystemService(context, LocationManager.class);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);
    }

    @Override
    protected void onActive() {
        requestLocation();
    }

    @Override
    protected void onInactive() {
        locationManager.removeUpdates(this);
        locationManager = null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        postValue(location);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
