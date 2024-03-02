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

class ChooseEmotionsFragment : Fragment(), ItemHolderFragment {
    private lateinit var communicator: Communicator
    private var chosenEmotions: HashSet<String> = HashSet()
    private var recyclerView: RecyclerView? = null
    private var emotionItem: ArrayList<Item>? = null

    private var itemsAdapters: ItemAdapter? = null
    private lateinit var nextButtonBackground: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_emotions, container, false)

        val parentActivity = activity as MoodFlowActivity
        restoreFragmentData(parentActivity)

        communicator = activity as Communicator

        // button to home screen
        view.findViewById<Button>(R.id.return_home_button).setOnClickListener {
            val mainActivityIntent = Intent(this.activity, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }

        // button to previous fragment
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            val moodEvent = MoodEvent()
            moodEvent.setChosenEmotions(chosenEmotions)
            communicator.passData(moodEvent)
            communicator.replaceFragment(ChooseActivitiesFragment())
        }

        // button to next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            if (chosenEmotions.isNotEmpty()) {
                val moodEvent = MoodEvent()
                moodEvent.setChosenEmotions(chosenEmotions)
                communicator.passData(moodEvent)
                communicator.replaceFragment(AnswerDailyQuestionFragment())
            }
        }

        nextButtonBackground = view.findViewById(R.id.next_button_background)
        if (chosenEmotions.isNotEmpty()) {
            nextButtonBackground.alpha = 1.0f
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        val gridLayoutManager = GridLayoutManager(
            activity?.applicationContext, 3, LinearLayoutManager.VERTICAL, false
        )
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        emotionItem = createEmotions()
        itemsAdapters = parentActivity.applicationContext?.let {
            ItemAdapter(it, emotionItem!!)
        }
        recyclerView?.adapter = itemsAdapters

        return view
    }

    private fun restoreFragmentData(activity: MoodFlowActivity) {
        val moodEvent = activity.getMoodEvent()
        if (moodEvent.getChosenEmotions() != null) {
            chosenEmotions = moodEvent.getChosenEmotions()!!
        }
    }

    private fun createItem(iconIndex: Int, text: String, iconColor: Int): Item {
        return Item(
            iconIndex, text, iconColor, chosenEmotions.contains(text)
        )
    }

    private fun createEmotions(): ArrayList<Item> {
        return arrayListOf(
            createItem(R.drawable.icon_joy, "joy", R.color.peach),
            createItem(R.drawable.icon_sadness, "sadness", R.color.peach),
            createItem(R.drawable.icon_relax, "satisfaction", R.color.peach),
            createItem(R.drawable.icon_inspiration, "inspiration", R.color.peach),
            createItem(R.drawable.icon_anxiety, "anxiety", R.color.peach),
            createItem(R.drawable.icon_love, "love", R.color.peach),
            createItem(R.drawable.icon_optimism, "optimism", R.color.peach),
            createItem(R.drawable.icon_disappointment, "disappointment", R.color.peach),
            createItem(R.drawable.icon_anger, "anger", R.color.peach),
            createItem(R.drawable.icon_meditation, "awareness", R.color.peach),
            createItem(R.drawable.icon_fear, "fear", R.color.peach),
            createItem(R.drawable.icon_drowsiness, "drowsiness", R.color.peach),
            createItem(R.drawable.icon_thoughtfulness, "thoughtfulness", R.color.peach),
            createItem(R.drawable.icon_lonely, "lonely", R.color.peach),
            createItem(R.drawable.icon_gratitude, "gratitude", R.color.peach),
            createItem(R.drawable.icon_irony, "irony", R.color.peach),
            createItem(R.drawable.icon_trust, "trust", R.color.peach),
            createItem(R.drawable.icon_party, "enthusiasm", R.color.peach),
            createItem(R.drawable.icon_astonishment, "astonishment", R.color.peach),
            createItem(R.drawable.icon_irritation, "irritation", R.color.peach),
            createItem(R.drawable.icon_shock, "shock", R.color.peach),
            createItem(R.drawable.icon_indifference, "indifference", R.color.peach),
            createItem(R.drawable.icon_determination, "determination", R.color.peach),
            createItem(R.drawable.icon_energy, "energy", R.color.peach),
            createItem(R.drawable.icon_admiration, "admiration", R.color.peach)
        )
    }

    override fun clickButton(buttonBackground: CardView, text: String) {
        if (chosenEmotions.contains(text)) {
            buttonBackground.alpha = BUTTON_DISABLED_ALPHA
            chosenEmotions.remove(text)
        } else {
            buttonBackground.alpha = BUTTON_ENABLED_ALPHA
            chosenEmotions.add(text)
        }
        if (chosenEmotions.isNotEmpty()) {
            nextButtonBackground.alpha = BUTTON_ENABLED_ALPHA
        } else {
            nextButtonBackground.alpha = BUTTON_DISABLED_ALPHA
        }
    }

    fun setEmotions(chosenEmotionsTexts: HashSet<String>) {
        this.chosenEmotions = chosenEmotionsTexts
    }
}
