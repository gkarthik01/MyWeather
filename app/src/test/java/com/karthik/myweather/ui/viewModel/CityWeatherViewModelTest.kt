package com.karthik.myweather.ui.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karthik.myweather.data.WeatherDatabase
import com.karthik.myweather.data.dao.CityWeatherDao
import com.karthik.myweather.data.dao.LocationEntityDao
import com.karthik.myweather.data.entities.City
import com.karthik.myweather.data.entities.CityWeather
import com.karthik.myweather.network.WeatherService
import com.karthik.myweather.utils.BusinessUtils
import com.karthik.myweather.utils.RxScheduler
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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(AndroidJUnit4::class)
class CityWeatherViewModelTest {

    lateinit var viewModel: CityWeatherViewModel

    @Mock
    lateinit var weatherDatabase: WeatherDatabase

    @Mock
    lateinit var rxScheduler: RxScheduler

    @Mock
    lateinit var locationEntityDao: LocationEntityDao

    @Mock
    lateinit var cityWeatherDao: CityWeatherDao

    @Mock
    lateinit var businessUtils: BusinessUtils

    @Mock
    lateinit var weatherService: WeatherService

    lateinit var closable: AutoCloseable

    @Before
    @Throws(Exception::class)
    fun setUp() {
        closable = MockitoAnnotations.openMocks(this)
        `when`(rxScheduler.io()).thenReturn(Schedulers.trampoline())
        `when`(rxScheduler.mainThread()).thenReturn(Schedulers.trampoline())
        `when`(weatherDatabase.locationEntityDao()).thenReturn(locationEntityDao)
        `when`(weatherDatabase.cityWeatherDao()).thenReturn(cityWeatherDao)
        viewModel = CityWeatherViewModel(weatherService, weatherDatabase,
                businessUtils, rxScheduler)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        closable.close()
    }

    @Test
    fun testGetweatherData() {
        `when`(cityWeatherDao.get(ArgumentMatchers.anyInt())).thenReturn(Single.just(CityWeather(City(0, "name"), ArrayList())))
        viewModel.getWeatherData(0)
        Mockito.verify(cityWeatherDao).get(0)
        Assert.assertNotNull(viewModel.weatherList.value)
    }
}