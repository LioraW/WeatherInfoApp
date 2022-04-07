package com.example.weatherinfoapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndpoints {
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("q") location: String,
        @Query("appid") key: String,
        @Query("units") units: String
    ): Call<WeatherInfo>
}