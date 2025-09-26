package com.karthik.myweather.ui.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karthik.myweather.Event
import com.karthik.myweather.Permission
import com.karthik.myweather.data.WeatherDatabase
import com.karthik.myweather.data.dao.CityWeatherDao
import com.karthik.myweather.data.dao.LocationEntityDao
import com.karthik.myweather.navigation.NavEvent
import com.karthik.myweather.network.WeatherService
import com.karthik.myweather.network.model.Location
import com.karthik.myweather.utils.RxScheduler
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(AndroidJUnit4::class)
class SearchCityViewModelTest {
    lateinit var viewModel: SearchCityViewModel

    @Mock
    lateinit var weatherDatabase: WeatherDatabase

    @Mock
    lateinit var weatherService: WeatherService

    @Mock
    lateinit var rxScheduler: RxScheduler

    @Mock
    lateinit var locationEntityDao: LocationEntityDao

    @Mock
    lateinit var cityWeatherDao: CityWeatherDao

    lateinit var closable: AutoCloseable

    @Before
    @Throws(Exception::class)
    fun setUp() {
        closable = MockitoAnnotations.openMocks(this)
        Mockito.`when`(rxScheduler.io()).thenReturn(Schedulers.trampoline())
        Mockito.`when`(rxScheduler.mainThread()).thenReturn(Schedulers.trampoline())
        Mockito.`when`(weatherDatabase.locationEntityDao()).thenReturn(locationEntityDao)
        Mockito.`when`(weatherDatabase.cityWeatherDao()).thenReturn(cityWeatherDao)
        viewModel = SearchCityViewModel(weatherService, weatherDatabase, rxScheduler)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        closable.close()
    }

    @Test
    fun submitQuery() {
        val locations: MutableList<Location?> = ArrayList()
        locations.add(Location("title", 99))
        Mockito.`when`(weatherService!!.getQueryResults(ArgumentMatchers.any<String>()))
                .thenReturn(Single.just(locations))
        Mockito.`when`(weatherDatabase!!.locationEntityDao()!!.addAll(ArgumentMatchers.anyList()))
                .thenReturn(Completable.complete())
        Mockito.`when`(weatherDatabase!!.locationEntityDao()!!.deleteAll())
                .thenReturn(Completable.complete())
        viewModel!!.submitQuery()
        Mockito.verify(locationEntityDao, Mockito.times(1))!!.deleteAll()
        Mockito.verify(locationEntityDao, Mockito.times(1))!!.addAll(ArgumentMatchers.anyList())
        Assert.assertEquals(viewModel!!.navEvent.value, NavEvent.LocationToCity)
    }

    @Test
    fun submitUseMyLocation() {
        viewModel!!.submitUseMyLocation()
        Assert.assertEquals(viewModel!!.permissionRequest.value, Event.PermissionRequest)
    }

    @Test
    fun onPermission_Granted() {
        viewModel!!.onPermission(Permission.GRANTED)
        Assert.assertEquals(viewModel!!.locationRequest.value, Event.LocationRequest)
    }

    @Test
    fun onPermission_Denied() {
        viewModel!!.onPermission(Permission.DENIED)
        Assert.assertNull(viewModel!!.locationRequest.value)
    }

    @Test
    fun testGetLocations() {
        val locations: MutableList<Location?> = ArrayList()
        locations.add(Location("title", 99))
        Mockito.`when`(weatherService!!.getLocationResults(ArgumentMatchers.any<String>()))
                .thenReturn(Single.just(locations))
        Mockito.`when`(weatherDatabase!!.locationEntityDao()!!.addAll(ArgumentMatchers.anyList()))
                .thenReturn(Completable.complete())
        Mockito.`when`(weatherDatabase!!.locationEntityDao()!!.deleteAll())
                .thenReturn(Completable.complete())
        viewModel!!.getLocationResults("")
        Mockito.verify(locationEntityDao, Mockito.times(1))!!.deleteAll()
        Mockito.verify(locationEntityDao, Mockito.times(1))!!.addAll(ArgumentMatchers.anyList())
        Assert.assertEquals(viewModel!!.navEvent.value, NavEvent.LocationToCity)
    }

    @Test
    fun onLocation() {
        val location = Mockito.mock(android.location.Location::class.java)
        Mockito.`when`(location.latitude).thenReturn(10.0)
        Mockito.`when`(location.longitude).thenReturn(11.0)
        val viewModelSpy = Mockito.spy(viewModel)
        Mockito.doNothing().`when`(viewModelSpy)!!.getLocationResults(ArgumentMatchers.anyString())
        viewModelSpy!!.onLocation(location)
        Mockito.verify(viewModelSpy)!!.getLocationResults(ArgumentMatchers.anyString())
    }
}