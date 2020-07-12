package com.karthik.myweather.di;

import com.karthik.myweather.ui.view.CityWeatherFragment;
import com.karthik.myweather.ui.view.SearchCityFragment;
import com.karthik.myweather.ui.view.SelectCityFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract SearchCityFragment provideMainFragment();

    @ContributesAndroidInjector
    abstract SelectCityFragment provideSelectLocationFragment();

    @ContributesAndroidInjector
    abstract CityWeatherFragment provideWeatherFragment();
}
