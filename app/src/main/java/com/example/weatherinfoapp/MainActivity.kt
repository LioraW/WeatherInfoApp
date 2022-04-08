package com.example.weatherinfoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.weatherinfoapp.databinding.ActivityMainBinding


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