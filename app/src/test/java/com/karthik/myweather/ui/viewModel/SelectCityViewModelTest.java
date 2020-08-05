package com.karthik.myweather.ui.viewModel;

import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.data.dao.CityWeatherDao;
import com.karthik.myweather.data.dao.LocationEntityDao;
import com.karthik.myweather.data.entities.CityWeather;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.network.model.ConsolidatedWeather;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectCityViewModelTest extends BaseTest{

    SelectCityViewModel viewModel;

    @Mock
    WeatherDatabase weatherDatabase;

    @Mock
    WeatherService weatherService;

    @Mock
    RxScheduler rxScheduler;

    @Mock
    LocationEntityDao locationEntityDao;

    @Mock
    CityWeatherDao cityWeatherDao;

    @Mock
    BusinessUtils businessUtils;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(rxScheduler.io()).thenReturn(Schedulers.trampoline());
        when(rxScheduler.mainThread()).thenReturn(Schedulers.trampoline());
        when(weatherDatabase.locationEntityDao()).thenReturn(locationEntityDao);
        when(weatherDatabase.cityWeatherDao()).thenReturn(cityWeatherDao);
        viewModel = new SelectCityViewModel(weatherService, weatherDatabase,
                businessUtils, rxScheduler);
    }

    @After
    public void tearDown() throws Exception {
        viewModel = null;
        weatherDatabase = null;
        weatherService = null;
        cityWeatherDao = null;
        locationEntityDao = null;
    }

    @Test
    public void getWeatherData() {
        ConsolidatedWeather consolidatedWeather = new ConsolidatedWeather(new ArrayList<>(),
                0, "city");
        when(weatherService.getConsolidatedWeather(anyLong()))
                .thenReturn(Single.just(consolidatedWeather));
        when(businessUtils.convertToDatabaseEntiry(anyInt(), any(ConsolidatedWeather.class)))
                .thenReturn(new CityWeather());
        //doNothing().when(cityWeatherDao).add(any(CityWeather.class));
        viewModel.getWeatherData(0);
        verify(cityWeatherDao).deleteAll();
        verify(cityWeatherDao).add(any(CityWeather.class));
    }

    @Test
    public void loadLocations() {
        when(locationEntityDao.getAll()).thenReturn(Single.just(new ArrayList<>()));
        viewModel.loadLocations();
        verify(locationEntityDao).getAll();
        assertNotNull(viewModel.locations.getValue());
    }
}