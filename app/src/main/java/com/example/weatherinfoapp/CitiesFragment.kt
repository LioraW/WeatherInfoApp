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

        val cally = request.getWeather("london", getString(R.string.api_key), "imperial")
        val calls: MutableList<Call<WeatherInfo>> = arrayListOf()

        for (cityName in resources.getStringArray(R.array.cityNames)) {
            val call = request.getWeather(cityName, getString(R.string.api_key), "imperial")
            calls.add(call)
        }
        calls.add(cally)

        val rvCities = binding.rvCities
        val tvCities = binding.tvCitiesFragment

        val results: MutableList<WeatherInfo> = arrayListOf()

//        call.enqueue(object: Callback<WeatherInfo>{
        calls.forEach { call ->
            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    if (response.isSuccessful) {
                        val result = response.body()!!
                        results.add(result)
                        rvCities.apply {
                            setHasFixedSize(true)
                            Toast.makeText( this@CitiesFragment.context, "in apply $results", Toast.LENGTH_LONG)
                                .show()

                            layoutManager = LinearLayoutManager(this.context)
                            adapter = WeatherAdapter(
                                results,
                                requireContext().resources
                            )
                            binding.rvCities.adapter = adapter
                        }

                    } else {
                        tvCities.text = "Code :" + response.code()
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


//        rvCities.apply {
//            setHasFixedSize(true)
//            Toast.makeText(this@CitiesFragment.context, "in apply $results", Toast.LENGTH_LONG)
//                .show()
//
//            layoutManager = LinearLayoutManager(this.context)
//            adapter = WeatherAdapter(
//                results,
//                requireContext().resources
//            )
//            binding.rvCities.adapter = adapter
//        }



        return binding.root
    }


}