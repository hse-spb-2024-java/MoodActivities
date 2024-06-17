package org.hse.moodactivities.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
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
import org.hse.moodactivities.services.UserService
import org.hse.moodactivities.utils.showCustomToast
import java.time.format.DateTimeFormatter

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
            sendNewDate()
        }

        view.findViewById<TextView>(R.id.birth_date_name).text = UserService.getBirthDate()

        setColorTheme(view)

        return view
    }

    private fun sendNewDate() {
        val day = view?.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_day)?.value
        val month = view?.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_month)?.value
        val year = view?.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_year)?.value

        val date : String?
        try {
            val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            val dateStr = "$day-$month-$year"
            Log.i("date", dateStr)
            date = dateStr.format(formatter)
        } catch (_: Exception) {
            Toast(this.activity as Activity).showCustomToast(
                "Wrong date", this.activity as Activity
            )
            return
        }

        // todo: send date
        UserService.setBirthDate(date)
        view?.findViewById<TextView>(R.id.birth_date_name)?.text = date

        Toast(this.activity as Activity).showCustomToast(
            "Your birth date has been changed", this.activity as Activity
        )
    }

    @SuppressLint("CutPasteId")
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

        // set color to old birth date
        view.findViewById<CardView>(R.id.old_birth_date_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())
        view.findViewById<TextView>(R.id.birth_date_name)
            .setTextColor(colorTheme.getSettingsWidgetFieldColor())

        // set color to number picker
        view.findViewById<CardView>(R.id.new_birth_date_background)
            .setCardBackgroundColor(colorTheme.getSettingsWidgetFieldColor())

        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_day).textColor =
            colorTheme.getSettingsWidgetFieldColor()
        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_day).selectedTextColor =
            colorTheme.getSettingsWidgetTitleColor()

        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_month).textColor =
            colorTheme.getSettingsWidgetFieldColor()
        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_month).selectedTextColor =
            colorTheme.getSettingsWidgetTitleColor()

        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_year).textColor =
            colorTheme.getSettingsWidgetFieldColor()
        view.findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.new_birth_year).selectedTextColor =
            colorTheme.getSettingsWidgetTitleColor()

        // set color to button
        view.findViewById<CardView>(R.id.button_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
        view.findViewById<TextView>(R.id.button_text).setTextColor(colorTheme.getButtonTextColor())
    }
}
