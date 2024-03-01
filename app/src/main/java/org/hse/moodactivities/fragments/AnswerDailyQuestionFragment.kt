package org.hse.moodactivities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.data_types.MoodFlowDataType
import org.hse.moodactivities.data_types.MoodFlowType
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.models.MoodEvent

class AnswerDailyQuestionFragment : Fragment() {
    private lateinit var communicator: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        communicator = activity as Communicator
        return inflater.inflate(R.layout.fragment_answer_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = activity as Communicator

        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            Log.i("fragment_changing", "return to day rating")
            val moodEvent = MoodEvent()
            val editText = view.findViewById<EditText>(R.id.edit_text)
            moodEvent.setAnswerForQuestion(editText.text.toString())
            communicator.passData(moodEvent, MoodFlowDataType(MoodFlowType.EMOTIONS_CHOOSING))
            communicator.replaceFragment(SummaryOfTheDayFragment())
        }

        // button to previous fragment
        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            Log.i("fragment_changing", "return to activities rating")
            communicator.replaceFragment(ChooseEmotionsFragment())
        }
    }
}