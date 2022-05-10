package com.example.weatherinfoapp


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.libraries.places.api.net.PlacesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentLocationActivity :  AppCompatActivity(R.layout.activity_current_location), OnMapReadyCallback {

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSIONS_REQUEST_LOCATION = 1
    private val defaultLocation = LatLng(38.7092229, -90.310511)
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //views
        val tvName = findViewById<TextView>(R.id.tvLondonName)
        val tvDescription = findViewById<TextView>(R.id.tvLondonDescription)
        val tvTemp = findViewById<TextView>(R.id.tvLondonCurrentTemp)

        // Build the map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        //set the back button
        val btnBack = findViewById<Button>(R.id.backButton)
        btnBack.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) //run the maps activity

        }

        getLocationPermission()

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
                                Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_LONG).show()
                                return
                            }
                        }
                        )
                    }
            }

        }  catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(CurrentLocationActivity.KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(CurrentLocationActivity.KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private val TAG = CurrentLocationActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

    }
}