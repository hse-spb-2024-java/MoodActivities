package org.hse.moodactivities.activities

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
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

        // setting text to terms and conditions
        setTermsAndConditions()

        setColorTheme()
    }

    override fun finish() {
        super.finish()
    }

    private fun createBoldText(text: String): SpannableString {
        val result = SpannableString(text)
        result.setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return result
    }

    @SuppressLint("SetTextI18n")
    private fun setTermsAndConditions() {
        val termsAndConditions = SpannableStringBuilder()

        // introduction terms
        termsAndConditions.append(createBoldText("1. Introduction\n"))
        termsAndConditions.append(
            "Welcome to MoodActivities («we», «our», «us»). These Terms and Conditions" +
                    " («Terms») govern your use of our mood tracking and assessment application " +
                    "(«App»), available through the official repository. By using our App, you " +
                    "agree to these Terms. If you do not agree, please do not use the App.\n"
        )

        // user data terms
        termsAndConditions.append(createBoldText("\n2. User data\n"))
        termsAndConditions.append(
            "○ You can share your day-to-day events, answer questions, and allow the App " +
                    "access to your geolocation and Google Fit statistics.\n"
        )
        termsAndConditions.append(
            "○ All data shared with the App is subject to our Privacy Policy. Please review it " +
                    "to understand how we collect, use, and protect your data.\n"
        )

        // non-medical advice terms
        termsAndConditions.append(createBoldText("\n3. Non-medical advice\n"))
        termsAndConditions.append(
            "○ The advice, recommendations, and activities provided by our App are for " +
                    "informational purposes only and should not be considered medical or " +
                    "psychological advice.\n"
        )
        termsAndConditions.append(
            "○ The App does not substitute for professional medical advice, diagnosis, or " +
                    "treatment. Always seek the advice of a qualified professional with any " +
                    "questions regarding a medical or psychological condition.\n"
        )

        // user responsibilities
        termsAndConditions.append(createBoldText("\n4. User responsibilities\n"))
        termsAndConditions.append(
            "○ You agree to use the App in a lawful manner and not for any illegal or " +
                    "unauthorized purpose.\n"
        )
        termsAndConditions.append(
            "○ You are responsible for the accuracy of the information you provide to the App.\n"
        )
        termsAndConditions.append(
            "○ You must not share your account with others or use the App to collect information " +
                    "about others without their consent.\n"
        )

        // access and use
        termsAndConditions.append(createBoldText("\n5. Access and use\n"))
        termsAndConditions.append(
            "○ We reserve the right to modify, suspend, or discontinue the App, or any part " +
                    "thereof, at any time without prior notice.\n"
        )
        termsAndConditions.append(
            "○ We are not responsible for any data loss or interruptions in service.\n"
        )

        // intellectual property
        termsAndConditions.append(createBoldText("\n6. Intellectual property"))
        termsAndConditions.append(
            "○ All intellectual property rights in the App, including but not limited to text, " +
                    "graphics, user interface, and logos, are owned by or licensed to us.\n"
        )
        termsAndConditions.append(
            "○ You may not copy, modify, distribute, sell, or lease any part of our App or " +
                    "included software.\n"
        )

        // limitation of liability
        termsAndConditions.append(createBoldText("\n7. Limitation of liability\n"))
        termsAndConditions.append(
            "○ To the fullest extent permitted by applicable law, we shall not be liable for any " +
                    "indirect, incidental, special, consequential, or punitive damages, or any " +
                    "loss of profits or revenues, whether incurred directly or indirectly, or " +
                    "any loss of data, use, goodwill, or other intangible losses.\n"
        )

        // changes to terms
        termsAndConditions.append(createBoldText("\n8. Changes to Еerms\n"))
        termsAndConditions.append(
            "○ We may revise these Terms at any time by posting an updated version. Your " +
                    "continued use of the App after any changes means you accept such changes.\n"
        )

        // governing law
        termsAndConditions.append(createBoldText("\n9. Governing law\n"))
        termsAndConditions.append(
            "○ These Terms are governed by and construed in accordance with the laws of Russia. " +
                    "Any disputes arising from these Terms or the App will be resolved in the " +
                    "courts of Russia.\n"
        )

        // contact us
        termsAndConditions.append(createBoldText("\n10. Contact us\n"))
        termsAndConditions.append(
            "○ If you have any questions about these Terms, please contact us at " +
                    "moodactivities@gmail.com.\n"
        )

        termsAndConditions.append(
            "\nBy using our App, you acknowledge that you have read, understood, and agree to be " +
                    "bound by these Terms.\n"
        )

        binding.termsAndConditions.text = termsAndConditions

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
