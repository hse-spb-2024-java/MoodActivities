package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.FeedbackActivity
import org.hse.moodactivities.activities.SettingsActivity
import org.hse.moodactivities.color_themes.ColorTheme
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.services.UserService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA

class ProfileScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)

        // set on click listeners
        setPersonalInfoWidgetListeners(view)
        setAppThemeWidgetListeners(view)
        setRemindersWidgetListeners(view)
        setAccountInfoWidgetListeners(view)
        setFeedbackWidgetListener(view)

        setUserData(view)
        setColorTheme(view)

        return view
    }

    private fun setUserData(view: View) {
        // todo: get user name if available
        val name = "Kristina"
        view.findViewById<TextView>(R.id.name).text = name

        // todo: get birth date if available
        val birthDate = "12/12/2003"
        view.findViewById<TextView>(R.id.birth_date).text = birthDate

        // todo: get birth date if available
        val login = "f"
        view.findViewById<TextView>(R.id.login).text = login
    }

    private fun setPersonalInfoWidgetListeners(view: View) {
        view.findViewById<Button>(R.id.name_button).setOnClickListener {
            UserService.setSettingsType(UserService.Companion.SettingsType.NAME)
            val intent = Intent(this.activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.birth_date_button).setOnClickListener {
            // todo: open fragment to change birth date
        }
    }

    private fun setFeedbackWidgetListener(view: View) {
        view.findViewById<CardView>(R.id.feedback_button_background).setOnClickListener {
            val intent = Intent(this.activity, FeedbackActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAppThemeWidgetListeners(view: View) {
        view.findViewById<Button>(R.id.color_theme_button_1).setOnClickListener {
            // todo: set color theme 1
        }

        view.findViewById<Button>(R.id.color_theme_button_1).setOnClickListener {
            // todo: set color theme 2
        }

        view.findViewById<Button>(R.id.color_theme_button_1).setOnClickListener {
            // todo: set color theme 3
        }

        view.findViewById<Button>(R.id.color_theme_button_1).setOnClickListener {
            // todo: set color theme 4
        }

        view.findViewById<Button>(R.id.light_mode_button).setOnClickListener {
            view.findViewById<CardView>(R.id.light_mode_background).alpha = BUTTON_ENABLED_ALPHA
            view.findViewById<CardView>(R.id.dark_mode_background).alpha = BUTTON_DISABLED_ALPHA
            ThemesService.changeLightMode(ColorTheme.LightMode.DAY)
        }

        view.findViewById<Button>(R.id.dark_mode_button).setOnClickListener {
            view.findViewById<CardView>(R.id.dark_mode_background).alpha = BUTTON_ENABLED_ALPHA
            view.findViewById<CardView>(R.id.light_mode_background).alpha = BUTTON_DISABLED_ALPHA
            ThemesService.changeLightMode(ColorTheme.LightMode.NIGHT)
        }
    }

    private fun setRemindersWidgetListeners(view: View) {
        view.findViewById<Button>(R.id.on_button).setOnClickListener {
            // todo: do something
            view.findViewById<CardView>(R.id.on_background).alpha = BUTTON_ENABLED_ALPHA
            view.findViewById<CardView>(R.id.off_background).alpha = BUTTON_DISABLED_ALPHA
        }

        view.findViewById<Button>(R.id.off_button).setOnClickListener {
            // todo: do something
            view.findViewById<CardView>(R.id.off_background).alpha = BUTTON_ENABLED_ALPHA
            view.findViewById<CardView>(R.id.on_background).alpha = BUTTON_DISABLED_ALPHA
        }
    }

    private fun setAccountInfoWidgetListeners(view: View) {
        view.findViewById<Button>(R.id.email_button).setOnClickListener {
            // todo: open fragment to change email
        }

        view.findViewById<Button>(R.id.password_button).setOnClickListener {
            // todo: open fragment to change password
        }

        view.findViewById<Button>(R.id.google_button).setOnClickListener {
            // todo: open fragment to change google
        }
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        // set color to background
        view.findViewById<ConstraintLayout>(R.id.layout)
            .setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to title
        view.findViewById<TextView>(R.id.title).setTextColor(colorTheme.getFontColor())

        // set color to user greeting
        view.findViewById<TextView>(R.id.hello).setTextColor(colorTheme.getFontColor())
        view.findViewById<TextView>(R.id.username).setTextColor(
            colorTheme.getFontColor()
        )

        // set color to personal info widget
        view.findViewById<TextView>(R.id.personal_info_title)
            .setTextColor(colorTheme.getFontColor())
        view.findViewById<CardView>(R.id.personal_info_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetColor())

        view.findViewById<TextView>(R.id.name_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<TextView>(R.id.name)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.name_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        view.findViewById<ImageView>(R.id.name_image)
            .setColorFilter(colorTheme.getSettingsWidgetTitleColor())

        view.findViewById<TextView>(R.id.birth_date_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<TextView>(R.id.birth_date)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.birth_date_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        view.findViewById<ImageView>(R.id.birth_date_image)
            .setColorFilter(colorTheme.getSettingsWidgetTitleColor())

        // set colors to app theme widget
        view.findViewById<TextView>(R.id.app_theme_title).setTextColor(colorTheme.getFontColor())
        view.findViewById<CardView>(R.id.app_theme_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetColor())

        view.findViewById<TextView>(R.id.color_theme_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())

        view.findViewById<TextView>(R.id.mode_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())

        view.findViewById<TextView>(R.id.light_mode_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.light_mode_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<TextView>(R.id.dark_mode_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.dark_mode_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        // set colors to reminders widget
        view.findViewById<TextView>(R.id.reminders_title).setTextColor(colorTheme.getFontColor())
        view.findViewById<CardView>(R.id.reminders_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetColor())

        view.findViewById<TextView>(R.id.on_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.on_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<TextView>(R.id.off_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.off_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        // set colors to account info widget
        view.findViewById<TextView>(R.id.account_info_title).setTextColor(colorTheme.getFontColor())
        view.findViewById<CardView>(R.id.account_info_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetColor())

        view.findViewById<TextView>(R.id.login_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<TextView>(R.id.login)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.login_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<TextView>(R.id.email_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<TextView>(R.id.email)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.email_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        view.findViewById<ImageView>(R.id.email_image)
            .setColorFilter(colorTheme.getSettingsWidgetTitleColor())

        view.findViewById<TextView>(R.id.password_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<TextView>(R.id.password)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.password_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        view.findViewById<ImageView>(R.id.password_image)
            .setColorFilter(colorTheme.getSettingsWidgetTitleColor())

        view.findViewById<TextView>(R.id.google_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<TextView>(R.id.google)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.google_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<CardView>(R.id.feedback_button_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
        view.findViewById<TextView>(R.id.feedback_title)
            .setTextColor(colorTheme.getButtonTextColor())
    }
}
