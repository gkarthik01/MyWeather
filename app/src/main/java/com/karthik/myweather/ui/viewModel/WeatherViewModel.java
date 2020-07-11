package com.karthik.myweather.ui.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.utils.RxScheduler;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
public class WeatherViewModel extends BaseViewModel {

    private RxScheduler scheduler;
    public MutableLiveData<CityWeather> cityWeather = new MutableLiveData<>();
    private WeatherDatabase weatherDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public WeatherViewModel(WeatherDatabase weatherDatabase, RxScheduler scheduler){
        this.weatherDatabase = weatherDatabase;
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
                    this.cityWeather.postValue(cityWeather);
                }, throwable -> {
                    isLoading.postValue(false);
                    error.postValue(Error);
                });
    }

}