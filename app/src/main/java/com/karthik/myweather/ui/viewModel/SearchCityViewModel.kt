package com.karthik.myweather.ui.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.karthik.myweather.Event
import com.karthik.myweather.Permission
import com.karthik.myweather.data.WeatherDatabase
import com.karthik.myweather.data.entities.LocationEntity
import com.karthik.myweather.navigation.NavEvent
import com.karthik.myweather.network.WeatherService
import com.karthik.myweather.network.model.Location
import com.karthik.myweather.utils.RxScheduler
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.stream.Collectors

class SearchCityViewModel @ViewModelInject constructor(private val weatherService: WeatherService,
                                                       private val weatherDatabase: WeatherDatabase, private val scheduler: RxScheduler) : BaseViewModel() {
    val query = MutableLiveData<String>()
    val isValid = MediatorLiveData<Boolean>()

    val permissionRequest = MutableLiveData<Event>()

    val locationRequest = MutableLiveData<Event>()

    val navEvent = MutableLiveData<NavEvent>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    fun init() {
        isValid.postValue(false)
        isValid.addSource(query) { s: String? -> isValid.postValue(isValid(s)) }
    }

    private fun isValid(query: String?): Boolean {
        return if (query == null || query.isEmpty()) false else {
            query.matches(Regex("""[a-zA-Z]*"""))
        }
    }

    fun submitQuery() {
        weatherService.getQueryResults(query.value)
                .doOnSubscribe { disposable: Disposable ->
                    isLoading.postValue(true)
                    compositeDisposable.add(disposable)
                }.flatMapCompletable { locations: List<Location>? -> addToDatabase(locations) }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe({
                    isLoading.postValue(false)
                    navEvent.postValue(NavEvent.LocationToCity)
                }, { throwable: Throwable? -> isLoading.postValue(false) })
    }

    fun addToDatabase(locations: List<Location>?): Completable {
        val entities = locations!!.stream().map { location: Location ->
            LocationEntity(
                    location.title, location.locationId)
        }
                .collect(Collectors.toList())
        return weatherDatabase.locationEntityDao().deleteAll()
                .andThen(weatherDatabase.locationEntityDao().addAll(entities))
    }

    fun submitUseMyLocation() {
        permissionRequest.postValue(Event.PermissionRequest)
    }

    fun onPermission(permission: Permission) {
        if (permission === Permission.GRANTED) {
            locationRequest.postValue(Event.LocationRequest)
        }
    }

    fun getLocationResults(latLong: String?) {
        weatherService.getLocationResults(latLong)
                .flatMapCompletable { locations: List<Location>? ->
                    val entities = locations?.stream()?.map { location: Location ->
                        LocationEntity(
                                location!!.title, location.locationId)
                    }?.collect(Collectors.toList())
                    weatherDatabase.locationEntityDao().deleteAll()
                            .andThen(weatherDatabase.locationEntityDao().addAll(entities))
                }.subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .doOnSubscribe { disposable: Disposable ->
                    compositeDisposable.add(disposable)
                    isLoading.postValue(true)
                }
                .subscribe({
                    isLoading.postValue(false)
                    navEvent.postValue(NavEvent.LocationToCity)
                }) { throwable: Throwable? ->
                    isLoading.postValue(false)
                    error.postValue(Error)
                }
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    fun onLocation(location: android.location.Location) {
        getLocationResults(String.format("%f,%f",
                location.latitude, location.longitude))
    }

}