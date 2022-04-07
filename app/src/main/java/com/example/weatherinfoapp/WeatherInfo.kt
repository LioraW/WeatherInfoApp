package com.example.weatherinfoapp

data class WeatherInfo(
    val weather: List<WeatherResults>

)

data class WeatherResults(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
