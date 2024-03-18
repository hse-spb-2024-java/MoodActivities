package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.services.MoodService
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
        return view
    }

    private fun restoreFragmentData(activity: MoodFlowActivity, view: View) {
        val moodEvent = activity.getMoodEvent()
        view.findViewById<ImageView>(R.id.emoji)?.setImageResource(
            UiUtils.getMoodImageResourcesIdByIndex(moodEvent.getMoodRate()!!)
        )
        // there is no GPT answer
        val gptResponse : MoodService.Companion.GptMoodResponse = MoodService.getGptResponse()
        view.findViewById<TextView>(R.id.summary_title)?.text =
            gptResponse.shortSummary
        view.findViewById<TextView>(R.id.summary_description)?.text = gptResponse.fullSummary

    }
}
