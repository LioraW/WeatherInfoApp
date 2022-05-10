package com.example.weatherinfoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        val umslFragment = UMSLFragment()
        val currentLocationFragment = CurrentLocationActivity()

        setFragment(umslFragment)

        mainViewBinding.bnvBottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.miUMSL -> setFragment(umslFragment)
                R.id.miCurrent -> {
                    val intent = Intent(this, CurrentLocationActivity::class.java)
                    startActivity(intent) //run the maps activity
                }
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