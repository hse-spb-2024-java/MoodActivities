package org.hse.moodactivities.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.hse.moodactivities.R
import org.hse.moodactivities.databinding.ActivityDailyActivityBinding
import org.hse.moodactivities.services.ActivityService
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class DailyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyActivityBinding
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set return home button
        binding.returnHomeButton.setOnClickListener {
            this.finish()
        }

        // set current date
        binding.date.text = LocalDate.now().toString()

        // set daily activity if it's available
        val dailyActivity = ActivityService.getDailyActivity(this)
        if (dailyActivity != null) {
            setActivity(dailyActivity)
        } else {
            setActivityUnavailableMessage()
        }

        // create dialog to ask user's impressions about today's activity
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_activity_impressions)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true)

        // set button to complete activity
        binding.completeButton.setOnClickListener {
            dialog.show()
        }

        dialog.findViewById<Button>(R.id.finish_button).setOnClickListener {
            val usersImpressions =
                dialog.findViewById<TextView>(R.id.users_impressions).text.toString()
            ActivityService.recordDailyActivity(this, usersImpressions)
            dialog.dismiss()
        }

    }

    private fun setActivity(dailyActivity: String) {
        // set activity
        binding.activityText.text = dailyActivity

        // set timer to countdown for activity completion
        val millsUntilEndOfTheDay = getMillisUntilEndOfTheDay()
        val timer = object : CountDownTimer(millsUntilEndOfTheDay, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.timer.text = "time left: ${hours}:${minutes}:${seconds}"
            }

            override fun onFinish() {
                // recreate window to get new activity
                finish()
                startActivity(intent)
            }
        }
        timer.start()
    }

    private fun getMillisUntilEndOfTheDay(): Long {
        val now = LocalDateTime.now()
        val endOfTheDay = now.with(LocalTime.MAX)
        val durationUntilEndOfDay = Duration.between(now, endOfTheDay)
        return durationUntilEndOfDay.toMillis()
    }

    private fun setActivityUnavailableMessage() {
        binding.activityTittle.text = UNAVAILABLE_MESSAGE
        binding.activityTextBackground.alpha = 0f
        binding.completedButtonBackground.alpha = 0f
    }

    companion object {
        const val UNAVAILABLE_MESSAGE = "You completed today's activity,\ncome back later!"
    }
}
