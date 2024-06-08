package org.hse.moodactivities.fragments

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.viewmodels.QuestionViewModel

const val MAXIMAL_SIZE_OF_USER_ANSWER = 100
const val MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER = 8

class AnswerDailyQuestionFragment : Fragment() {
    private lateinit var communicator: Communicator
    private lateinit var userAnswer: EditText
    private lateinit var question: TextView
    private lateinit var nextButtonBackground: CardView
    private lateinit var questionViewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answer_question, container, false)

        val parentActivity = activity as MoodFlowActivity
        restoreFragmentData(parentActivity)

        nextButtonBackground = view.findViewById(R.id.next_button_background)

        userAnswer = view.findViewById(R.id.card_text)
        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAXIMAL_SIZE_OF_USER_ANSWER))
        userAnswer.filters = filters
        userAnswer.maxLines = MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER

        questionViewModel = QuestionViewModel()
        questionViewModel.onCreateView(this.requireActivity())

        question = view.findViewById(R.id.question)
        question.text = questionViewModel.getRandomQuestion()

        communicator = activity as Communicator

        // button to next fragment
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            if (userAnswer.text.isNotEmpty()) {
                val moodEvent = MoodEvent()
                moodEvent.setQuestion(question.text.toString())
                moodEvent.setUserAnswer(userAnswer.text.toString())
                communicator.passData(moodEvent)
                communicator.replaceFragment(SummaryOfTheDayFragment())
            }
        }

        // button to previous fragment
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            val moodEvent = MoodEvent()
            moodEvent.setQuestion(question.text.toString())
            moodEvent.setUserAnswer(userAnswer.text.toString())
            communicator.passData(moodEvent)
            communicator.replaceFragment(ChooseEmotionsFragment())
        }

        // button to regenerate question
        view.findViewById<Button>(R.id.regenerate_button).setOnClickListener {
            this.question.text = questionViewModel.getRandomQuestion()
        }

        setColorTheme(view)

        return view
    }

    private fun restoreFragmentData(activity: MoodFlowActivity) {
    }

    private fun setColorTheme(view: View) {
        val colorTheme =

        // set color to background
        view.findViewById<ConstraintLayout>(R.id.fragment_answer_question_layout)
            ?.setBackgroundColor(ThemesService.getBackgroundColor())

        // set color to tittle
        view.findViewById<TextView>(R.id.title)
            ?.setTextColor(ThemesService.getFontColor())

        // set color to card
        view.findViewById<CardView>(R.id.card)?.setCardBackgroundColor(ThemesService.getColor3())

        // set color to card text
        view.findViewById<TextView>(R.id.card_text)
            ?.setTextColor(ThemesService.getDimmedBackgroundColor())

        // set color to back button
        view.findViewById<CardView>(R.id.back_button_background)
            ?.setCardBackgroundColor(ThemesService.getButtonColor())
        view.findViewById<TextView>(R.id.back_button_text)
            ?.setTextColor(ThemesService.getButtonTextColor())

        // set color to regenerate button
        view.findViewById<CardView>(R.id.next_button_background)
            ?.setCardBackgroundColor(ThemesService.getButtonColor())
        view.findViewById<TextView>(R.id.next_button_text)
            ?.setTextColor(ThemesService.getButtonTextColor())

        // set color to next button
        view.findViewById<CardView>(R.id.regenerate_button_background)
            ?.setCardBackgroundColor(ThemesService.getButtonColor())
        view.findViewById<TextView>(R.id.regenerate_button_text)
            ?.setTextColor(ThemesService.getButtonTextColor())
    }
}
