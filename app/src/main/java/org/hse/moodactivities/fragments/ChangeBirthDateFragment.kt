package org.hse.moodactivities.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.showCustomToast

class ChangeBirthDateFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_birth_date, container, false)

        // button to profile
        view.findViewById<Button>(R.id.return_button).setOnClickListener {
            this.activity?.finish()
        }

        view.findViewById<CardView>(R.id.button_background).setOnClickListener {
//            view.findViewById<EditText>(R.id.new_name).text.clear()

            Toast(this.activity as Activity).showCustomToast(
                "Your birth date has been changed", this.activity as Activity
            )
        }


        setColorTheme(view)

        return view
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
        view.findViewById<CardView>(R.id.birth_date_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetColor())

        // set color
        view.findViewById<CardView>(R.id.new_birth_date_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_day).textColor =
            colorTheme.getSettingsWidgetFieldColor()
        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_day).selectedTextColor =
            colorTheme.getSettingsWidgetTitleColor()

    }
}
