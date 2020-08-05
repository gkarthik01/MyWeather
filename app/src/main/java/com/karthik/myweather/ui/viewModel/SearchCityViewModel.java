package com.karthik.myweather.ui.viewModel;


import android.location.Location;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.karthik.myweather.Event;
import com.karthik.myweather.Permission;
import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.data.entities.LocationEntity;
import com.karthik.myweather.navigation.NavEvent;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.utils.RxScheduler;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class SearchCityViewModel extends BaseViewModel {

    public MutableLiveData<String> query = new MutableLiveData<>();
    public MediatorLiveData<Boolean> isValid = new MediatorLiveData<>();
    public MutableLiveData<Event> permissionRequest = new MutableLiveData<>();
    public MutableLiveData<Event> locationRequest = new MutableLiveData<>();
    public MutableLiveData<NavEvent> navEvent = new MutableLiveData<>();
    private WeatherDatabase weatherDatabase;
    private WeatherService weatherService;
    private RxScheduler scheduler;
    private CompositeDisposable compositeDisposable;

    @Inject
    public SearchCityViewModel(WeatherService weatherService,
                               WeatherDatabase weatherDatabase, RxScheduler scheduler) {
        this.weatherService = weatherService;
        this.weatherDatabase = weatherDatabase;
        this.scheduler = scheduler;
        compositeDisposable = new CompositeDisposable();
    }

    public void init() {
        isValid.postValue(false);
        isValid.addSource(query, s -> {
            isValid.postValue(isValid(s));
        });
    }

    private boolean isValid(String query) {
        if (query == null || query.isEmpty())
            return false;
        else {
            return query.matches("[a-zA-Z]*");
        }
    }

    public void submitQuery() {
        weatherService.getQueryResults(query.getValue())
                .doOnSubscribe(disposable -> {
                    isLoading.postValue(true);
                    compositeDisposable.add(disposable);
                })
                .flatMapCompletable(locations -> addToDatabase(locations))
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe(() -> {
                    isLoading.postValue(false);
                    navEvent.postValue(NavEvent.LocationToCity);
                }, throwable -> {
                    isLoading.postValue(false);
                });
    }

    public Completable addToDatabase(List<com.karthik.myweather.network.model.Location> locations) {
        List<LocationEntity> entities = locations.stream().map(
                location -> new LocationEntity() {{
                    title = location.getTitle();
                    locationId = location.getLocationId();
                }}).collect(Collectors.toList());
        return weatherDatabase.locationEntityDao().deleteAll()
                .andThen(weatherDatabase.locationEntityDao().addAll(entities));
    }

    public void submitUseMyLocation() {
        permissionRequest.postValue(Event.PermissionRequest);
    }

    public void onPermission(Permission permission) {
        if (permission == Permission.GRANTED) {
            locationRequest.postValue(Event.LocationRequest);
        }
    }

    public void getLocationResults(String latLong) {
        weatherService.getLocationResults(latLong)
                .flatMapCompletable(locations -> {
                    List<LocationEntity> entities = locations.stream().map(
                            location -> new LocationEntity() {{
                                title = location.getTitle();
                                locationId = location.getLocationId();
                            }}).collect(Collectors.toList());
                    return weatherDatabase.locationEntityDao().deleteAll()
                            .andThen(weatherDatabase.locationEntityDao().addAll(entities));
                }).subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    isLoading.postValue(true);
                })
                .subscribe(() -> {
                    isLoading.postValue(false);
                    navEvent.postValue(NavEvent.LocationToCity);
                }, throwable -> {
                    isLoading.postValue(false);
                    error.postValue(Error);
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void onLocation(Location location) {
        getLocationResults(String.format("%f,%f",
                location.getLatitude(), location.getLongitude()));
    }
}