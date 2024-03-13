package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
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
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.ItemHolderFragment
import org.hse.moodactivities.models.Item
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import kotlin.collections.HashSet

class ChooseActivitiesFragment : Fragment(), ItemHolderFragment {
    private lateinit var communicator: Communicator
    private var chosenActivities: HashSet<String> = HashSet()
    private var recyclerView: RecyclerView? = null
    private var items: ArrayList<Item>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var itemsAdapters: ItemAdapter? = null
    private lateinit var nextButtonBackground: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_activities, container, false)

        val parentActivity = activity as MoodFlowActivity
        restoreFragmentData(parentActivity)

        communicator = activity as Communicator

        // button to home screen
        view.findViewById<Button>(R.id.return_home_button).setOnClickListener {
            startActivity(Intent(this.activity, MainActivity::class.java))
        }

        // button to previous fragment
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            val moodEvent = MoodEvent()
            moodEvent.setChosenActivities(chosenActivities)
            communicator.passData(moodEvent)
            communicator.replaceFragment(RateDayFragment())
        }

        // button to next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            if (chosenActivities.isNotEmpty()) {
                val moodEvent = MoodEvent()
                moodEvent.setChosenActivities(chosenActivities)
                communicator.passData(moodEvent)
                communicator.replaceFragment(ChooseEmotionsFragment())
            }
        }

        nextButtonBackground = view.findViewById(R.id.next_button_background)
        if (chosenActivities.isNotEmpty()) {
            nextButtonBackground.alpha = BUTTON_ENABLED_ALPHA
        }

        // set activities
        recyclerView = view.findViewById(R.id.recycler_view)
        gridLayoutManager =
            GridLayoutManager(
                parentActivity.applicationContext,
                3,
                LinearLayoutManager.VERTICAL,
                false
            )
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        items = createActivities()
        itemsAdapters = parentActivity.applicationContext?.let {
            ItemAdapter(it, items!!)
        }
        recyclerView?.adapter = itemsAdapters

        return view
    }

    private fun restoreFragmentData(activity: MoodFlowActivity) {
        val moodEvent = activity.getMoodEvent()
        if (moodEvent.getChosenActivities() != null) {
            chosenActivities = moodEvent.getChosenActivities()!!
        }
    }

    private fun createItem(iconIndex: Int, text: String, iconColor: Int): Item {
        return Item(iconIndex, text, iconColor, chosenActivities.contains(text))
    }

    private fun createActivities(): ArrayList<Item> {
        return arrayListOf(
            createItem(R.drawable.icon_books, "read", R.color.gray),
            createItem(R.drawable.icon_family, "family", R.color.gray),
            createItem(R.drawable.icon_work, "work", R.color.gray),
            createItem(R.drawable.icon_walking, "walk", R.color.gray),
            createItem(R.drawable.icon_sport, "sport", R.color.gray),
            createItem(R.drawable.icon_study, "study", R.color.gray),
            createItem(R.drawable.icon_movie, "watch movie", R.color.gray),
            createItem(R.drawable.icon_friends, "friends", R.color.gray),
            createItem(R.drawable.icon_cook, "cook", R.color.gray),
            createItem(R.drawable.icon_meditation, "meditation", R.color.gray),
            createItem(R.drawable.icon_music, "listen music", R.color.gray),
            createItem(R.drawable.icon_art, "art", R.color.gray),
            createItem(R.drawable.icon_photo, "photo", R.color.gray),
            createItem(R.drawable.icon_dance, "dance", R.color.gray),
            createItem(R.drawable.icon_pet, "play with pet", R.color.gray),
            createItem(R.drawable.icon_museum, "museum", R.color.gray),
            createItem(R.drawable.icon_language, "learn new language", R.color.gray),
            createItem(R.drawable.icon_board_games, "board games", R.color.gray),
            createItem(R.drawable.icon_video_games, "video games", R.color.gray),
            createItem(R.drawable.icon_instrument, "play music", R.color.gray),
            createItem(R.drawable.icon_date, "date", R.color.gray),
            createItem(R.drawable.icon_shopping, "shopping", R.color.gray),
            createItem(R.drawable.icon_party, "party", R.color.gray),
            createItem(R.drawable.icon_attractions, "attractions", R.color.gray),
            createItem(R.drawable.icon_holiday, "holiday", R.color.gray),
            createItem(R.drawable.icon_concert, "concert", R.color.gray),
            createItem(R.drawable.icon_gardening, "gardening", R.color.gray)
        )
    }

    override fun clickButton(buttonBackground: CardView, text: String) {
        if (chosenActivities.contains(text)) {
            buttonBackground.alpha = BUTTON_DISABLED_ALPHA
            chosenActivities.remove(text)
        } else {
            buttonBackground.alpha = BUTTON_ENABLED_ALPHA
            chosenActivities.add(text)
        }
        if (chosenActivities.isNotEmpty()) {
            nextButtonBackground.alpha = BUTTON_ENABLED_ALPHA
        } else {
            nextButtonBackground.alpha = BUTTON_DISABLED_ALPHA
        }
    }
}
