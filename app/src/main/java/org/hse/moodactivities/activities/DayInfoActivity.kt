package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.adapters.DailyItemAdapter
import org.hse.moodactivities.databinding.ActivityDayInfoBinding
import org.hse.moodactivities.models.DailyActivityModel
import org.hse.moodactivities.models.DailyInfoModel
import org.hse.moodactivities.models.DailyItemModel
import org.hse.moodactivities.models.DailyQuestionModel
import org.hse.moodactivities.models.MoodActivity
import org.hse.moodactivities.models.MoodEmotion
import org.hse.moodactivities.services.CalendarService

class DayInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDayInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // button to return to calendar
        binding.returnHomeButton.setOnClickListener {
            this.finish()
        }

        // set chosen date
        binding.dayInfoTittle.text = CalendarService.getDate()

        // create
        setWidgets()
    }

    private fun setWidgets() {
        val widgets = getWidgets()
        val dailyItemAdapter = DailyItemAdapter(widgets)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(this, 1)
        binding.calendarDayRecyclerView.setLayoutManager(layoutManager)
        binding.calendarDayRecyclerView.setAdapter(dailyItemAdapter)
    }

    private fun getWidgets(): ArrayList<DailyItemModel> {
        // todo: get result from server
        return arrayListOf(
            DailyActivityModel(
                "Drink a glass of water",
                "It was amazing!!!!! I really like to drink water!!!!!",
                "00:30"
            ),
            DailyQuestionModel(
                "What is your favourite color?",
                "White!!!",
                "10:10"
            ),
            DailyInfoModel(
                "It was quite regular day",
                "22:17",
                3,
                "How do you feel?",
                "Really cool",
                arrayListOf(MoodActivity("reading"), MoodActivity("dancing")),
                arrayListOf(MoodEmotion("love")),
            )
        )
    }
}