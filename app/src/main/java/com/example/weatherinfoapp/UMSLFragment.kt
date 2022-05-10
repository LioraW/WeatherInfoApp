package com.example.weatherinfoapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.weatherinfoapp.databinding.UmslFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UMSLFragment: Fragment(R.layout.umsl_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = UmslFragmentBinding.inflate(inflater, container, false)

        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)

        val call = request.getWeather(getString(R.string.lat),getString(R.string.lon), getString(R.string.api_key), "imperial")

        val tvName = binding.tvHomeFragment
        tvName.text = "UMSL"

        val tvDescription = binding.tvHomeCityName
        val tvTemp = binding.tvCurrentTemp
        val ivIcon = binding.weatherIcon

        val severeWeatherDialog = AlertDialog.Builder(requireContext())
            .setTitle("Severe Weather Alert")
            .setMessage("It is about to rain. You might want to carry an umbrella!")
            .setPositiveButton("Okay"){_, _, ->
                Toast.makeText(this@UMSLFragment.context, "Clicked okay", Toast.LENGTH_LONG).show()

            }
            .create()

        call.enqueue(object: Callback<WeatherInfo>{
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                if (response.isSuccessful){
                    val result = response.body()!!
                    tvDescription.text = "Current Weather: ${result.weather[0].description}"
                    tvTemp.text = "Current Temperature: ${result.main.temp} F"

                    Glide.with(requireContext())
                        .load("http://openweathermap.org/img/wn/${result.weather[0].icon}@2x.png")
                        .into(ivIcon)

                    //show alert if it is raining
                    val weatherCode = result.weather[0].id
//                    val weatherCode = 333 //for testing
                    if (weatherCode in 200..600) { //200-600 is rainy conditions codes
                        severeWeatherDialog.show()
                    }

                } else {
                    Toast.makeText(this@UMSLFragment.context, "Failed: ${response.code()}", Toast.LENGTH_LONG).show()
                    return
                }
            }
            override fun onFailure(call: Call<WeatherInfo>, t: Throwable){
                tvName.text = "Failed: ${t.message}"
                Toast.makeText(this@UMSLFragment.context, "${t.message}", Toast.LENGTH_LONG).show()
                return
            }
        }
        )
        return binding.root
    }


}