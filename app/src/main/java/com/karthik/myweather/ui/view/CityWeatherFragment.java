package com.karthik.myweather.ui.view;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karthik.myweather.R;
import com.karthik.myweather.databinding.CityWeatherFragmentBinding;
import com.karthik.myweather.ui.adapter.WeatherRecyclerViewAdapter;
import com.karthik.myweather.ui.viewModel.CityWeatherViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CityWeatherFragment extends BaseFragment {

    private static final String Tag_Calendar_Dialog = "Tag_Calendar_Dialog";
    public static final String Extra_Location_Id = "Extra_Location_Id";
    public static final String Extra_Date = "Extra_Date";
    public static final int Date_Request_Code = 11;
    private CityWeatherViewModel mViewModel;
    private CityWeatherFragmentBinding binding;
    private WeatherRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.city_weather_fragment,
                container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mViewModel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvWeather.setLayoutManager(layoutManager);
        binding.rvWeather.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        adapter = new WeatherRecyclerViewAdapter(getActivity());
        binding.rvWeather.setAdapter(adapter);
        binding.etDatePicker.setOnClickListener(view -> {
            showCalendarFragment();
        });
        return binding.getRoot();
    }

    private void showCalendarFragment(){
        Fragment prev = getFragmentManager()
                .findFragmentByTag(Tag_Calendar_Dialog);
        if(prev==null){
            CalendarDialogFragment calendarFragment = new CalendarDialogFragment();
            calendarFragment.setTargetFragment(this, Date_Request_Code);
            calendarFragment.setCancelable(true);
            calendarFragment.show(getFragmentManager(), Tag_Calendar_Dialog);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CityWeatherViewModel.class);
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
        mViewModel.cityName.observe(this, cityName ->{
            setTitle(cityName);
        });
        mViewModel.weatherList.observe(this, weatherList -> {
            adapter.setItems(weatherList);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Date_Request_Code && resultCode== Activity.RESULT_OK){
            Date date = new Date(data.getLongExtra(Extra_Date, 0l));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String formattedDate = dateFormat.format(date);
            binding.etDatePicker.setText(formattedDate);
            mViewModel.getWeatherData(getArguments().getInt(Extra_Location_Id),
                    formattedDate);
        }
    }
}