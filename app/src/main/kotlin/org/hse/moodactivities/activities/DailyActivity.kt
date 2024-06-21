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
import androidx.cardview.widget.CardView
import org.hse.moodactivities.R
import org.hse.moodactivities.databinding.ActivityDailyActivityBinding
import org.hse.moodactivities.services.ActivityService
import org.hse.moodactivities.services.ThemesService
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)
        binding.date.text = formattedDate

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
            this.finish()
        }

        setColorTheme()
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
                val formattedHours = if (hours < 10) "0$hours" else hours
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val formattedMinutes = if (minutes < 10) "0$minutes" else minutes
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                val formattedSeconds = if (seconds < 10) "0$seconds" else seconds
                binding.timer.text =
                    "time left: ${formattedHours}:${formattedMinutes}:${formattedSeconds}"
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

    private fun setColorTheme() {
        val colorTheme = ThemesService.getColorTheme()
        // set color to status bar
        window.statusBarColor = colorTheme.getBackgroundColor()

        // set background color
        binding.dailyActivityLayout.setBackgroundColor(colorTheme.getBackgroundColor())

        // set title color
        binding.screenTittle.setTextColor(colorTheme.getFontColor())

        // set date color
        binding.date.setTextColor(colorTheme.getFontColor())

        // set activity widget color
        binding.activityTextBackground.setCardBackgroundColor(colorTheme.getDailyActivityWidgetColor())

        // set activity text color
        binding.activityText.setTextColor(colorTheme.getDailyActivityWidgetTextColor())
        binding.activityTittle.setTextColor(colorTheme.getFontColor())

        // set complete button color
        binding.completedButtonBackground.setCardBackgroundColor(colorTheme.getDailyActivityWidgetIconColor())
        binding.completedButtonText.setTextColor(colorTheme.getDailyActivityWidgetIconTextColor())

        // set timer color
        binding.timer.setTextColor(colorTheme.getFontColor())

        // set dialog background color
        dialog.findViewById<CardView>(R.id.dialog_background)
            .setCardBackgroundColor(colorTheme.getDailyActivityWidgetColor())

        // set dialog question text color
        dialog.findViewById<TextView>(R.id.dialog_tittle)
            .setTextColor(colorTheme.getDailyActivityWidgetTextColor())

        // set dialog text color
        dialog.findViewById<TextView>(R.id.users_impressions)
            .setTextColor(colorTheme.getDailyActivityWidgetTextColor())

        // set dialog button color
        dialog.findViewById<CardView>(R.id.button_background)
            .setCardBackgroundColor(colorTheme.getDailyActivityWidgetIconColor())

        // set dialog button text color
        dialog.findViewById<TextView>(R.id.button_text)
            .setTextColor(colorTheme.getDailyActivityWidgetIconTextColor())
    }
}
