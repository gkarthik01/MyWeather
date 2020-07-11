package com.karthik.myweather.ui.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karthik.myweather.R;
import com.karthik.myweather.databinding.WeatherFragmentBinding;
import com.karthik.myweather.ui.adapter.WeatherRecyclerViewAdapter;
import com.karthik.myweather.ui.viewModel.WeatherViewModel;

public class WeatherFragment extends BaseFragment {

    public static final String Extra_Location_Id = "Extra_Location_Id";
    private WeatherViewModel mViewModel;
    private WeatherFragmentBinding binding;
    private WeatherRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment,
                container, false);
        binding.setViewModel(mViewModel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvWeather.setLayoutManager(layoutManager);
        binding.rvWeather.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        adapter = new WeatherRecyclerViewAdapter(getActivity());
        binding.rvWeather.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(WeatherViewModel.class);
        registerForData();
        registerForLoadingIndicator(mViewModel);
        registerForErrorDialog(mViewModel);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.getWeatherData(getArguments().getInt(Extra_Location_Id));
    }

    private void registerForData() {
        mViewModel.cityWeather.observe(this, cityWeather -> {
            binding.tvCity.setText(cityWeather.city.cityName);
            adapter.setItems(cityWeather.weatherList);
        });
    }

}