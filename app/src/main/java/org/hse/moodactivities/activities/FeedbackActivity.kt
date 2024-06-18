package org.hse.moodactivities.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.hse.moodactivities.databinding.ActivityFeedbackBinding
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import org.hse.moodactivities.utils.showCustomToast

class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnButton.setOnClickListener {
            this.finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.positiveFeedback.text.isEmpty() && binding.negativeFeedback.text.isEmpty()) {
                    binding.sendButtonBackground.alpha = BUTTON_DISABLED_ALPHA
                } else {
                    binding.sendButtonBackground.alpha = BUTTON_ENABLED_ALPHA
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        }

        binding.sendButton.setOnClickListener {
            // todo: send feedback

            binding.positiveFeedback.text.clear()
            binding.negativeFeedback.text.clear()
            Toast(this).showCustomToast("Your feedback has been sent", this)
        }

        binding.positiveFeedback.addTextChangedListener(textWatcher)
        binding.negativeFeedback.addTextChangedListener(textWatcher)

        setColorTheme()
    }

    private fun setColorTheme() {
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        window.statusBarColor = colorTheme.getBackgroundColor()

        // set color to background
        binding.layout.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to title
        binding.title.setTextColor(colorTheme.getFontColor())
        binding.intro.setTextColor(colorTheme.getFontColor())

        // set color to return image
        binding.returnImage.setColorFilter(colorTheme.getFontColor())

        // set color to questions
        binding.positiveFeedbackQuestion.setTextColor(colorTheme.getFontColor())
        binding.positiveFeedbackBackground.setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        binding.positiveFeedback.setTextColor(colorTheme.getSettingsWidgetTitleColor())

        binding.negativeFeedbackQuestion.setTextColor(colorTheme.getFontColor())
        binding.negativeFeedbackBackground.setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        binding.negativeFeedback.setTextColor(colorTheme.getSettingsWidgetTitleColor())

        // set color to button
        binding.sendButtonBackground.setCardBackgroundColor(colorTheme.getButtonColor())
        binding.buttonText.setTextColor(colorTheme.getButtonTextColor())
    }
}
