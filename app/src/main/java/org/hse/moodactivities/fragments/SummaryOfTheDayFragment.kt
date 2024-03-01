package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MainActivity
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.data_types.MoodFlowType

class SummaryOfTheDayFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summary_of_the_day, container, false)
        val activity = activity as MoodFlowActivity
        activity.restoreFragmentData(this, view, MoodFlowType.SUMMARY_OF_THE_DAY)

        // button to finish
        view.findViewById<Button>(R.id.finish_button).setOnClickListener {
            val mainActivityIntent = Intent(this.activity, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }
        return view
    }
}