package com.karthik.myweather.ui.viewModel;

import com.karthik.myweather.Event;
import com.karthik.myweather.Permission;
import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.navigation.NavEvent;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.network.model.Location;
import com.karthik.myweather.utils.RxScheduler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchCityViewModelTest extends BaseTest{

    SearchCityViewModel viewModel;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(rxScheduler.io()).thenReturn(Schedulers.trampoline());
        when(rxScheduler.mainThread()).thenReturn(Schedulers.trampoline());
        when(weatherDatabase.locationEntityDao()).thenReturn(locationEntityDao);
        when(weatherDatabase.cityWeatherDao()).thenReturn(cityWeatherDao);
        viewModel = new SearchCityViewModel(weatherService, weatherDatabase, rxScheduler);
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
    public void submitQuery() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(){{
            title = "title";
            locationId = 99;
        }});
        when(weatherService.getQueryResults(any()))
                .thenReturn(Single.just(locations));
        when(weatherDatabase.locationEntityDao().addAll(anyList()))
                .thenReturn(Completable.complete());
        when(weatherDatabase.locationEntityDao().deleteAll())
                .thenReturn(Completable.complete());
        viewModel.submitQuery();
        verify(locationEntityDao, times(1)).deleteAll();
        verify(locationEntityDao, times(1)).addAll(anyList());
        assertEquals(viewModel.navEvent.getValue(), NavEvent.LocationToCity);
    }

    @Test
    public void submitUseMyLocation() {
        viewModel.submitUseMyLocation();
        assertEquals(viewModel.permissionRequest.getValue(), Event.PermissionRequest);
    }

    @Test
    public void onPermission_Granted() {
        viewModel.onPermission(Permission.GRANTED);
        assertEquals(viewModel.locationRequest.getValue(), Event.LocationRequest);
    }

    @Test
    public void onPermission_Denied() {
        viewModel.onPermission(Permission.DENIED);
        assertNull(viewModel.locationRequest.getValue());
    }

    @Test
    public void getLocationResults() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(){{
            title = "title";
            locationId = 99;
        }});
        when(weatherService.getLocationResults(any()))
                .thenReturn(Single.just(locations));
        when(weatherDatabase.locationEntityDao().addAll(anyList()))
                .thenReturn(Completable.complete());
        when(weatherDatabase.locationEntityDao().deleteAll())
                .thenReturn(Completable.complete());
        viewModel.getLocationResults("");
        verify(locationEntityDao, times(1)).deleteAll();
        verify(locationEntityDao, times(1)).addAll(anyList());
        assertEquals(viewModel.navEvent.getValue(), NavEvent.LocationToCity);
    }

    @Test
    public void onLocation() {
        android.location.Location location = mock(android.location.Location.class);
        when(location.getLatitude()).thenReturn(10.0);
        when(location.getLongitude()).thenReturn(11.0);
        SearchCityViewModel viewModelSpy = spy(viewModel);
        doNothing().when(viewModelSpy).getLocationResults(anyString());
        viewModelSpy.onLocation(location);
        verify(viewModelSpy).getLocationResults(anyString());
    }
}