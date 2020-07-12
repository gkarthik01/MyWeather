package com.karthik.myweather.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.karthik.myweather.R;
import com.karthik.myweather.databinding.DialogCalendarBinding;

import java.util.Calendar;
import java.util.Date;

public class CalendarDialogFragment extends DialogFragment implements CalendarView.OnDateChangeListener {

    private DialogCalendarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         binding = DataBindingUtil.inflate(inflater, R.layout.dialog_calendar, container, false);
         binding.calendarView.setOnDateChangeListener(this);
         return binding.getRoot();
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
        Calendar calenderInstance = Calendar.getInstance();
        calenderInstance.set(year, month, day);
        Intent intent = new Intent();
        intent.putExtra(CityWeatherFragment.Extra_Date, calenderInstance.getTime().getTime());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}
