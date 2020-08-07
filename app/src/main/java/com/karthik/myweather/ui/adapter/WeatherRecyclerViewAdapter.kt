package com.karthik.myweather.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karthik.myweather.R
import com.karthik.myweather.data.entities.Weather
import com.karthik.myweather.utils.UIUtils.getImgUrl
import com.squareup.picasso.Picasso

class WeatherRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<WeatherViewHolder>() {
    private var weatherList: List<Weather>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.template_weather_day, parent, false)
        return WeatherViewHolder(v)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.minTemperature.text = String.format("%2f",
                weatherList!![position].minTemperature)
        holder.maxTemperature.text = String.format("%2f",
                weatherList!![position].maxTemperature)
        holder.windSpeed.text = String.format("%2f",
                weatherList!![position].windSpeed)
        holder.weatherDescription.text = weatherList!![position].weatherDescription.toString()
        holder.dateTextView.text = weatherList!![position].date
        Picasso.with(context).load(getImgUrl(weatherList!![position].weatherCode))
                .placeholder(R.drawable.hc)
                .fit()
                .into(holder.weatherImageView)
    }

    override fun getItemCount(): Int {
        return if (weatherList == null) 0 else weatherList!!.size
    }

    fun setItems(weatherList: List<Weather>?) {
        this.weatherList = weatherList
        notifyDataSetChanged()
    }

}

class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var weatherImageView: ImageView
    var minTemperature: TextView
    var maxTemperature: TextView
    var windSpeed: TextView
    var weatherDescription: TextView
    var dateTextView: TextView

    init {
        weatherImageView = itemView.findViewById(R.id.iv_weather)
        minTemperature = itemView.findViewById(R.id.tv_min)
        maxTemperature = itemView.findViewById(R.id.tv_max)
        windSpeed = itemView.findViewById(R.id.tv_windSpeed)
        weatherDescription = itemView.findViewById(R.id.tv_weatherCondition)
        dateTextView = itemView.findViewById(R.id.tv_date)
    }
}