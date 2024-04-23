package org.hse.moodactivities.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hse.moodactivities.R
import org.hse.moodactivities.databinding.MainScreenBinding
import org.hse.moodactivities.fragments.HistoryScreenFragment
import org.hse.moodactivities.fragments.HomeScreenFragment
import org.hse.moodactivities.fragments.InsightsScreenFragment
import org.hse.moodactivities.fragments.ProfileScreenFragment

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: MainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeScreenFragment())
        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_menu_home -> replaceFragment(HomeScreenFragment())
                R.id.bottom_menu_history -> replaceFragment(HistoryScreenFragment())
                R.id.bottom_menu_insights -> replaceFragment(InsightsScreenFragment())
                R.id.bottom_menu_profile -> replaceFragment(ProfileScreenFragment())
            }
            true
        }

        findViewById<FloatingActionButton>(R.id.chat_button).setOnClickListener {
            val chatActivity = Intent(this, ChatActivity::class.java)
            startActivity(chatActivity)
            this.finish()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
