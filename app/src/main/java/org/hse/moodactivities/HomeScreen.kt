package org.hse.moodactivities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.time.DayOfWeek
import java.time.LocalDate

class HomeScreen : Fragment() {
    companion object {
        private val dayOfWeekTextId: Array<Int> = arrayOf(
            R.id.week_widget_day_of_week_5, R.id.week_widget_day_of_week_4,
            R.id.week_widget_day_of_week_3, R.id.week_widget_day_of_week_2,
            R.id.week_widget_day_of_week_1
        )
        private val dayOfMonthTextId: Array<Int> = arrayOf(
            R.id.week_widget_day_of_month_5, R.id.week_widget_day_of_month_4,
            R.id.week_widget_day_of_month_3, R.id.week_widget_day_of_month_2,
            R.id.week_widget_day_of_month_1
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        setCurrentDate(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityWidgetButton: Button = view.rootView.findViewById(R.id.activity_widget_button)
        activityWidgetButton.setOnClickListener {
            Log.d("activity button", "clicked!")
        }

        val moodWidgetButton: Button = view.rootView.findViewById(R.id.mood_widget_button)
        moodWidgetButton.setOnClickListener {
            Log.d("mood button", "clicked!")
        }

        val noteWidgetButton: Button = view.rootView.findViewById(R.id.note_widget_button)
        noteWidgetButton.setOnClickListener {
            Log.d("note button", "clicked!")
        }
    }

    private fun setCurrentDate(view: View) {
        val localDate = LocalDate.now()

        for (daysBefore in 0L..4L) {
            val localDateBefore = localDate.minusDays(daysBefore)
            setDateToWeekWidget(
                localDateBefore.dayOfWeek, localDateBefore.dayOfMonth,
                view.rootView.findViewById(dayOfWeekTextId[daysBefore.toInt()]),
                view.rootView.findViewById(dayOfMonthTextId[daysBefore.toInt()])
            )
        }
    }

    private fun setDateToWeekWidget(
        dayOfWeek: DayOfWeek,
        day: Int,
        dayOfWeekTextView: TextView,
        dayTextView: TextView
    ) {
        dayOfWeekTextView.text = getString(getStringByDayOfWeek(dayOfWeek))
        dayTextView.text = day.toString()
    }

    private fun getStringByDayOfWeek(dayOfWeek: DayOfWeek): Int {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> R.string.monday
            DayOfWeek.TUESDAY -> R.string.tuesday
            DayOfWeek.WEDNESDAY -> R.string.wednesday
            DayOfWeek.THURSDAY -> R.string.thursday
            DayOfWeek.FRIDAY -> R.string.friday
            DayOfWeek.SATURDAY -> R.string.saturday
            DayOfWeek.SUNDAY -> R.string.sunday
        }
    }
}
