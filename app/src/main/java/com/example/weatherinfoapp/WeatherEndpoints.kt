package com.example.weatherinfoapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndpoints {
    //    @GET("/lat=35&lon=139&appid=${R.string.api_key}")
    @GET("data/2.5/weather")
    fun getWeather(@Query("q") location: String, @Query("appid") key: String): Call<WeatherInfo>
}