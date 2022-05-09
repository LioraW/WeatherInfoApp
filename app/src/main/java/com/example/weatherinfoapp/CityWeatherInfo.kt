package com.example.weatherinfoapp

import android.content.Context
import android.content.res.Resources
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityWeatherInfo(private val lat: String, private val lon: String,val context: Context?){

    fun getInfo(): Response<WeatherInfo>? {

        var resp : Response<WeatherInfo>? = null

        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)

        val call = request.getWeather(lat, lon, R.string.api_key.toString(), "imperial")

        call.enqueue(object: Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                if (response.isSuccessful){
                    resp =  response
                } else {
                    resp = response
                    return
                }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable){
                //failure
                return
            }
        }
        )
        return resp
    }
}