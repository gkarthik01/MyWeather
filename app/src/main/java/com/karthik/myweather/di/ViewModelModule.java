package com.karthik.myweather.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.karthik.myweather.ui.viewModel.MainViewModel;
import com.karthik.myweather.ui.viewModel.SelectLocationViewModel;
import com.karthik.myweather.ui.viewModel.WeatherViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel.class)
    abstract ViewModel bindWeatherViewModel(WeatherViewModel weatherViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectLocationViewModel.class)
    abstract ViewModel bindSelectLocationViewModel(SelectLocationViewModel selectLocationViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
