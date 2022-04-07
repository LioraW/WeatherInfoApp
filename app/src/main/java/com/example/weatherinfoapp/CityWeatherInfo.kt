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

class CityWeatherInfo(location: String, con: Context?){

    var isSuccessful = false
    var responseCode: Int = 0
    val context = con

    fun getInfo(): WeatherInfo? {

        var wi : WeatherInfo? = null

        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)

        val call = request.getWeather("London", R.string.api_key.toString(), "imperial")

        call.enqueue(object: Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                Log.d("umsl", "onResponse:succcessful ")
                if (response.isSuccessful){
                    wi = response.body()!!
                    isSuccessful = true
                } else {
                    Log.d("umsl", "onResponse:error ")
                    responseCode = response.code()
                    return
                }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable){
                //failure
//                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show()
                return
            }
        }
        )
        return wi
    }
}