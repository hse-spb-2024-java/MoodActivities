package org.hse.moodactivities.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.data_types.MoodFlowDataType
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.fragments.ChooseActivitiesFragment
import org.hse.moodactivities.fragments.ChooseEmotionsFragment
import org.hse.moodactivities.fragments.RateDayFragment
import org.hse.moodactivities.fragments.SummaryOfTheDayFragment
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.Data
import org.hse.moodactivities.interfaces.DataType
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
        fragmentTransaction.replace(R.id.activity_mood_flow_frame_layout, fragment)
        fragmentTransaction.commit()
        if (fragment is SummaryOfTheDayFragment) {
            MoodService.sendMoodEvent(moodEvent)
            dayRate = MoodService.getDayRate(moodEvent)
        }
    }

    override fun passData(data: Data, dataType: DataType) {
        val receivedMoodEvent = data as MoodEvent
        val receivedDataType = dataType as MoodFlowDataType
        if (receivedDataType.getDataType() == MoodFlowType.DAY_RATING) {
            moodEvent.setMoodRating(receivedMoodEvent.getMoodRating()!!)
        } else if (receivedDataType.getDataType() == MoodFlowType.ACTIVITIES_CHOOSING) {
            moodEvent.setActivitiesChosen(data.getActivitiesChosen())
        } else if (receivedDataType.getDataType() == MoodFlowType.EMOTIONS_CHOOSING) {
            moodEvent.setEmotionsChosen(receivedMoodEvent.getEmotionsChosen())
        } else if (receivedDataType.getDataType() == MoodFlowType.ANSWER_QUESTION) {
            moodEvent.setAnswerForQuestion(receivedMoodEvent.getAnswerForQuestion().orEmpty())
        }
    }

    fun restoreFragmentData(fragment: Fragment, view: View, fragmentType: MoodFlowType) {
        if (fragmentType == MoodFlowType.DAY_RATING) {
            val moodImageId = when (moodEvent.getMoodRating()) {
                0 -> R.id.mood_1_image
                1 -> R.id.mood_2_image
                2 -> R.id.mood_3_image
                3 -> R.id.mood_4_image
                4 -> R.id.mood_5_image
                else -> -1
            }
            if (moodImageId != -1) {
                val rateDayFragment = fragment as RateDayFragment
                view.findViewById<ImageView>(moodImageId)?.alpha = 1.0f
                view.findViewById<CardView>(R.id.next_button_background)?.alpha = 1.0f
                rateDayFragment.setActiveMoodIndex(moodEvent.getMoodRating()!!)
            }
        } else if (fragmentType == MoodFlowType.ACTIVITIES_CHOOSING) {
            val activitiesChosen = moodEvent.getActivitiesChosen()
            val fragmentAc = fragment as ChooseActivitiesFragment
            fragmentAc.setActivities(activitiesChosen)
        } else if (fragmentType == MoodFlowType.EMOTIONS_CHOOSING) {
            val emotionsChosen = moodEvent.getEmotionsChosen()
            val fragmentAc = fragment as ChooseEmotionsFragment
            fragmentAc.setEmotions(emotionsChosen)
        }  else if (fragmentType == MoodFlowType.ANSWER_QUESTION) {
            // TODO: add custom
        } else {
            view.findViewById<ImageView>(R.id.emoji)?.setImageResource(R.drawable.mood_flow_1)
            view.findViewById<TextView>(R.id.summary_title)?.text = MoodService.getGptShortResponse()
            view.findViewById<TextView>(R.id.summary_description)?.text = MoodService.getGptLongResponse()
        }
    }
}
