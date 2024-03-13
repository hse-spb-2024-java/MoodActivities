package org.hse.moodactivities.fragments

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA

const val MAXIMAL_SIZE_OF_USER_ANSWER = 100
const val MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER = 8

class AnswerDailyQuestionFragment : Fragment() {
    private lateinit var communicator: Communicator
    private lateinit var userAnswer: EditText
    private lateinit var nextButtonBackground: CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answer_question, container, false)

        val parentActivity = activity as MoodFlowActivity
        restoreFragmentData(parentActivity)

        nextButtonBackground = view.findViewById(R.id.next_button_background)

        userAnswer = view.findViewById(R.id.edit_text)
        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAXIMAL_SIZE_OF_USER_ANSWER))
        userAnswer.filters = filters
        userAnswer.maxLines = MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER
        userAnswer.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (userAnswer.text.isNotEmpty()) {
                    nextButtonBackground.alpha = BUTTON_ENABLED_ALPHA
                } else {
                    nextButtonBackground.alpha = BUTTON_DISABLED_ALPHA
                }
            }
        })

        communicator = activity as Communicator

        // button to next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            if (userAnswer.text.isNotEmpty()) {
                val moodEvent = MoodEvent()
                moodEvent.setUserAnswer(userAnswer.text.toString())
                communicator.passData(moodEvent)
                communicator.replaceFragment(SummaryOfTheDayFragment())
            }
        }

        // button to previous fragment
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            val moodEvent = MoodEvent()
            moodEvent.setUserAnswer(userAnswer.text.toString())
            communicator.passData(moodEvent)
            communicator.replaceFragment(ChooseEmotionsFragment())
        }

        return view
    }

    private fun restoreFragmentData(activity: MoodFlowActivity) {
        // TODO: add custom questions from server
    }
}
