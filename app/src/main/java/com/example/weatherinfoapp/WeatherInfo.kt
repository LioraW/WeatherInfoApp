package com.example.weatherinfoapp

data class WeatherInfo(
    val weather: List<WeatherResults>,
    val main: Main,
    val name: String

)

data class WeatherResults(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double,
)
