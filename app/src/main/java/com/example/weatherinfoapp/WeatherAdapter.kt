package com.example.weatherinfoapp

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter (val cities: List<WeatherResults>, val res: Resources) : RecyclerView.Adapter<WeatherViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return WeatherViewHolder(view, res)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        return holder.bind(cities[position], position)
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}

class WeatherViewHolder(itemView: View, res: Resources): RecyclerView.ViewHolder(itemView) {
    var cityNames: Array<String> = res.getStringArray(R.array.cityNames)

    private val cityName: TextView = itemView.findViewById(R.id.cityName)
    private val cityMainWeather: TextView = itemView.findViewById(R.id.cityMainWeather)

    fun bind(city: WeatherResults, position: Int ){
        cityName.text = cityNames[position]
        cityMainWeather.text = city.main
    }
}
