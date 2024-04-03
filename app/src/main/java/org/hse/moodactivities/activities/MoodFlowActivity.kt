package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.hse.moodactivities.R
import org.hse.moodactivities.fragments.RateDayFragment
import org.hse.moodactivities.fragments.SummaryOfTheDayFragment
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.Data
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.services.MoodService

class MoodFlowActivity : AppCompatActivity(), Communicator {
    private var moodEvent: MoodEvent = MoodEvent()
    private var dayRate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_flow)
        replaceFragment(RateDayFragment())
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_mood_flow_layout, fragment)
        fragmentTransaction.commit()
        if (fragment is SummaryOfTheDayFragment) {
            MoodService.setMoodEvent(moodEvent)
        }
    }

    override fun passData(data: Data) {
        val receivedMoodEvent = data as MoodEvent
        if (receivedMoodEvent.getMoodRate() != null) {
            moodEvent.setMoodRate(receivedMoodEvent.getMoodRate()!!)
        }
        if (receivedMoodEvent.getChosenActivities() != null) {
            moodEvent.setChosenActivities(receivedMoodEvent.getChosenActivities()!!)
        }
        if (receivedMoodEvent.getChosenEmotions() != null) {
            moodEvent.setChosenEmotions(receivedMoodEvent.getChosenEmotions()!!)
        }
        if (receivedMoodEvent.getUserAnswer() != null) {
            moodEvent.setUserAnswer(receivedMoodEvent.getUserAnswer()!!)
        }
    }

    fun getMoodEvent() : MoodEvent {
        return moodEvent
    }
}
