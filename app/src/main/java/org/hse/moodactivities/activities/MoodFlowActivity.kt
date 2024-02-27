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
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.data_types.MoodFlowDataType
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.fragments.ActivitiesChoosingFragment
import org.hse.moodactivities.fragments.DayRatingFragment
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.Data
import org.hse.moodactivities.interfaces.DataType
import org.hse.moodactivities.models.MoodFlowData


enum class MoodFlowFragmentType {
    DAY_RATING, ACTIVITIES_CHOOSING, EMOTIONS_CHOOSING
}

class MoodFlowActivity : AppCompatActivity(), Communicator {
    private var moodFlowData: MoodFlowData = MoodFlowData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_flow)
        replaceFragment(DayRatingFragment())
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_mood_flow_frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun passData(data: Data, dataType: DataType) {
        val receivedMoodFlowData = data as MoodFlowData
        val receivedDataType = dataType as MoodFlowDataType
        if (receivedDataType.getDataType() == MoodFlowType.DAY_RATING) {
            moodFlowData.setMoodRating(receivedMoodFlowData.getMoodRating()!!)
        } else if (receivedDataType.getDataType() == MoodFlowType.ACTIVITIES_CHOOSING) {
            moodFlowData.setActivitiesChosen(data.getActivitiesChosen())
        }
    }

    fun restoreFragmentData(fragment: Fragment, view: View, fragmentType: MoodFlowFragmentType) {
        if (fragmentType == MoodFlowFragmentType.DAY_RATING) {
            val moodImageId = when (moodFlowData.getMoodRating()) {
                0 -> R.id.mood_1_image
                1 -> R.id.mood_2_image
                2 -> R.id.mood_3_image
                3 -> R.id.mood_4_image
                4 -> R.id.mood_5_image
                else -> -1
            }
            if (moodImageId != -1) {
                val dayRatingFragment = fragment as DayRatingFragment
                view.findViewById<ImageView>(moodImageId)?.alpha = 1.0f
                view.findViewById<CardView>(R.id.next_button_background)?.alpha = 1.0f
                dayRatingFragment.setActiveMoodIndex(moodFlowData.getMoodRating()!!)
            }
        } else if (fragmentType == MoodFlowFragmentType.ACTIVITIES_CHOOSING) {
            val activitiesChosen = moodFlowData.getActivitiesChosen()
            if (activitiesChosen.isNotEmpty()) {
                val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
                // TODO: set chosen activities
            }
        }
    }
}
