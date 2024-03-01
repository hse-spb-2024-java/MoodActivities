package org.hse.moodactivities.fragments

import org.hse.moodactivities.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import org.hse.moodactivities.activities.MainActivity
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.data_types.MoodFlowDataType
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.MoodEvent

class RateDayFragment : Fragment() {
    private lateinit var communicator: Communicator
    private var activeMoodIndex: Int = -1
    private lateinit var moodButtons: Array<Button?>
    private lateinit var moodImages: Array<ImageView?>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_rating, container, false)
        val thisActivity = activity as MoodFlowActivity
        thisActivity.restoreFragmentData(this, view, MoodFlowType.DAY_RATING)

        // button to home screen
        view.findViewById<Button>(R.id.return_home_button).setOnClickListener {
            Log.d("home return button", "clicked!")
            val mainActivityIntent = Intent(this.activity, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }

        // button to go to the next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            Log.d("next button", "clicked!")
            if (activeMoodIndex != -1) {
                val moodEvent = MoodEvent()
                moodEvent.setMoodRating(activeMoodIndex)
                communicator.passData(moodEvent, MoodFlowDataType(MoodFlowType.DAY_RATING))
                communicator.replaceFragment(ChooseActivitiesFragment())
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = activity as Communicator

        moodButtons = arrayOfNulls(5)
        moodImages = arrayOfNulls(5)

        for (index in 0..4) {
            moodButtons[index] = view.findViewById(getButtonIdByIndex(index))
            moodImages[index] = view.findViewById(getImageIdByIndex(index))
            moodButtons[index]?.setOnClickListener {
                Log.d("mood " + (index + 1) + " button", "clicked!")
                clickOnButton(index)
            }
        }
    }

    private fun getButtonIdByIndex(index: Int): Int {
        return when (index) {
            0 -> R.id.mood_1_button
            1 -> R.id.mood_2_button
            2 -> R.id.mood_3_button
            3 -> R.id.mood_4_button
            4 -> R.id.mood_5_button
            else -> -1 // unreachable
        }
    }

    companion object {
        fun getImageIdByIndex(index: Int): Int {
            return when (index) {
                0 -> R.id.mood_1_image
                1 -> R.id.mood_2_image
                2 -> R.id.mood_3_image
                3 -> R.id.mood_4_image
                4 -> R.id.mood_5_image
                else -> -1 // unreachable
            }
        }
    }

    private fun clickOnButton(index: Int) {
        if (index == activeMoodIndex) {
            moodImages[index]?.alpha = 0.5f
            activeMoodIndex = -1
            view?.findViewById<CardView>(R.id.next_button_background)?.alpha = 0.5f
        } else {
            if (activeMoodIndex != -1) {
                moodImages[activeMoodIndex]?.alpha = 0.5f
            }
            moodImages[index]?.alpha = 1.0f
            activeMoodIndex = index
            view?.findViewById<CardView>(R.id.next_button_background)?.alpha = 1.0f
        }
    }

    fun setActiveMoodIndex(moodIndex: Int) {
        this.activeMoodIndex = moodIndex
    }
}
