package org.hse.moodactivities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.ItemHolderFragment
import org.hse.moodactivities.models.ActivatedItem
import org.hse.moodactivities.models.EMOTIONS
import org.hse.moodactivities.models.Item
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA

class ChooseEmotionsFragment : Fragment(), ItemHolderFragment {
    private lateinit var communicator: Communicator
    private var chosenEmotions: HashSet<String> = HashSet()
    private var recyclerView: RecyclerView? = null
    private var emotionItem: ArrayList<ActivatedItem>? = null

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
            this.activity?.finish()
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

        nextButtonBackground = view.findViewById(R.id.button_background)
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

        setColorTheme(view)

        return view
    }

    private fun restoreFragmentData(activity: MoodFlowActivity) {
        val moodEvent = activity.getMoodEvent()
        if (moodEvent.getChosenEmotions() != null) {
            chosenEmotions = moodEvent.getChosenEmotions()!!
        }
    }

    private fun createActivatedItem(item: Item): ActivatedItem {
        return ActivatedItem(
            item.getName(),
            item.getIconIndex(),
            item.getIconColor(),
            chosenEmotions.contains(item.getName())
        )
    }

    private fun createEmotions(): ArrayList<ActivatedItem> {
        return EMOTIONS.mapTo(ArrayList()) { createActivatedItem(it) }
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

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        // set color to background
        view.findViewById<ConstraintLayout>(R.id.fragment_choose_emotions_layout)
            ?.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to return button
        view.findViewById<ImageView>(R.id.return_image)
            ?.setColorFilter(colorTheme.getMoodFlowWidgetIconColor())

        // set color to tittle
        view.findViewById<TextView>(R.id.title)?.setTextColor(colorTheme.getFontColor())

        // set color to question
        view.findViewById<TextView>(R.id.question)?.setTextColor(colorTheme.getFontColor())

        // set color to back button
        view.findViewById<CardView>(R.id.back_button_background)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowWidgetIconColor())
        view.findViewById<TextView>(R.id.back_button_text)
            ?.setTextColor(colorTheme.getMoodFlowWidgetIconTextColor())

        // set color to next button
        view.findViewById<CardView>(R.id.button_background)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowWidgetIconColor())
        view.findViewById<TextView>(R.id.next_button_text)
            ?.setTextColor(colorTheme.getMoodFlowWidgetIconTextColor())
    }
}
