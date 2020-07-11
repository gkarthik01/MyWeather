package com.karthik.myweather.ui.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.data.entities.City;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.data.entities.LocationEntity;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.network.model.ConsolidatedWeather;
import com.karthik.myweather.utils.RxScheduler;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class SelectLocationViewModel extends BaseViewModel {

    public MutableLiveData<List<LocationEntity>> locations;
    public MutableLiveData<Integer> locationId = new MutableLiveData<>();
    private WeatherDatabase weatherDatabase;
    private WeatherService weatherService;
    private RxScheduler scheduler;
    private CompositeDisposable compositeDisposable;

    @Inject
    public SelectLocationViewModel(WeatherService weatherService,
                                   WeatherDatabase weatherDatabase,
                                   RxScheduler scheduler) {
        this.weatherDatabase = weatherDatabase;
        this.weatherService = weatherService;
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
                    CityWeather cityWeather = convertToDatabaseEntiry(locationIdToSearch, consolidatedWeather);
                    return Completable.fromAction(() -> weatherDatabase.cityWeatherDao().add(cityWeather));
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

    private CityWeather convertToDatabaseEntiry(int locationIdToSearch, ConsolidatedWeather consolidatedWeather) {
        CityWeather cityWeather = new CityWeather();
        cityWeather.city = new City(){{
            locationId = consolidatedWeather.locationId;
            cityName = consolidatedWeather.cityName;
        }};
        cityWeather.weatherList = consolidatedWeather
                .consolidatedWeather
                .stream()
                .map(locationWeather -> new Weather(){{
                    locationId = locationIdToSearch;
                    date = locationWeather.date;
                    minTemperature = locationWeather.minTemperature;
                    maxTemperature = locationWeather.maxTemperature;
                    windSpeed = locationWeather.windSpeed;
                    weatherCode = locationWeather.weatherStateAbbr;
                    weatherDescription = locationWeather.weatherStateName;
                }})
                .collect(Collectors.toList());
        return cityWeather;
    }
}