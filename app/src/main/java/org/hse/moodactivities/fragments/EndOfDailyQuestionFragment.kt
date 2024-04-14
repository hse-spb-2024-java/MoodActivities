package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MainScreenActivity

class EndOfDailyQuestionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_end_of_daily_question, container, false)

        // button to finish
        view.findViewById<Button>(R.id.finish_button).setOnClickListener {
            startActivity(Intent(this.activity, MainScreenActivity::class.java))
        }
        return view
    }
}
