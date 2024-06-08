package org.hse.moodactivities.activities

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.hse.moodactivities.R
import org.hse.moodactivities.databinding.MainScreenBinding
import org.hse.moodactivities.fragments.CalendarFragment
import org.hse.moodactivities.fragments.HomeFragment
import org.hse.moodactivities.fragments.InsightsScreenFragment
import org.hse.moodactivities.fragments.ProfileScreenFragment
import org.hse.moodactivities.services.ThemesService


class MainScreenActivity : AppCompatActivity() {
    private lateinit var binding: MainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_menu_home -> replaceFragment(HomeFragment())
                R.id.bottom_menu_history -> replaceFragment(CalendarFragment())
                R.id.bottom_menu_insights -> replaceFragment(InsightsScreenFragment())
                R.id.bottom_menu_profile -> replaceFragment(ProfileScreenFragment())
            }
            true
        }

        setColorTheme()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun setColorTheme() {
        // set color to status bar
        window.statusBarColor = ThemesService.getBackgroundColor()

        // set color to background
        binding.mainScreenLayout.setBackgroundColor(ThemesService.getBackgroundColor())

        // set color to
        window.navigationBarColor = ThemesService.getDimmedColor6()
        binding.bottomNavigationView.setBackgroundColor(ThemesService.getDimmedColor6())
        binding.bottomNavigationView.itemTextColor =
            ColorStateList.valueOf(ThemesService.getColor6())
        binding.bottomNavigationView.itemIconTintList =
            ColorStateList.valueOf(ThemesService.getColor6())
    }
}
