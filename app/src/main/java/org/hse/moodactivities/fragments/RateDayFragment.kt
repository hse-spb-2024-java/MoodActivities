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
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import org.hse.moodactivities.utils.UiUtils

class RateDayFragment : Fragment() {
    private lateinit var communicator: Communicator
    private var activeMoodIndex: Int = -1
    private lateinit var moodButtons: Array<Button?>
    private lateinit var moodImages: Array<ImageView?>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rate_day, container, false)

        val parentActivity = activity as MoodFlowActivity
        restoreFragmentData(parentActivity, view)

        // button to home screen
        view.findViewById<Button>(R.id.return_home_button).setOnClickListener {
            this.activity?.finish()
        }

        // button to the next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            if (activeMoodIndex != -1) {
                val moodEvent = MoodEvent()
                moodEvent.setMoodRate(activeMoodIndex)
                communicator.passData(moodEvent)
                communicator.replaceFragment(ChooseActivitiesFragment())
            }
        }

        setColorTheme(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = activity as Communicator

        moodButtons = arrayOfNulls(5)
        moodImages = arrayOfNulls(5)

        for (index in 0..4) {
            moodButtons[index] = view.findViewById(UiUtils.getMoodButtonIdByIndex(index))
            moodImages[index] = view.findViewById(UiUtils.getMoodImageIdByIndex(index))
            moodButtons[index]?.setOnClickListener {
                clickOnButton(index)
            }
        }
    }

    private fun clickOnButton(index: Int) {
        if (index == activeMoodIndex) {
            moodImages[index]?.alpha = BUTTON_DISABLED_ALPHA
            activeMoodIndex = -1
            view?.findViewById<CardView>(R.id.button_background)?.alpha = BUTTON_DISABLED_ALPHA
        } else {
            if (activeMoodIndex != -1) {
                moodImages[activeMoodIndex]?.alpha = BUTTON_DISABLED_ALPHA
            }
            moodImages[index]?.alpha = BUTTON_ENABLED_ALPHA
            activeMoodIndex = index
            view?.findViewById<CardView>(R.id.button_background)?.alpha = BUTTON_ENABLED_ALPHA
        }
    }

    private fun restoreFragmentData(activity: MoodFlowActivity, view: View) {
        val moodEvent = activity.getMoodEvent()
        if (moodEvent.getMoodRate() != null) {
            val moodImageId = UiUtils.getMoodImageIdByIndex(moodEvent.getMoodRate()!!)
            if (moodImageId != -1) {
                view.findViewById<ImageView>(moodImageId)?.alpha = BUTTON_ENABLED_ALPHA
                view.findViewById<CardView>(R.id.button_background)?.alpha =
                    BUTTON_ENABLED_ALPHA
                this.activeMoodIndex = moodEvent.getMoodRate()!!
            }
        }
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        // set color to background
        view.findViewById<ConstraintLayout>(R.id.fragment_rate_day_layout)
            ?.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to return button
        view.findViewById<ImageView>(R.id.return_image)?.setColorFilter(colorTheme.getFontColor())

        // set color to title
        view.findViewById<TextView>(R.id.title)?.setTextColor(colorTheme.getFontColor())

        // set color to question
        view.findViewById<TextView>(R.id.question)?.setTextColor(colorTheme.getFontColor())

        // set color to card
        view.findViewById<CardView>(R.id.card)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowWidgetColor())

        // set color to next button
        view.findViewById<CardView>(R.id.button_background)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowWidgetIconColor())
        view.findViewById<TextView>(R.id.button_text)
            ?.setTextColor(colorTheme.getMoodFlowWidgetIconTextColor())
    }
}
