package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.hse.moodactivities.R
import org.hse.moodactivities.fragments.ChangeBirthDateFragment
import org.hse.moodactivities.fragments.ChangeNameFragment
import org.hse.moodactivities.fragments.ChangePasswordFragment
import org.hse.moodactivities.services.UserService

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsType = UserService.getSettingsType()
        when (settingsType) {
            UserService.Companion.SettingsType.NAME -> setFragment(ChangeNameFragment())
            UserService.Companion.SettingsType.BIRTH_DAY -> setFragment(ChangeBirthDateFragment())
            UserService.Companion.SettingsType.PASSWORD -> setFragment(ChangePasswordFragment())
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_settings_layout, fragment)
        fragmentTransaction.commit()
    }
}
