package com.karthik.myweather.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karthik.myweather.R;
import com.karthik.myweather.data.entities.Weather;
import com.karthik.myweather.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private Context context;
    private List<Weather> weatherList;
    public WeatherRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_weather_day, parent, false);
        return new WeatherViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.minTemperature.setText(String.format("%2f",
                weatherList.get(position).minTemperature));
        holder.maxTemperature.setText(String.format("%2f",
                weatherList.get(position).maxTemperature));
        holder.windSpeed.setText(String.format("%2f",
                weatherList.get(position).windSpeed));
        holder.weatherDescription.setText(String.valueOf(
                weatherList.get(position).weatherDescription));
        holder.dateTextView.setText(weatherList.get(position).date);
        Picasso.with(context).load(UIUtils.getImgUrl(weatherList.get(position).weatherCode))
                .placeholder(R.drawable.hc)
                .fit()
                .into(holder.weatherImageView);
    }

    @Override
    public int getItemCount() {
        return weatherList==null?0:weatherList.size();
    }

    public void setItems(List<Weather> weatherList) {
        this.weatherList = weatherList;
        notifyDataSetChanged();
    }
}

class WeatherViewHolder extends RecyclerView.ViewHolder {

    ImageView weatherImageView;
    TextView minTemperature;
    TextView maxTemperature;
    TextView windSpeed;
    TextView weatherDescription;
    TextView dateTextView;

    WeatherViewHolder(@NonNull View itemView) {
        super(itemView);
        weatherImageView = itemView.findViewById(R.id.iv_weather);
        minTemperature = itemView.findViewById(R.id.tv_min);
        maxTemperature = itemView.findViewById(R.id.tv_max);
        windSpeed = itemView.findViewById(R.id.tv_windSpeed);
        weatherDescription = itemView.findViewById(R.id.tv_weatherCondition);
        dateTextView = itemView.findViewById(R.id.tv_date);
    }
}
