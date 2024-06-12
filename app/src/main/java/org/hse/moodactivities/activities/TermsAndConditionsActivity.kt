package org.hse.moodactivities.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.hse.moodactivities.databinding.ActivityTermsAndConditionsBinding
import org.hse.moodactivities.services.ThemesService


class TermsAndConditionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsAndConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // todo: set terms and conditions
        // setting text to terms and conditions

        setColorTheme()
    }

    override fun finish() {
        super.finish()
    }

    private fun setColorTheme() {
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        window.statusBarColor = colorTheme.getBackgroundColor()

        // set color to background
        binding.layout.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to tittle
        binding.title.setTextColor(colorTheme.getFontColor())

        // set color to terms and conditions
        binding.termsAndConditions.setTextColor(colorTheme.getTermsAndConditionsFontColor())

        binding.returnButton.setColorFilter(colorTheme.getFontColor())
    }

    fun returnToPreviousActivity(view: View) {
        this.finish()
    }
}