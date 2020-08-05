package com.karthik.myweather.ui.viewModel;

import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.utils.BusinessUtils;
import com.karthik.myweather.utils.RxScheduler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CityWeatherViewModelTest extends BaseTest{

    CityWeatherViewModel viewModel;

    @Mock
    WeatherDatabase weatherDatabase;

    @Mock
    RxScheduler rxScheduler;

    @Mock
    LocationEntityDao locationEntityDao;

    @Mock
    CityWeatherDao cityWeatherDao;

    @Mock
    BusinessUtils businessUtils;

    @Mock
    WeatherService weatherService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(rxScheduler.io()).thenReturn(Schedulers.trampoline());
        when(rxScheduler.mainThread()).thenReturn(Schedulers.trampoline());
        when(weatherDatabase.locationEntityDao()).thenReturn(locationEntityDao);
        when(weatherDatabase.cityWeatherDao()).thenReturn(cityWeatherDao);
        viewModel = new CityWeatherViewModel(weatherService, weatherDatabase,
                businessUtils, rxScheduler);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getWeatherData() {
        when(cityWeatherDao.get(anyInt())).thenReturn(Single.just(new CityWeather(){{
            weatherList = new ArrayList<>();
            city = new City(){{
                cityName = "name";
                locationId = 0;
            }};
        }}));
        viewModel.getWeatherData(0);
        verify(cityWeatherDao).get(0);
        assertNotNull(viewModel.weatherList.getValue());
    }
}