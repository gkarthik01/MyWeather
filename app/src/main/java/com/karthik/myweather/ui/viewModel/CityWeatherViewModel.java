package com.karthik.myweather.ui.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.network.model.ConsolidatedWeather;
import com.karthik.myweather.utils.BusinessUtils;
import com.karthik.myweather.utils.RxScheduler;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
public class CityWeatherViewModel extends BaseViewModel {

    private BusinessUtils businessUtils;
    private WeatherService weatherService;
    private RxScheduler scheduler;
    public MutableLiveData<List<Weather>> weatherList = new MutableLiveData<>();
    public MutableLiveData<String> cityName = new MutableLiveData<>();
    private WeatherDatabase weatherDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public CityWeatherViewModel(
            WeatherService weatherService, WeatherDatabase weatherDatabase,
            BusinessUtils businessUtils, RxScheduler scheduler){
        this.weatherDatabase = weatherDatabase;
        this.weatherService = weatherService;
        this.businessUtils = businessUtils;
        this.scheduler = scheduler;
    }

    public void getWeatherData(int locationId) {
        weatherDatabase.cityWeatherDao()
                .get(locationId)
                .doOnSubscribe(disposable -> {
                    isLoading.postValue(true);
                    compositeDisposable.add(disposable);
                })
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe(cityWeather -> {
                    isLoading.postValue(false);
                    cityName.postValue(cityWeather.city.cityName);
                    this.weatherList.postValue(cityWeather.weatherList);
                }, throwable -> {
                    isLoading.postValue(false);
                    error.postValue(Error);
                });
    }

    public void getWeatherData(int locationIdParam, String date){
        weatherService.getConsolidatedWeather(locationIdParam, date)
                .doOnSubscribe(disposable -> {
                    isLoading.postValue(true);
                    compositeDisposable.add(disposable);
                })
                .flatMap(weatherList -> {
                    ConsolidatedWeather consolidatedWeather = new ConsolidatedWeather();
                    consolidatedWeather.setConsolidatedWeather(weatherList);
                    CityWeather cityWeather = businessUtils.convertToDatabaseEntiry(
                            locationIdParam, consolidatedWeather);
                    return Completable.fromAction(() -> {
                        weatherDatabase.cityWeatherDao().deleteAll();
                        weatherDatabase.cityWeatherDao().add(cityWeather);
                    })
                            .andThen(weatherDatabase.cityWeatherDao().get(locationIdParam));

                })
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe(cityWeather -> {
                    isLoading.postValue(false);
                    this.weatherList.postValue(cityWeather.weatherList);
                }, throwable -> {
                    isLoading.postValue(false);
                    error.postValue(Error);
                });
    }

}