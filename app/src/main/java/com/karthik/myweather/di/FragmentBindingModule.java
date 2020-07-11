package com.karthik.myweather.di;

import com.karthik.myweather.ui.view.MainFragment;
import com.karthik.myweather.ui.view.SelectLocationFragment;
import com.karthik.myweather.ui.view.WeatherFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract MainFragment provideMainFragment();

    @ContributesAndroidInjector
    abstract SelectLocationFragment provideSelectLocationFragment();

    @ContributesAndroidInjector
    abstract WeatherFragment provideWeatherFragment();
}
