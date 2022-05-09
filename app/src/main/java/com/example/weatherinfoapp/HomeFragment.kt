package com.example.weatherinfoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.HomeFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment: Fragment(R.layout.home_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cityName = "St. Louis"
        val binding = HomeFragmentBinding.inflate(inflater, container, false)

        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)

        val call = request.getWeather(getString(R.string.lat),getString(R.string.lon), getString(R.string.api_key), "imperial")

//        val call = request.getWeather(cityName, getString(R.string.api_key), "imperial")

        val tvName = binding.tvHomeFragment
        val tvDescription = binding.tvHomeCityName
        val tvTemp = binding.tvCurrentTemp

        call.enqueue(object: Callback<WeatherInfo>{
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                if (response.isSuccessful){
                    val result = response.body()!!

                    tvName.text = result.name
                    tvDescription.text = "Current Weather: ${result.weather[0].description}"
                    tvTemp.text = "Current Temperature: ${result.main.temp} F"

                } else {
                    tvName.text = "Code :" + response.code()
                    return
                }
            }
            override fun onFailure(call: Call<WeatherInfo>, t: Throwable){
                tvName.text = "Failed: ${t.message}"
                Toast.makeText(this@HomeFragment.context, "${t.message}", Toast.LENGTH_LONG).show()
                return
            }
        }
        )
        return binding.root
    }


}