package com.karthik.myweather.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.karthik.myweather.ui.viewModel.SearchCityViewModel;
import com.karthik.myweather.ui.viewModel.SelectCityViewModel;
import com.karthik.myweather.ui.viewModel.CityWeatherViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchCityViewModel.class)
    abstract ViewModel bindMainViewModel(SearchCityViewModel searchCityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CityWeatherViewModel.class)
    abstract ViewModel bindWeatherViewModel(CityWeatherViewModel cityWeatherViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectCityViewModel.class)
    abstract ViewModel bindSelectLocationViewModel(SelectCityViewModel selectCityViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
