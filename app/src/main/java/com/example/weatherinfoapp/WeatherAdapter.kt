package com.example.weatherinfoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter (val cities: List<WeatherResults>) : RecyclerView.Adapter<WeatherViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, true)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        return holder.bind(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}

class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val cityName: TextView = itemView.findViewById(R.id.cityName)
    private val cityMainWeather: TextView = itemView.findViewById(R.id.cityMainWeather)

    fun bind(city: WeatherResults ){
        cityName.text = "bound"
        cityMainWeather.text = city.main
    }
}
