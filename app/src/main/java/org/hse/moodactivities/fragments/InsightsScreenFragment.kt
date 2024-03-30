package org.hse.moodactivities.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.StatisticActivity
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.StatisticMode
import org.hse.moodactivities.services.TimePeriod

class InsightsScreenFragment : Fragment() {
    private var moodFlowTimePeriod: TimePeriod = TimePeriod(TimePeriod.Value.WEEK)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun showChoiceTimePeriodAlert(label: TextView, timePeriod: TimePeriod) {
        val items = arrayOf("Week", "Month", "Year", "All time")
        val builder = AlertDialog.Builder(this.requireContext())

        builder.setTitle("Choose time period")
        builder.setItems(items) { _, index ->
            label.text = items[index]
            timePeriod.value = TIME_PERIODS[index]
            // todo: pass function to call as parameter
        }

        builder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_time_period, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_insights_screen, container, false)

        view.findViewById<Button>(R.id.mood_flow_time_label_button).setOnClickListener {
            showChoiceTimePeriodAlert(
                view.findViewById(R.id.mood_flow_time_label), moodFlowTimePeriod
            )
        }

        val weekMoodChart = view.findViewById<LineChart>(R.id.week_mood_chart)
        ChartsService.createWeekMoodCharts(this.requireActivity().resources, weekMoodChart)

        ChartsService.createFrequentlyUsedEmotions(this.requireActivity().resources, view)
        ChartsService.createFrequentlyUsedActivities(this.requireActivity().resources, view)

        view.findViewById<Button>(R.id.emotions_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.EMOTIONS)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
        }

        view.findViewById<Button>(R.id.emotions_time_label_button).setOnClickListener {
            showChoiceTimePeriodAlert(
                view.findViewById(R.id.emotions_time_label), moodFlowTimePeriod
            )
        }

        view.findViewById<Button>(R.id.activities_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.ACTIVITIES)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
            this.activity?.finish()
        }

        view.findViewById<Button>(R.id.activities_time_label_button).setOnClickListener {
            showChoiceTimePeriodAlert(
                view.findViewById(R.id.activities_time_label), moodFlowTimePeriod
            )
        }

        ChartsService.createDaysInRow(view.findViewById(R.id.days_in_row))

        return view
    }

    companion object {
        private val TIME_PERIODS = arrayOf(
            TimePeriod.Value.WEEK,
            TimePeriod.Value.MONTH,
            TimePeriod.Value.YEAR,
            TimePeriod.Value.ALL_TIME
        )
    }
}
