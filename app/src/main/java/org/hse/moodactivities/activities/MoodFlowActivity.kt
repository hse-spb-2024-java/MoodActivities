package org.hse.moodactivities.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import org.hse.moodactivities.R

class MoodFlowActivity : AppCompatActivity() {
    private var activeMoodIndex: Int = -1
    private lateinit var moodButtons: Array<Button?>
    private lateinit var moodImages: Array<ImageView?>
    private lateinit var nextButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_flow)

        val returnHomeButton = findViewById<Button>(R.id.return_home_button)
        returnHomeButton.setOnClickListener {
            Log.d("home return button", "clicked!")
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }

        nextButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            Log.d("next button", "clicked!")
            if (activeMoodIndex != -1) {
                // TODO: replace with next activity
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
        }

        moodButtons = arrayOfNulls(5)
        moodImages = arrayOfNulls(5)

        for (index in 0..4) {
            moodButtons[index] = findViewById(getButtonIdByIndex(index))
            moodImages[index] = findViewById(getImageIdByIndex(index))
            moodButtons[index]?.setOnClickListener {
                Log.d("mood " + (index + 1) + " button", "clicked!")
                clickOnButton(index)
            }
        }
    }

    private fun getButtonIdByIndex(index: Int): Int {
        return when (index) {
            0 -> R.id.mood_1_button
            1 -> R.id.mood_2_button
            2 -> R.id.mood_3_button
            3 -> R.id.mood_4_button
            4 -> R.id.mood_5_button
            else -> -1 // unreachable
        }
    }

    private fun getImageIdByIndex(index: Int): Int {
        return when (index) {
            0 -> R.id.mood_1_image
            1 -> R.id.mood_2_image
            2 -> R.id.mood_3_image
            3 -> R.id.mood_4_image
            4 -> R.id.mood_5_image
            else -> -1 // unreachable
        }
    }

    private fun clickOnButton(index: Int) {
        if (index == activeMoodIndex) {
            moodImages[index]?.alpha = 0.5f
            activeMoodIndex = -1
            findViewById<CardView>(R.id.next_button_background).alpha = 0.5f
        } else {
            if (activeMoodIndex != -1) {
                moodImages[activeMoodIndex]?.alpha = 0.5f
            }
            moodImages[index]?.alpha = 1.0f
            activeMoodIndex = index
            findViewById<CardView>(R.id.next_button_background).alpha = 1.0f
        }
    }
}
