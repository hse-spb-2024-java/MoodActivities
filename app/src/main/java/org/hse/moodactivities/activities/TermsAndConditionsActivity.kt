package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.hse.moodactivities.databinding.ActivityTermsAndConditionsBinding
import org.hse.moodactivities.services.ThemesService

class TermsAndConditionsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTermsAndConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // todo: set terms and conditions
        // setting text to terms and conditions
    }

    private fun setColorTheme() {
        // set color to status bar
        window.statusBarColor = ThemesService.getBackgroundColor()

        // set color to background
        binding.layout.setBackgroundColor(ThemesService.getBackgroundColor())

        // set color to tittle
        binding.tittle.setTextColor(ThemesService.getFontColor())

        // set color to terms and conditions
        binding.tittle.setTextColor(ThemesService.getColor6())
    }
}
