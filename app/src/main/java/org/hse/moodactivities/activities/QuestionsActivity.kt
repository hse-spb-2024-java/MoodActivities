package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.hse.moodactivities.R
import org.hse.moodactivities.fragments.QuestionOfTheDayFragment
import org.hse.moodactivities.interfaces.Communicator
import org.hse.moodactivities.interfaces.Data

class QuestionsActivity : AppCompatActivity(), Communicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_flow)
        replaceFragment(QuestionOfTheDayFragment())
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.activity_mood_flow_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun passData(data: Data) {
    }
}
