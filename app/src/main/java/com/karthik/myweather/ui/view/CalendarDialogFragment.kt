package com.karthik.myweather.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.karthik.myweather.R
import com.karthik.myweather.databinding.DialogCalendarBinding
import java.util.*

class CalendarDialogFragment : DialogFragment(), OnDateChangeListener {
    private lateinit var binding: DialogCalendarBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_calendar, container, false)
        binding.calendarView.setOnDateChangeListener(this)
        return binding.root
    }

    override fun onSelectedDayChange(calendarView: CalendarView, year: Int, month: Int, day: Int) {
        val calenderInstance = Calendar.getInstance()
        calenderInstance[year, month] = day
        val intent = Intent()
        intent.putExtra(CityWeatherFragment.Extra_Date, calenderInstance.time.time)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        dismiss()
    }
}