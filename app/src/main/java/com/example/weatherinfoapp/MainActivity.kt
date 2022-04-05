package com.example.weatherinfoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenuView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root) //mainViewBinding.root

        val homeFragment = HomeFragment()
        val citiesFragment = CitiesFragment()
        val alertsFragment = AlertsFragment()

        setFragment(homeFragment)

        mainViewBinding.bnvBottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.miHome -> setFragment(homeFragment)
                R.id.miCities -> setFragment(citiesFragment)
                R.id.miAlerts -> setFragment(alertsFragment)
            }
            true //return statement to true (its a lambda)
        }

        mainViewBinding.bnvBottomNavigationView.getOrCreateBadge(R.id.miAlerts).apply {
            number = 5
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