package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.services.ThemesService

class EndOfDailyQuestionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_end_of_daily_question, container, false)

        // button to finish
        view.findViewById<Button>(R.id.finish_button).setOnClickListener {
            startActivity(Intent(this.activity, MainScreenActivity::class.java))
        }

        setColorTheme(view)

        return view
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        // set color to background
        view.findViewById<ConstraintLayout>(R.id.layout)
            ?.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to title
        view.findViewById<TextView>(R.id.title)?.setTextColor(colorTheme.getFontColor())

        // set color to message
        view.findViewById<TextView>(R.id.message)?.setTextColor(colorTheme.getFontColor())

        // set color to next button
        view.findViewById<CardView>(R.id.finish_button_background)
            ?.setCardBackgroundColor(colorTheme.getDailyQuestionWidgetIconColor())
        view.findViewById<TextView>(R.id.button_text)
            ?.setTextColor(colorTheme.getDailyQuestionWidgetIconTextColor())
    }
}
