package com.example.weatherinfoapp


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.LondonFragmentBinding
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.net.PlacesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Array.getDouble


class LondonFragment : Fragment(R.layout.london_fragment){

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSIONS_REQUEST_LOCATION = 1
    private val defaultLocation = LatLng(38.7092229, -90.310511)
    private var locationPermissionGranted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val binding = LondonFragmentBinding.inflate(inflater, container, false)
        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)


        val tvName = binding.tvLondonName
        val tvDescription = binding.tvLondonDescription
        val tvTemp = binding.tvLondonCurrentTemp
        val btnAct2 = binding.button

        btnAct2.setOnClickListener(){
            Toast.makeText(requireContext(), "Welcome", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MapsActivity::class.java) //from this package to activity 2, we want to call this acitivity
            startActivity(intent) //run the activity 2

        }


        //getCurrentLocation()
        if ( ActivityCompat.checkSelfPermission( requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission( requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) { //if permissions are not granted, ask for them
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS_REQUEST_LOCATION
            )
        }

        //then use the locationProvider
        fusedLocationProviderClient.lastLocation //last location, needs someone else to request locaiton
            .addOnSuccessListener { location : Location? ->

                val call = request.getWeather("${location?.latitude}","${location?.longitude}", getString(R.string.api_key), "imperial")

                //make call
                call.enqueue(object: Callback<WeatherInfo> {
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
                        Toast.makeText(this@LondonFragment.context, "${t.message}", Toast.LENGTH_LONG).show()
                        return
                    }
                }
                )

            }

        return binding.root
    }


    //
//    //these two functions are based on https://stackoverflow.com/questions/36512349/how-do-i-get-my-last-known-location-in-android/62761897#62761897
    private fun getCurrentLocation() {

        if ( ActivityCompat.checkSelfPermission( requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission( requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
        ) { //if permissions are not granted, ask for them
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS_REQUEST_LOCATION
            )
            return
        }

        //then use the locationProvider
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Toast.makeText(requireContext(), "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}", Toast.LENGTH_SHORT).show()


            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "You need to allow the permissions for Location", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }


}