package org.hse.moodactivities.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.adapters.CalendarDayAdapter
import org.hse.moodactivities.databinding.ActivityCalendarDayBinding
import org.hse.moodactivities.models.DailyActivityItemModel
import org.hse.moodactivities.models.DailyEmptyItemModel
import org.hse.moodactivities.models.DailyInfoItemModel
import org.hse.moodactivities.models.DailyItemModel
import org.hse.moodactivities.models.DailyItemModel.Companion.DEFAULT_TIME
import org.hse.moodactivities.models.MoodActivity
import org.hse.moodactivities.models.MoodEmotion
import org.hse.moodactivities.services.CalendarService
import java.time.format.DateTimeFormatter

class CalendarDayActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "calendar day"
    }

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

        Log.i(LOG_TAG, "Show user's information for the date " + binding.dayInfoTittle.text)
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
        } else {
            Log.d(
                LOG_TAG, "User hasn't completed the activity for " + binding.dayInfoTittle.text
            )
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
        } else {
            Log.d(
                LOG_TAG, "User hasn't answered the question for " + binding.dayInfoTittle.text
            )
        }
        // setup mood records
        val moodEvents = response.getMoodEvents()
        for (pos in 0..<moodEvents.size) {
            val moodRecord = moodEvents[pos]

            // set activities
            val activities = ArrayList<MoodActivity>()
            if (moodRecord.getChosenActivities().isNullOrEmpty()) {
                Log.d(LOG_TAG, "Activities in mood record are empty")
            } else {
                for (activity in moodRecord.getChosenActivities()!!) {
                    activities.add(MoodActivity(activity))
                }
            }
            // set emotions
            val emotions = ArrayList<MoodEmotion>()
            if (moodRecord.getChosenEmotions().isNullOrEmpty()) {
                Log.d(LOG_TAG, "Emotions in mood record are empty")
            } else {
                for (emotion in moodRecord.getChosenEmotions()!!) {
                    emotions.add(MoodEmotion(emotion))
                }
            }

            widgets.add(
                DailyInfoItemModel(
                    // TODO: add description after adding to mood flow
                    "",
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
            widgets.add(DailyEmptyItemModel(DEFAULT_TIME))
            Log.d(LOG_TAG, "There is no information for the date " + binding.dayInfoTittle.text)
        }
        // sort by time
        widgets.sortWith { widget1, widget2 ->
            widget2.getTime().compareTo(widget1.getTime())
        }
        return widgets
    }
}
