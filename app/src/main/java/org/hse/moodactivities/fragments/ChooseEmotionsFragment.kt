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
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.ActivityItem

class ChooseEmotionsFragment : Fragment() {
    private lateinit var communicator: Communicator
    private var chosenEmotionsTexts: HashSet<String> = HashSet()
    private var recyclerView: RecyclerView? = null
    private var emotionItem: ArrayList<ActivityItem>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var itemsAdapters: ItemAdapter? = null
    private lateinit var nextButtonBackground : CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_emotions_choosing, container, false)

        val thisActivity = activity as MoodFlowActivity
        thisActivity.restoreFragmentData(this, view, MoodFlowType.EMOTIONS_CHOOSING)

        // button to home screen
        view.findViewById<Button>(R.id.return_home_button).setOnClickListener {
            Log.i("home return button", "clicked!")
            val mainActivityIntent = Intent(this.activity, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }

        // button to previous fragment
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            Log.i("fragment_changing", "return to activities rating")
            communicator.replaceFragment(ChooseActivitiesFragment())
        }

        // button to next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            Log.d("next button", "clicked!")
            if (chosenEmotionsTexts.isNotEmpty()) {
                communicator.replaceFragment(AnswerDailyQuestionFragment())
            }
        }

        nextButtonBackground = view.findViewById(R.id.next_button_background)
        if (chosenEmotionsTexts.isNotEmpty()) {
            nextButtonBackground.alpha = 1.0f
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

        emotionItem = ArrayList()
        emotionItem = createEmotions()
        itemsAdapters = activity?.applicationContext?.let {
            ItemAdapter(
                it,
                emotionItem!!,
                MoodFlowType.EMOTIONS_CHOOSING
            )
        }
        recyclerView?.adapter = itemsAdapters
        val thisActivity = activity as MoodFlowActivity

        thisActivity.restoreFragmentData(this, view.rootView, MoodFlowType.EMOTIONS_CHOOSING)

        communicator = activity as Communicator


    }

    private fun createEmotions(): ArrayList<ActivityItem> {
        val emotionsList: ArrayList<ActivityItem> = ArrayList()

        emotionsList.add(
            ActivityItem(
                R.drawable.icon_joy,
                "joy",
                R.color.asparagus,
                chosenEmotionsTexts.contains("joy")
            )
        )

        emotionsList.add(
            ActivityItem(
                R.drawable.icon_sadness,
                "sadness",
                R.color.peach,
                chosenEmotionsTexts.contains("sadness")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_inspiration,
                "inspiration",
                R.color.steel_blue,
                chosenEmotionsTexts.contains("inspiration")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_relax,
                "satisfaction",
                R.color.lilac,
                chosenEmotionsTexts.contains("satisfaction")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_anxiety,
                "anxiety",
                R.color.peach,
                chosenEmotionsTexts.contains("anxiety")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_love,
                "love",
                R.color.peach,
                chosenEmotionsTexts.contains("love")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_optimism,
                "optimism",
                R.color.peach,
                chosenEmotionsTexts.contains("optimism")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_disappointment,
                "disappointment",
                R.color.peach,
                chosenEmotionsTexts.contains("disappointment")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_anger,
                "anger",
                R.color.peach,
                chosenEmotionsTexts.contains("anger")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_meditation,
                "awareness",
                R.color.peach,
                chosenEmotionsTexts.contains("awareness")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_fear,
                "fear",
                R.color.peach,
                chosenEmotionsTexts.contains("fear")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_drowsiness,
                "drowsiness",
                R.color.peach,
                chosenEmotionsTexts.contains("drowsiness")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_thoughtfulness,
                "thoughtfulness",
                R.color.peach,
                chosenEmotionsTexts.contains("thoughtfulness")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_lonely,
                "lonely",
                R.color.peach,
                chosenEmotionsTexts.contains("lonely")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_gratitude,
                "gratitude",
                R.color.peach,
                chosenEmotionsTexts.contains("gratitude")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_irony,
                "irony",
                R.color.peach,
                chosenEmotionsTexts.contains("irony")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_trust,
                "trust",
                R.color.peach,
                chosenEmotionsTexts.contains("trust")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_party,
                "enthusiasm",
                R.color.peach,
                chosenEmotionsTexts.contains("enthusiasm")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_astonishment,
                "astonishment",
                R.color.peach,
                chosenEmotionsTexts.contains("astonishment")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_irritation,
                "irritation",
                R.color.peach,
                chosenEmotionsTexts.contains("irritation")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_shock,
                "shock",
                R.color.peach,
                chosenEmotionsTexts.contains("shock")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_indifference,
                "indifference",
                R.color.peach,
                chosenEmotionsTexts.contains("indifference")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_determination,
                "determination",
                R.color.peach,
                chosenEmotionsTexts.contains("determination")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_energy,
                "energy",
                R.color.peach,
                chosenEmotionsTexts.contains("energy")
            )
        )
        emotionsList.add(
            ActivityItem(
                R.drawable.icon_admiration,
                "admiration",
                R.color.peach,
                chosenEmotionsTexts.contains("admiration")
            )
        )

        return emotionsList
    }

    fun clickButton(buttonBackground: CardView, text: String) {
        if (chosenEmotionsTexts.contains(text)) {
            buttonBackground.alpha = 0.7f
            chosenEmotionsTexts.remove(text)
        } else {
            buttonBackground.alpha = 1.0f
            chosenEmotionsTexts.add(text)
        }
        if (chosenEmotionsTexts.size != 0) {
            nextButtonBackground.alpha = 1.0f
        } else {
            nextButtonBackground.alpha = 0.5f
        }
    }

    fun setEmotions(chosenEmotionsTexts: HashSet<String>) {
        this.chosenEmotionsTexts = chosenEmotionsTexts
    }
}
