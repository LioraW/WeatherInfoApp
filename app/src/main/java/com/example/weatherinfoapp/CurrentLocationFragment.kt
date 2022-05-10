package com.example.weatherinfoapp


import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import com.example.weatherinfoapp.databinding.CurrentLocationFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrentLocationFragment : Fragment(R.layout.current_location_fragment) {

    //added
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient


    //needed

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

        val binding = CurrentLocationFragmentBinding.inflate(inflater, container, false)


        val tvName = binding.tvLondonName
        val tvDescription = binding.tvLondonDescription
        val tvTemp = binding.tvLondonCurrentTemp
        val btnMaps = binding.button

//        // Build the map.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(this)

        //button to open maps
        btnMaps.setOnClickListener(){
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent) //run the maps activity

        }

        getLocationsPermissions()

        try{
            if (locationPermissionGranted){
                fusedLocationProviderClient.lastLocation //last location, needs someone else to request locaiton
                    .addOnSuccessListener { location : Location? ->

                        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)
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
                                Toast.makeText(this@CurrentLocationFragment.context, "${t.message}", Toast.LENGTH_LONG).show()
                                return
                            }
                        }
                        )
                    }
            }

        }  catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }


        return binding.root
    }

    private fun getLocationsPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
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
        locationPermissionGranted = true
    }

// these two functions are based on https://stackoverflow.com/questions/36512349/how-do-i-get-my-last-known-location-in-android/62761897#62761897
    private fun getCurrentLocation() {
        getLocationsPermissions()
        try {
            if (locationPermissionGranted){
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        Toast.makeText(requireContext(), "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}", Toast.LENGTH_SHORT).show()

                    }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
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