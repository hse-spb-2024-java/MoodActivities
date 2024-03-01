package org.hse.moodactivities.fragments

import android.content.Intent
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
import org.hse.moodactivities.activities.MainActivity
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.data_types.MoodFlowDataType
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.ActivityItem
import org.hse.moodactivities.models.MoodEvent
import kotlin.collections.HashSet

class ChooseActivitiesFragment : Fragment() {
    private lateinit var communicator: Communicator
    private var chosenActivitiesTexts: HashSet<String> = HashSet()
    private var recyclerView: RecyclerView? = null
    private var activityItems: ArrayList<ActivityItem>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var itemsAdapters: ItemAdapter? = null
    private lateinit var nextButtonBackground : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        communicator = activity as Communicator
        val view = inflater.inflate(R.layout.fragment_activities_choosing, container, false)
        val thisActivity = activity as MoodFlowActivity
        thisActivity.restoreFragmentData(this, view, MoodFlowType.ACTIVITIES_CHOOSING)

        // button to home screen
        view.findViewById<Button>(R.id.return_home_button).setOnClickListener {
            Log.d("home return button", "clicked!")
            val mainActivityIntent = Intent(this.activity, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }

        // button to next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            Log.i("fragment_changing", "go to emotions choosing")
            if (chosenActivitiesTexts.size != 0) {
                communicator.replaceFragment(ChooseEmotionsFragment())
            }
        }

        nextButtonBackground = view.findViewById(R.id.next_button_background)
        if (chosenActivitiesTexts.isNotEmpty()) {
            nextButtonBackground.alpha = 1.0f
        }

        // button to previous fragment
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            Log.i("fragment_changing", "return to day rating")
            val moodEvent = MoodEvent()
            moodEvent.setActivitiesChosen(chosenActivitiesTexts)
            communicator.passData(moodEvent, MoodFlowDataType(MoodFlowType.ACTIVITIES_CHOOSING))
            communicator.replaceFragment(RateDayFragment())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        gridLayoutManager =
            GridLayoutManager(activity?.applicationContext, 3, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        activityItems = createActivities()
        itemsAdapters = activity?.applicationContext?.let {
            ItemAdapter(
                it,
                activityItems!!,
                MoodFlowType.ACTIVITIES_CHOOSING
            )
        }
        recyclerView?.adapter = itemsAdapters
        val thisActivity = activity as MoodFlowActivity

        thisActivity.restoreFragmentData(
            this,
            view.rootView,
            MoodFlowType.ACTIVITIES_CHOOSING
        )
    }

    private fun createActivities(): ArrayList<ActivityItem> {
        val activitiesList: ArrayList<ActivityItem> = ArrayList()

        activitiesList.add(
            ActivityItem(
                R.drawable.icon_books,
                "read",
                R.color.yellow,
                chosenActivitiesTexts.contains("read")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_family,
                "family",
                R.color.dark_salmon,
                chosenActivitiesTexts.contains("family")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_work,
                "work",
                R.color.gray_silk,
                chosenActivitiesTexts.contains("work")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_walking,
                "walk",
                R.color.olive,
                chosenActivitiesTexts.contains("walk")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_sport,
                "sport",
                R.color.sea_green,
                chosenActivitiesTexts.contains("sport")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_study,
                "study",
                R.color.ton,
                chosenActivitiesTexts.contains("study")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_movie,
                "watch movie",
                R.color.gray,
                chosenActivitiesTexts.contains("watch movie")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_friends,
                "friends",
                R.color.lilac,
                chosenActivitiesTexts.contains("friends")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_cook,
                "cook",
                R.color.mid_orange,
                chosenActivitiesTexts.contains("cook")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_meditation,
                "meditation",
                R.color.mid_pink,
                chosenActivitiesTexts.contains("meditation")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_music,
                "listen music",
                R.color.gray,
                chosenActivitiesTexts.contains("listen music")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_art,
                "art",
                R.color.gray,
                chosenActivitiesTexts.contains("art")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_photo,
                "photo",
                R.color.gray,
                chosenActivitiesTexts.contains("photo")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_dance,
                "dance",
                R.color.gray,
                chosenActivitiesTexts.contains("dance")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_pet,
                "play with pet",
                R.color.gray,
                chosenActivitiesTexts.contains("play with pet")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_museum,
                "museum",
                R.color.gray,
                chosenActivitiesTexts.contains("museum")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_language,
                "learn new language",
                R.color.gray,
                chosenActivitiesTexts.contains("learn new language")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_board_games,
                "board games",
                R.color.gray,
                chosenActivitiesTexts.contains("board games")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_video_games,
                "video games",
                R.color.gray,
                chosenActivitiesTexts.contains("video games")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_instrument,
                "play music",
                R.color.gray,
                chosenActivitiesTexts.contains("play music")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_date,
                "date",
                R.color.gray,
                chosenActivitiesTexts.contains("date")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_shopping,
                "shopping",
                R.color.gray,
                chosenActivitiesTexts.contains("shopping")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_party,
                "party",
                R.color.gray,
                chosenActivitiesTexts.contains("party")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_attractions,
                "attractions",
                R.color.gray,
                chosenActivitiesTexts.contains("attractions")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_holiday,
                "holiday",
                R.color.gray,
                chosenActivitiesTexts.contains("holiday")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_concert,
                "concert",
                R.color.gray,
                chosenActivitiesTexts.contains("concert")
            )
        )
        activitiesList.add(
            ActivityItem(
                R.drawable.icon_gardening,
                "gardening",
                R.color.asparagus,
                chosenActivitiesTexts.contains("gardening")
            )
        )

        return activitiesList
    }

    fun clickButton(buttonBackground: CardView, text: String) {
        if (chosenActivitiesTexts.contains(text)) {
            buttonBackground.alpha = 0.7f
            chosenActivitiesTexts.remove(text)
        } else {
            buttonBackground.alpha = 1.0f
            chosenActivitiesTexts.add(text)
        }
        if (chosenActivitiesTexts.size != 0) {
            nextButtonBackground.alpha = 1.0f
        } else {
            nextButtonBackground.alpha = 0.5f
        }
    }

    fun setActivities(chosenActivitiesTexts: HashSet<String>) {
        this.chosenActivitiesTexts = chosenActivitiesTexts
    }
}
