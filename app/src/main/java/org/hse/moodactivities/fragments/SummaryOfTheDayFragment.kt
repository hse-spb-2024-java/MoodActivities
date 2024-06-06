package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.services.MoodService
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.UiUtils

class SummaryOfTheDayFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summary_of_the_day, container, false)

        val parentActivity = activity as MoodFlowActivity
        restoreFragmentData(parentActivity, view)

        // button to finish
        view.findViewById<Button>(R.id.finish_button).setOnClickListener {
            startActivity(Intent(this.activity, MainScreenActivity::class.java))
        }

        setColorTheme(view)

        return view
    }


    private fun restoreFragmentData(activity: MoodFlowActivity, view: View) {
        val moodEvent = activity.getMoodEvent()
        val moodRate = moodEvent.getMoodRate() ?: -1
        view.findViewById<ImageView>(R.id.emoji)?.setImageResource(
            UiUtils.getMoodImageResourcesIdByIndex(moodRate)
        )

        val gptResponse = MoodService.getGptResponse(this.activity as AppCompatActivity)
        view.findViewById<TextView>(R.id.summary_title)?.text =
            gptResponse.shortSummary
        view.findViewById<TextView>(R.id.summary_description)?.text = gptResponse.fullSummary
    }

    private fun setColorTheme(view: View) {
        // set color to background
        view.findViewById<ConstraintLayout>(R.id.layout)
            ?.setBackgroundColor(ThemesService.getBackgroundColor())

        // set color to tittle
        view.findViewById<TextView>(R.id.title)
            ?.setTextColor(ThemesService.getFontColor())

        // set color to summary tittle
        view.findViewById<TextView>(R.id.summary_title)
            ?.setTextColor(ThemesService.getFontColor())

        // set color to summary description
        view.findViewById<TextView>(R.id.summary_description)
            ?.setTextColor(ThemesService.getFontColor())

        // set color to card
        view.findViewById<CardView>(R.id.card)?.setCardBackgroundColor(ThemesService.getColor3())

        // set color to finish button
        view.findViewById<CardView>(R.id.button_background)
            ?.setCardBackgroundColor(ThemesService.getButtonColor())
        view.findViewById<TextView>(R.id.button_text)
            ?.setTextColor(ThemesService.getDimmedBackgroundColor())
    }
}
