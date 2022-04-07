package com.example.weatherinfoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherinfoapp.databinding.CitiesFragmentBinding
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class CitiesFragment : Fragment(R.layout.cities_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CitiesFragmentBinding.inflate(inflater, container, false)

        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)

        val cally = request.getWeather("Orlando", getString(R.string.api_key), "imperial")
        val calls: MutableList<Call<WeatherInfo>> = arrayListOf(cally)

        for (cityName in resources.getStringArray(R.array.cityNames)) {
            calls.add(request.getWeather(cityName, getString(R.string.api_key), "imperial"))
        }

        val rvCities = binding.rvCities
        val tvCities = binding.tvCitiesFragment


//        call.enqueue(object: Callback<WeatherInfo>{
        calls.forEach { call ->
            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    if (response.isSuccessful) {

                        rvCities.apply {
                            setHasFixedSize(true)

                            layoutManager = LinearLayoutManager(this.context)
                            adapter = WeatherAdapter(
                                response.body()!!.weather,
                                requireContext().resources
                            )

                            binding.rvCities.adapter = adapter

                        }
                    } else {
                        tvCities.text = "Code :" + response.code()
                        return
                    }
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    tvCities.text = "Failed: ${t.message}"
                    Toast.makeText(this@CitiesFragment.context, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                    return
                }
            }
            )
        }



        return binding.root
    }


}