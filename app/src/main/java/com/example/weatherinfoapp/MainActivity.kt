package com.example.weatherinfoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        val umslFragment = UMSLFragment()
        val currentLocationFragment = CurrentLocationFragment()

        setFragment(currentLocationFragment)

        mainViewBinding.bnvBottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.miUMSL -> setFragment(umslFragment)
                R.id.miCurrent -> setFragment(currentLocationFragment)
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