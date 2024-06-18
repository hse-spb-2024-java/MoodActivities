package org.hse.moodactivities.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.services.UserService
import org.hse.moodactivities.utils.showCustomToast

class ChangePasswordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        // button to profile
        view.findViewById<Button>(R.id.return_button).setOnClickListener {
            this.activity?.finish()
        }

        view.findViewById<CardView>(R.id.button_background).setOnClickListener {
            validatePasswordChange(view)
        }

        setColorTheme(view)

        return view
    }

    private fun validatePasswordChange(view: View) {
        val oldPassword = view.findViewById<EditText>(R.id.old_password).text.toString()

        if (UserService.checkOldPassword(oldPassword)) {
            Toast(this.activity as Activity).showCustomToast(
                "Incorrect old password", this.activity as Activity
            )
        }

        val newPassword = view.findViewById<EditText>(R.id.new_password).text.toString()
        val newPasswordConfirm =
            view.findViewById<EditText>(R.id.confirm_new_password).text.toString()

        if (newPassword != newPasswordConfirm) {
            Toast(this.activity as Activity).showCustomToast(
                "New passwords don't match", this.activity as Activity
            )
            return
        }

        if (oldPassword != newPassword) {
            Toast(this.activity as Activity).showCustomToast(
                "The old and new passwords are the same", this.activity as Activity
            )
            return
        }

        if (UserService.setPassword(newPassword, this.activity as AppCompatActivity)) {
            Toast(this.activity as Activity).showCustomToast(
                "Your password has been changed", this.activity as Activity
            )
        } else {
            Toast(this.activity as Activity).showCustomToast(
                "Error: password wasn't changed", this.activity as Activity
            )
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

        // set color to return button
        view.findViewById<ImageView>(R.id.return_image).setColorFilter(colorTheme.getFontColor())

        // set color to widget
        view.findViewById<CardView>(R.id.password_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetColor())

        view.findViewById<TextView>(R.id.old_password_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.old_password_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<TextView>(R.id.old_password_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<EditText>(R.id.old_password)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.old_password_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<TextView>(R.id.new_password_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<EditText>(R.id.new_password)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.new_password_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<TextView>(R.id.confirm_new_password_title)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<EditText>(R.id.confirm_new_password)
            .setTextColor(colorTheme.getSettingsWidgetTitleColor())
        view.findViewById<CardView>(R.id.confirm_new_password_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        // set color to button
        view.findViewById<TextView>(R.id.button_text).setTextColor(colorTheme.getButtonTextColor())
        view.findViewById<CardView>(R.id.button_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
    }
}
