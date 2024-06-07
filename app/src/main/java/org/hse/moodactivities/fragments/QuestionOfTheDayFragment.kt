package org.hse.moodactivities.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.common.proto.requests.dailyQuestion.AnswerRequest
import org.hse.moodactivities.common.proto.requests.dailyQuestion.QuestionRequest
import org.hse.moodactivities.common.proto.services.QuestionServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.services.UserService
import org.hse.moodactivities.utils.BUTTON_DISABLED_ALPHA
import org.hse.moodactivities.utils.BUTTON_ENABLED_ALPHA
import org.hse.moodactivities.viewmodels.AuthViewModel
import java.net.HttpURLConnection.HTTP_BAD_REQUEST


class QuestionOfTheDayFragment : Fragment() {
    private lateinit var communicator: Communicator
    private lateinit var channel: ManagedChannel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var gptServiceStub: QuestionServiceGrpc.QuestionServiceBlockingStub
    private lateinit var userAnswer: EditText
    private lateinit var sendButtonBackground: CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question_of_the_day, container, false)

        sendButtonBackground = view.findViewById(R.id.send_button_background)

        communicator = activity as Communicator

        channel =
            ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT).usePlaintext()
                .build()

        authViewModel = ViewModelProvider(this.requireActivity())[AuthViewModel::class.java]

        gptServiceStub = QuestionServiceGrpc.newBlockingStub(channel)
            .withInterceptors(
                JwtClientInterceptor {
                    authViewModel.getToken(
                        this.requireActivity()
                            .getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

        val questionTitleTextView: TextView = view.findViewById(R.id.questionTitleTextView)
        val questionTitle = gptServiceStub.getDailyQuestion(QuestionRequest.getDefaultInstance())
        questionTitleTextView.text = questionTitle.question
        userAnswer = view.findViewById(R.id.edit_text)
        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAXIMAL_SIZE_OF_USER_ANSWER))
        userAnswer.filters = filters
        userAnswer.maxLines = MAXIMAL_LINES_AMOUNT_IN_USER_ANSWER
        userAnswer.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (userAnswer.text.isNotEmpty()) {
                    sendButtonBackground.alpha = BUTTON_ENABLED_ALPHA
                } else {
                    sendButtonBackground.alpha = BUTTON_DISABLED_ALPHA
                }
            }
        })

        // button to send data
        view.findViewById<Button>(R.id.send_button).setOnClickListener {
            if (userAnswer.text.isNotEmpty()) {
                val request =
                    AnswerRequest.newBuilder().setQuestion(questionTitleTextView.text.toString())
                        .setAnswer(userAnswer.text.toString()).build()
                val response = gptServiceStub.sendDailyQuestionAnswer(request)
                if (response.statusCode < HTTP_BAD_REQUEST) {
                    communicator.replaceFragment(EndOfDailyQuestionFragment())
                }
            }
        }

        // button to regenerate question
        view.findViewById<Button>(R.id.return_home_button).setOnClickListener {
            val mainActivityIntent = Intent(this.activity, MainScreenActivity::class.java)
            startActivity(mainActivityIntent)
        }

        return view
    }
}
