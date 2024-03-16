package org.hse.moodactivities.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.hse.moodactivities.fragments.HistoryScreenFragment
import org.hse.moodactivities.fragments.HomeScreenFragment
import org.hse.moodactivities.fragments.InsightsScreenFragment
import org.hse.moodactivities.fragments.ProfileScreenFragment
import org.hse.moodactivities.R
import org.hse.moodactivities.databinding.ActivityMainBinding
import org.hse.moodactivities.fragments.ChatFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeScreenFragment())
        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_menu_home -> replaceFragment(HomeScreenFragment())
                R.id.bottom_menu_history -> replaceFragment(HistoryScreenFragment())
                R.id.bottom_menu_add -> replaceFragment(ChatFragment())
                R.id.bottom_menu_insights -> replaceFragment(InsightsScreenFragment())
                R.id.bottom_menu_profile -> replaceFragment(ProfileScreenFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
