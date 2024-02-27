package org.hse.moodactivities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.activities.MoodFlowFragmentType
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.data_types.MoodFlowDataType
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.ActivityItem
import org.hse.moodactivities.models.MoodFlowData
import kotlin.collections.HashSet

//enum class Activities {
//    BOOKS, FAMILY, WORK,
//    WALK, SPORT, STUDY,
//    MOVIE, FRIENDS, COOK,
//    MEDITATION, LISTEN_MUSIC,
//    ART, PHOTO, DANCE, PET,
//    MUSEUM, LANGUAGE, BOARD_GAMES,
//    VIDEO_GAMES, PLAY_MUSIC, DATE,
//    SHOPPING, PARTY, ATTRACTIONS,
//    HOLIDAY, CONCERT, GARDENING
//}

class ActivitiesChoosingFragment : Fragment() {
    private lateinit var communicator: Communicator
    private var chosenActivitiesTexts: HashSet<String> = HashSet()
    private var recyclerView: RecyclerView? = null
    private var charItem: ArrayList<ActivityItem>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var itemsAdapters: ItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities_choosing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        gridLayoutManager =
            GridLayoutManager(activity?.applicationContext, 3, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        charItem = ArrayList()
        charItem = createActivities()
        itemsAdapters = activity?.applicationContext?.let { ItemAdapter(it, charItem!!) }
        recyclerView?.adapter = itemsAdapters
        val thisActivity = activity as MoodFlowActivity

        thisActivity.restoreFragmentData(this, view.rootView, MoodFlowFragmentType.ACTIVITIES_CHOOSING)


        communicator = activity as Communicator

        val returnButton = view.findViewById<Button>(R.id.back_button)
        returnButton.setOnClickListener {
            Log.i("fragment_changing", "return to day rating")
            val moodFlowData = MoodFlowData()
            moodFlowData.setActivitiesChosen(chosenActivitiesTexts)
            communicator.passData(moodFlowData, MoodFlowDataType(MoodFlowType.ACTIVITIES_CHOOSING))
            communicator.replaceFragment(DayRatingFragment())
        }

        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            Log.i("fragment_changing", "go to emotions choosing")
            if (chosenActivitiesTexts.size != 0) {
                communicator.replaceFragment(EmotionsChoosingFragment())
            }
        }
    }

    private fun createActivities(): ArrayList<ActivityItem> {
        val activitiesList: ArrayList<ActivityItem> = ArrayList()

        activitiesList.add(ActivityItem(R.drawable.icon_books, "read", R.color.tan))
        activitiesList.add(ActivityItem(R.drawable.icon_family, "family", R.color.dark_salmon))
        activitiesList.add(ActivityItem(R.drawable.icon_work, "work", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_walking, "walk", R.color.olive))
        activitiesList.add(ActivityItem(R.drawable.icon_sport, "sport", R.color.sea_green))
        activitiesList.add(ActivityItem(R.drawable.icon_study, "study", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_movie, "watch movie", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_friends, "friends", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_cook, "cook", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_meditation, "meditation", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_music, "listen music", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_art, "art", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_photo, "photo", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_dance, "dance", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_pet, "play with pet", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_museum, "museum", R.color.gray))
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_language, "learn new language", R.color.gray
            )
        )
        activitiesList.add(ActivityItem(R.drawable.icon_board_games, "board games", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_video_games, "video games", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_instrument, "play music", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_date, "date", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_shopping, "shopping", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_party, "party", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_attractions, "attractions", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_holiday, "holiday", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_concert, "concert", R.color.gray))
        activitiesList.add(ActivityItem(R.drawable.icon_gardening, "gardening", R.color.asparagus))

        return activitiesList
    }

    fun clickButton(buttonBackground : CardView, text : String) {
        if (chosenActivitiesTexts.contains(text)) {
            buttonBackground.alpha = 0.7f
            chosenActivitiesTexts.remove(text)
        } else {
            buttonBackground.alpha = 1.0f
            chosenActivitiesTexts.add(text)
        }
    }
}
