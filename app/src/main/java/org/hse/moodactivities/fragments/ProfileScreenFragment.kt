package org.hse.moodactivities.fragments

import GoogleSignInManager
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
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.FeedbackActivity
import org.hse.moodactivities.activities.SettingsActivity
import org.hse.moodactivities.color_themes.CalmnessColorTheme
import org.hse.moodactivities.color_themes.ColorTheme
import org.hse.moodactivities.color_themes.ColorThemeType
import org.hse.moodactivities.color_themes.EnergeticColorTheme
import org.hse.moodactivities.color_themes.LemonadeColorTheme
import org.hse.moodactivities.models.AuthType
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.services.UserService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import org.hse.moodactivities.viewmodels.UserViewModel

class ProfileScreenFragment : Fragment() {
    companion object {
        const val NOT_CONNECTED = "Not connected."
        const val CONNECTED = "Connected."
        const val NO_DATA = "No data"
    }

    private var colorThemeCardId = R.id.color_theme_forest
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // set on click listeners
        setPersonalInfoWidgetListeners(view)
        setAppThemeWidgetListeners(view)
        setRemindersWidgetListeners(view)
        setAccountInfoWidgetListeners(view)
        setFeedbackWidgetListener(view)

        val colorThemeType = ThemesService.getColorTheme().getColorThemeType()
        pressColorThemeButton(view, getColorThemeCardIdByType(colorThemeType))

        // set light mode
        val lightMode = ThemesService.getLightMode()
        if (lightMode == ColorTheme.LightMode.DAY) {
            view.findViewById<CardView>(R.id.light_mode_background).alpha = BUTTON_ENABLED_ALPHA
            view.findViewById<CardView>(R.id.dark_mode_background).alpha = BUTTON_DISABLED_ALPHA
        } else {
            view.findViewById<CardView>(R.id.light_mode_background).alpha = BUTTON_DISABLED_ALPHA
            view.findViewById<CardView>(R.id.dark_mode_background).alpha = BUTTON_ENABLED_ALPHA
        }

        UserService.uploadUserInfoFromServer()

        setUserData(view)
        setColorTheme(view)

        return view
    }

    private fun getColorThemeCardIdByType(colorThemeType: ColorThemeType): Int {
        return when (colorThemeType) {
            ColorThemeType.CALMNESS -> R.id.color_theme_calmness
            ColorThemeType.LEMONADE -> R.id.color_theme_energetic
            ColorThemeType.FOREST -> R.id.color_theme_forest
            ColorThemeType.ENERGETIC -> R.id.color_theme_lemonade
        }
    }

    private fun setUserData(view: View) {
        var username = UserService.getUsername()
        if (username == null) {
            username = NO_DATA
        }
        view.findViewById<TextView>(R.id.name).text = username

        var birthDate = UserService.getBirthDate()
        if (birthDate == null) {
            birthDate = NO_DATA
        }
        view.findViewById<TextView>(R.id.birth_date).text = birthDate

        var login = UserService.getLogin()
        if (login == null) {
            login = NO_DATA
        }
        view.findViewById<TextView>(R.id.login).text = login

        var email = UserService.getEmail()
        if (email == null) {
            email = NO_DATA
        }
        view.findViewById<TextView>(R.id.email).text = login
    }

    private fun setPersonalInfoWidgetListeners(view: View) {
        view.findViewById<Button>(R.id.name_button).setOnClickListener {
            UserService.setSettingsType(UserService.Companion.SettingsType.NAME)
            val intent = Intent(this.activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.birth_date_button).setOnClickListener {
            UserService.setSettingsType(UserService.Companion.SettingsType.BIRTH_DAY)
            val intent = Intent(this.activity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFeedbackWidgetListener(view: View) {
        view.findViewById<CardView>(R.id.feedback_button_background).setOnClickListener {
            val intent = Intent(this.activity, FeedbackActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pressColorThemeButton(view: View, newColorThemeCardId: Int) {
        view.findViewById<CardView>(colorThemeCardId).alpha = BUTTON_DISABLED_ALPHA
        colorThemeCardId = newColorThemeCardId
        view.findViewById<CardView>(newColorThemeCardId).alpha = BUTTON_ENABLED_ALPHA
    }

    private fun setAppThemeWidgetListeners(view: View) {
        view.findViewById<Button>(R.id.color_theme_button_forest).setOnClickListener {
            pressColorThemeButton(view, R.id.color_theme_forest)
            // todo: set color theme 1
        }

        view.findViewById<Button>(R.id.color_theme_button_calmness).setOnClickListener {
            pressColorThemeButton(view, R.id.color_theme_calmness)
            // todo: set color theme 2
        }

        view.findViewById<Button>(R.id.color_theme_button_lemonade).setOnClickListener {
            pressColorThemeButton(view, R.id.color_theme_lemonade)
            // todo: set color theme 3
        }

        view.findViewById<Button>(R.id.color_theme_button_energetic).setOnClickListener {
            pressColorThemeButton(view, R.id.color_theme_energetic)
            // todo: set color theme 4
        }

        view.findViewById<Button>(R.id.light_mode_button).setOnClickListener {
            view.findViewById<CardView>(R.id.light_mode_background).alpha = BUTTON_ENABLED_ALPHA
            view.findViewById<CardView>(R.id.dark_mode_background).alpha = BUTTON_DISABLED_ALPHA
            ThemesService.setLightMode(ColorTheme.LightMode.DAY)
            setColorTheme(view)
        }

        view.findViewById<Button>(R.id.dark_mode_button).setOnClickListener {
            view.findViewById<CardView>(R.id.dark_mode_background).alpha = BUTTON_ENABLED_ALPHA
            view.findViewById<CardView>(R.id.light_mode_background).alpha = BUTTON_DISABLED_ALPHA
            ThemesService.setLightMode(ColorTheme.LightMode.NIGHT)
            setColorTheme(view)
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
        view.findViewById<Button>(R.id.password_button).setOnClickListener {
            UserService.setSettingsType(UserService.Companion.SettingsType.PASSWORD)
            val intent = Intent(this.activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        if (GoogleSignIn.getLastSignedInAccount(requireContext()) == null) {
            view.findViewById<Button>(R.id.google_button).setOnClickListener {
                performGoogleSignIn()
            }
            view.findViewById<TextView>(R.id.google_text).text = NOT_CONNECTED
        } else {
            // Only allow Google logout on PLAIN accounts!
            if (userViewModel.getUser(requireContext())!!.authType == AuthType.PLAIN) {
                view.findViewById<Button>(R.id.google_button).setOnClickListener {
                    performGoogleLogOut()
                }
            }
            view.findViewById<TextView>(R.id.google_text).text = CONNECTED
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleSignInManager.RETURN_CODE_SIGN_IN) {
            // Reload current fragment
            val ft = requireFragmentManager().beginTransaction()
            ft.detach(this).attach(this).commit()
        }
    }
    private fun performGoogleSignIn() {
        val signInIntent = GoogleSignInManager.getSignInIntent()
        startActivityForResult(signInIntent, GoogleSignInManager.RETURN_CODE_SIGN_IN)
    }

    private fun performGoogleLogOut() {
        GoogleSignInManager.signOut(requireContext()) {
            // Reload current fragment
            val ft = requireFragmentManager().beginTransaction()
            ft.detach(this).attach(this).commit()
        }
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        activity?.window?.statusBarColor = colorTheme.getBackgroundColor()

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

        view.findViewById<TextView>(R.id.forest)
            .setTextColor(colorTheme.getColorThemeColor())
        view.findViewById<CardView>(R.id.color_theme_forest)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetTitleColor())

        view.findViewById<TextView>(R.id.calmness)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.color_theme_lemonade)
            .setCardBackgroundColor(CalmnessColorTheme.getColorThemeColor())

        view.findViewById<TextView>(R.id.lemonade)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.color_theme_lemonade)
            .setCardBackgroundColor(LemonadeColorTheme.getColorThemeColor())

        view.findViewById<TextView>(R.id.energetic)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.color_theme_energetic)
            .setCardBackgroundColor(EnergeticColorTheme.getColorThemeColor())

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
        view.findViewById<TextView>(R.id.google_text)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.google_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<CardView>(R.id.feedback_button_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
        view.findViewById<TextView>(R.id.feedback_title)
            .setTextColor(colorTheme.getButtonTextColor())
    }
}
