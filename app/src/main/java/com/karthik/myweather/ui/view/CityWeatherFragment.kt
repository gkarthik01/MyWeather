package com.karthik.myweather.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.karthik.myweather.R
import com.karthik.myweather.data.entities.Weather
import com.karthik.myweather.databinding.CityWeatherFragmentBinding
import com.karthik.myweather.ui.adapter.WeatherRecyclerViewAdapter
import com.karthik.myweather.ui.viewModel.CityWeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class CityWeatherFragment : BaseFragment() {
    private lateinit var mViewModel: CityWeatherViewModel
    private lateinit var binding: CityWeatherFragmentBinding
    private lateinit var adapter: WeatherRecyclerViewAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.city_weather_fragment,
                container, false)
        binding.lifecycleOwner = this
        binding.viewModel = mViewModel
        val layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false)
        binding.rvWeather.layoutManager = layoutManager
        binding.rvWeather.addItemDecoration(DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL))
        adapter = WeatherRecyclerViewAdapter(requireActivity())
        binding.rvWeather.adapter = adapter
        binding.etDatePicker.setOnClickListener { showCalendarFragment() }
        return binding.root
    }

    private fun showCalendarFragment() {
        val prev = parentFragmentManager
                .findFragmentByTag(Tag_Calendar_Dialog)
        if (prev == null) {
            val calendarFragment = CalendarDialogFragment()
            calendarFragment.setTargetFragment(this, Date_Request_Code)
            calendarFragment.isCancelable = true
            calendarFragment.show(parentFragmentManager, Tag_Calendar_Dialog)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CityWeatherViewModel::class.java)
        registerForData()
        registerForLoadingIndicator(mViewModel)
        registerForErrorDialog(mViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getWeatherData(requireArguments().getInt(Extra_Location_Id))
    }

    private fun registerForData() {
        mViewModel.cityName.observe(this, Observer { cityName: String -> setTitle(cityName) })
        mViewModel.weatherList.observe(this, Observer { weatherList: List<Weather> -> adapter.setItems(weatherList) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Date_Request_Code && resultCode == Activity.RESULT_OK) {
            val date = Date(data!!.getLongExtra(Extra_Date, 0L))
            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
            val formattedDate = dateFormat.format(date)
            binding.etDatePicker.setText(formattedDate)
            mViewModel.getWeatherData(requireArguments().getInt(Extra_Location_Id),
                    formattedDate)
        }
    }

    companion object {
        private const val Tag_Calendar_Dialog = "Tag_Calendar_Dialog"
        const val Extra_Location_Id = "Extra_Location_Id"
        const val Extra_Date = "Extra_Date"
        const val Date_Request_Code = 11
    }
}