package com.karthik.myweather.ui.viewModel

import androidx.lifecycle.MutableLiveData
import com.karthik.myweather.data.WeatherDatabase
import com.karthik.myweather.data.entities.CityWeather
import com.karthik.myweather.data.entities.Weather
import com.karthik.myweather.network.WeatherService
import com.karthik.myweather.network.model.ConsolidatedWeather
import com.karthik.myweather.network.model.LocationWeather
import com.karthik.myweather.utils.BusinessUtils
import com.karthik.myweather.utils.RxScheduler
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class CityWeatherViewModel @Inject constructor(
        private val weatherService: WeatherService, private val weatherDatabase: WeatherDatabase,
        private val businessUtils: BusinessUtils, private val scheduler: RxScheduler) : BaseViewModel() {

    @JvmField
    val weatherList = MutableLiveData<List<Weather>>()

    @JvmField
    val cityName = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()
    fun getWeatherData(locationId: Int) {
        weatherDatabase.cityWeatherDao()
                .get(locationId)
                .doOnSubscribe { disposable ->
                    isLoading.postValue(true)
                    compositeDisposable.add(disposable)
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe(Consumer { cityWeather ->
                    isLoading.postValue(false)
                    cityName.postValue(cityWeather?.city!!.cityName)
                    weatherList.postValue(cityWeather?.weatherList)
                }, Consumer { throwable: Throwable? ->
                    isLoading.postValue(false)
                    error.postValue(Error)
                })
    }

    fun getWeatherData(locationIdParam: Int, date: String?) {
        weatherService.getConsolidatedWeather(locationIdParam.toLong(), date)
                .doOnSubscribe { disposable: Disposable? ->
                    isLoading.postValue(true)
                    compositeDisposable.add(disposable!!)
                }
                .flatMap { weatherList: List<LocationWeather>? ->
                    val consolidatedWeather = ConsolidatedWeather()
                    consolidatedWeather.consolidatedWeather = weatherList
                    val cityWeather = businessUtils.convertToDatabaseEntiry(
                            locationIdParam, consolidatedWeather)
                    Completable.fromAction {
                        weatherDatabase.cityWeatherDao()!!.deleteAll()
                        weatherDatabase.cityWeatherDao()!!.add(cityWeather)
                    }
                            .andThen(weatherDatabase.cityWeatherDao()!!.get(locationIdParam))
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe({ cityWeather: CityWeather? ->
                    isLoading.postValue(false)
                    weatherList.postValue(cityWeather!!.weatherList)
                }) { throwable: Throwable? ->
                    isLoading.postValue(false)
                    error.postValue(Error)
                }
    }

}