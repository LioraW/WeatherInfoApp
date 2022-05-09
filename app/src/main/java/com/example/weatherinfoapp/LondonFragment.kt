package com.example.weatherinfoapp

import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.LondonFragmentBinding
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import androidx.core.content.ContextCompat.getSystemService

import android.location.LocationManager
import android.location.LocationRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.example.weatherinfoapp.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LondonFragment : Fragment(R.layout.london_fragment) {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSIONS_REQUEST_LOCATION = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        getCurrentLocation()
        val cityName = "London"

        val binding = LondonFragmentBinding.inflate(inflater, container, false)

        val request = ServiceBuilder.buildService(WeatherEndpoints::class.java)

        val call = request.getWeather(getString(R.string.lat),getString(R.string.lon), getString(R.string.api_key), "imperial")


        val tvName = binding.tvLondonName
        val tvDescription = binding.tvLondonDescription
        val tvTemp = binding.tvLondonCurrentTemp


            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    if (response.isSuccessful) {
                        val result = response.body()!!
                        tvName.text = result.name
                        tvDescription.text = "Current Weather: ${result.weather[0].main}"
                        tvTemp.text = "Current Tempurature: ${result.main.temp} F"

                    } else {
                        tvName.text = "Code: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    tvName.text = "Failed: ${t.message}"
                    Toast.makeText(this@LondonFragment.context, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                    return
                }
            }
            )


        return binding.root
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
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




//    private fun getLocation(locationManager: LocationManager) {
//        ActivityCompat.requestPermissions(requireActivity(),
//            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1);
//        if (isLocationPermissionGranted()) {
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1);
//        } else {
////            val locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            val locationGPS = locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER);
//
//            if (locationGPS != null) {
//                val lat = locationGPS.getLatitude();
//                val  long = locationGPS.getLongitude();
//                tvDescription = lat
//                longitude = long
//                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
//            } else {
//                Toast.makeText(this@LondonFragment.context, "cannot find locaiton", Toast.LENGTH_LONG)
//                    .show()
//            }
//        }


//    }
//    private fun isLocationPermissionGranted(): Boolean {
//        return if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ), 0
////                1
//            )
//            false
//        } else {
//            true
//        }
//    }


}