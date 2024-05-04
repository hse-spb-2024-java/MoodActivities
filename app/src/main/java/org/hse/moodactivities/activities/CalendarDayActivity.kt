package org.hse.moodactivities.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.adapters.CalendarDayAdapter
import org.hse.moodactivities.databinding.ActivityCalendarDayBinding
import org.hse.moodactivities.models.DailyActivityItemModel
import org.hse.moodactivities.models.DailyEmptyItemModel
import org.hse.moodactivities.models.DailyInfoItemModel
import org.hse.moodactivities.models.DailyItemModel
import org.hse.moodactivities.models.MoodActivity
import org.hse.moodactivities.models.MoodEmotion
import org.hse.moodactivities.services.CalendarService
import java.time.format.DateTimeFormatter

class CalendarDayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarDayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // button to return to calendar
        binding.returnHomeButton.setOnClickListener {
            this.finish()
        }

        // set chosen date
        val date = CalendarService.getDate()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        binding.dayInfoTittle.text = date.format(formatter)

        // create widgets
        setWidgets()
    }

    private fun setWidgets() {
        val widgets = getWidgets()
        val calendarDayAdapter = CalendarDayAdapter(widgets)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
        binding.calendarDayRecyclerView.setLayoutManager(layoutManager)
        binding.calendarDayRecyclerView.setAdapter(calendarDayAdapter)
    }

    private fun getWidgets(): ArrayList<DailyItemModel> {
        val response = CalendarService.getFullDayReportResponse(CalendarService.getDate(), this)

        // setup widgets
        val widgets = ArrayList<DailyItemModel>()
        // setup daily activity
        val dailyActivity = response.getDailyActivity()
        if (dailyActivity.getDailyActivity().isNotEmpty()) {
            val dailyActivityItemModel = DailyActivityItemModel(
                dailyActivity.getDailyActivity(),
                dailyActivity.getUserImpressions(),
                dailyActivity.getTime()
            )
            widgets.add(dailyActivityItemModel)
        }
        // setup daily question
        val dailyQuestion = response.getDailyQuestion()
        if (dailyQuestion.getDailyQuestion().isNotEmpty()) {
            val dailyQuestionModel = DailyActivityItemModel(
                dailyQuestion.getDailyQuestion(),
                dailyQuestion.getAnswerToDailyQuestion(),
                dailyQuestion.getTime()
            )
            widgets.add(dailyQuestionModel)
        }
        // setup mood records
        val moodEvents = response.getMoodEvents()
        val moodRecordsCount = moodEvents.size - 1
        for (pos in 0..moodRecordsCount) {
            val moodRecord = moodEvents[pos]
            val activities = ArrayList<MoodActivity>()
            for (activity in moodRecord.getChosenActivities()!!) {
                activities.add(MoodActivity(activity))
            }
            val emotions = ArrayList<MoodEmotion>()
            for (emotion in moodRecord.getChosenEmotions()!!) {
                emotions.add(MoodEmotion(emotion))
            }
            widgets.add(
                DailyInfoItemModel(
                    moodRecord.getDescription()!!,
                    moodRecord.getMoodRate()!!,
                    moodRecord.getQuestion()!!,
                    moodRecord.getUserAnswer()!!,
                    activities,
                    emotions,
                    moodRecord.getTime()!!
                )
            )
        }
        // add empty widget to show that there is no data for that day
        if (widgets.isEmpty()) {
            widgets.add(DailyEmptyItemModel("00:00"))
        }
        // sort by time
        widgets.sortWith { widget1, widget2 ->
            widget2.getTime().compareTo(widget1.getTime())
        }
        return widgets
    }
}
