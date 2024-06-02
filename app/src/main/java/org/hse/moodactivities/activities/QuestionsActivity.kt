package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.hse.moodactivities.R
import org.hse.moodactivities.common.proto.requests.dailyQuestion.CheckAnswerRequest
import org.hse.moodactivities.fragments.EndOfDailyQuestionFragment
import org.hse.moodactivities.fragments.QuestionOfTheDayFragment
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.Data
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.viewmodels.QuestionViewModel

class QuestionsActivity : AppCompatActivity(), Communicator {
    private lateinit var questionViewModel: QuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionViewModel = QuestionViewModel()
        questionViewModel.onCreateView(this)
        setContentView(R.layout.activity_mood_flow)
        if (!questionViewModel.check(CheckAnswerRequest.getDefaultInstance())) {
            replaceFragment(QuestionOfTheDayFragment())
        } else {
            replaceFragment(EndOfDailyQuestionFragment())
        }

        setColorTheme()
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_mood_flow_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun passData(data: Data) {
    }

    private fun setColorTheme() {
        // set color to status bar
        window.statusBarColor = ThemesService.getBackgroundColor()

//        // set background color
//        binding.dailyActivityLayout.setBackgroundColor(ThemesService.getBackgroundColor())
//
//        // set tittle color
//        binding.screenTittle.setTextColor(ThemesService.getFontColor())
    }
}
