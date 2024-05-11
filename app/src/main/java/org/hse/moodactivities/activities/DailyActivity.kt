package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.hse.moodactivities.databinding.ActivityDailyActivityBinding
import java.time.LocalDate

class DailyActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityDailyActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnHomeButton.setOnClickListener {
            this.finish()
        }

        binding.date.text = LocalDate.now().toString()
    }

    private fun updateTimer() {

    }
}