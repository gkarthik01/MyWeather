package com.karthik.myweather.ui.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.data.entities.City;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.data.entities.LocationEntity;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.network.model.ConsolidatedWeather;
import com.karthik.myweather.utils.BusinessUtils;
import com.karthik.myweather.utils.RxScheduler;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class SelectCityViewModel extends BaseViewModel {

    private BusinessUtils businessUtils;
    public MutableLiveData<List<LocationEntity>> locations;
    public MutableLiveData<Integer> locationId = new MutableLiveData<>();
    private WeatherDatabase weatherDatabase;
    private WeatherService weatherService;
    private RxScheduler scheduler;
    private CompositeDisposable compositeDisposable;

    @Inject
    public SelectCityViewModel(WeatherService weatherService,
                               WeatherDatabase weatherDatabase,
                               BusinessUtils businessUtils,
                               RxScheduler scheduler) {
        this.weatherDatabase = weatherDatabase;
        this.weatherService = weatherService;
        this.businessUtils = businessUtils;
        this.scheduler = scheduler;
        compositeDisposable = new CompositeDisposable();
        locations = new MutableLiveData<>();
    }

    public void loadLocations() {
        weatherDatabase.locationEntityDao()
                .getAll()
                .doOnSubscribe(disposable -> {
                    isLoading.postValue(true);
                    compositeDisposable.add(disposable);
                })
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe(locationEntities -> {
                    isLoading.postValue(false);
                    locations.postValue(locationEntities);
                }, throwable -> {
                    isLoading.postValue(false);
                    error.postValue(Error);
                });
    }

    public void getWeatherData(int locationIdToSearch) {
        weatherService.getConsolidatedWeather(locationIdToSearch)
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    isLoading.postValue(true);
                })
                .flatMapCompletable(consolidatedWeather -> {
                    CityWeather cityWeather = businessUtils.convertToDatabaseEntiry(
                            locationIdToSearch, consolidatedWeather);
                    return Completable.fromAction(() -> {
                        weatherDatabase.cityWeatherDao().deleteAll();
                        weatherDatabase.cityWeatherDao().add(cityWeather);
                    });
                })
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe(() -> {
                    isLoading.postValue(false);
                    this.locationId.postValue(locationIdToSearch);
                }, throwable -> {
                    isLoading.postValue(false);
                    error.postValue(Error);
                });
    }


}