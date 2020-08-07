package com.karthik.myweather.ui.viewModel

import androidx.lifecycle.MutableLiveData
import com.karthik.myweather.data.WeatherDatabase
import com.karthik.myweather.data.entities.LocationEntity
import com.karthik.myweather.network.WeatherService
import com.karthik.myweather.network.model.ConsolidatedWeather
import com.karthik.myweather.utils.BusinessUtils
import com.karthik.myweather.utils.RxScheduler
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class SelectCityViewModel @Inject constructor(private val weatherService: WeatherService,
                                              private val weatherDatabase: WeatherDatabase,
                                              private val businessUtils: BusinessUtils,
                                              private val scheduler: RxScheduler) : BaseViewModel() {
    @JvmField
    val locations: MutableLiveData<List<LocationEntity>> = MutableLiveData()

    @JvmField
    val locationId = MutableLiveData<Int>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    fun loadLocations() {
        weatherDatabase.locationEntityDao().all
                .doOnSubscribe(Consumer { disposable: Disposable ->
                    isLoading.postValue(true)
                    compositeDisposable.add(disposable)
                })
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe({ locationEntities: List<LocationEntity>? ->
                    isLoading.postValue(false)
                    locations.postValue(locationEntities)
                }) { throwable: Throwable? ->
                    isLoading.postValue(false)
                    error.postValue(Error)
                }
    }

    fun getWeatherData(locationIdToSearch: Int) {
        weatherService.getConsolidatedWeather(locationIdToSearch.toLong())
                .doOnSubscribe { disposable: Disposable ->
                    compositeDisposable.add(disposable)
                    isLoading.postValue(true)
                }.flatMapCompletable { consolidatedWeather: ConsolidatedWeather? ->
                    val cityWeather = businessUtils.convertToDatabaseEntiry(
                            locationIdToSearch, consolidatedWeather!!)
                    Completable.fromAction {
                        weatherDatabase.cityWeatherDao().deleteAll()
                        weatherDatabase.cityWeatherDao().add(cityWeather)
                    }
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe({
                    isLoading.postValue(false)
                    locationId.postValue(locationIdToSearch)
                }) { throwable: Throwable? ->
                    isLoading.postValue(false)
                    error.postValue(Error)
                }
    }

}