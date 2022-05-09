package com.example.weatherinfoapp


import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.LondonFragmentBinding
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices



class LondonFragment : Fragment(R.layout.london_fragment) {
    data class LocationCoordinates(var lat: Double?, var lon: Double?)

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSIONS_REQUEST_LOCATION = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val binding = LondonFragmentBinding.inflate(inflater, container, false)



        val tvName = binding.tvLondonName
        val tvDescription = binding.tvLondonDescription
        val tvTemp = binding.tvLondonCurrentTemp

        //getCurrentLocation() //modifies lc dataclass instance
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
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Toast.makeText(requireContext(), "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}", Toast.LENGTH_SHORT).show()

                tvName.text = " Current Latitude${location?.latitude}"
                tvDescription.text = "Current Longitude: ${location?.longitude}"
//          tvTemp.text = "Current Tempurature: ${result.main.temp} F"

            }



        return binding.root
    }


    //these two functions are based on https://stackoverflow.com/questions/36512349/how-do-i-get-my-last-known-location-in-android/62761897#62761897
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