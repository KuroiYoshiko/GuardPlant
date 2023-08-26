package com.kuroi.guardplant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.kuroi.guardplant.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        replaceFragment(CalendarFragment())

        binding.bottomNavView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.bottomMenu_calendar -> replaceFragment(CalendarFragment())
                R.id.bottomMenu_diagnose -> replaceFragment(DiagnoseFragment())
                R.id.bottomMenu_reminders -> replaceFragment(RemindersFragment())
                R.id.bottomMenu_myPlants -> replaceFragment(CalendarFragment())
                else -> replaceFragment(CalendarFragment())
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}