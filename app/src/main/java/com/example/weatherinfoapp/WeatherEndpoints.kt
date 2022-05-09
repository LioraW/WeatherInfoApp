package com.example.weatherinfoapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndpoints {
    //38.7092229,-90.310511

    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

    @GET("data/2.5/weather")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") key: String,
        @Query("units") units: String
    ): Call<WeatherInfo>

}