package com.example.weatherinfoapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.ActivityMainBinding
import android.widget.Toast

import android.location.LocationManager
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        val homeFragment = HomeFragment()
        val londonFragment = LondonFragment()
        val chicagoFragment = ChicagoFragment()

        setFragment(homeFragment)

        mainViewBinding.bnvBottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.miHome -> setFragment(homeFragment)
                R.id.miLondon -> setFragment(londonFragment)
                R.id.miChicago -> setFragment(chicagoFragment)
            }
            true //return statement to true (its a lambda)
        }

    }

    //accepts a fragment and replaces the previous fragment with the new one
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }




}