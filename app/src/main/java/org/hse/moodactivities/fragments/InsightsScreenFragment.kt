package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.StatisticActivity
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.StatisticMode

class InsightsScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_insights_screen, container, false)

        val weekMoodChart = view.findViewById<LineChart>(R.id.week_mood_chart)
        ChartsService.createWeekMoodCharts(this.requireActivity().resources, weekMoodChart)

        ChartsService.createFrequentlyUsedEmotions(this.requireActivity().resources, view)
        ChartsService.createFrequentlyUsedActivities(this.requireActivity().resources, view)

        view.findViewById<Button>(R.id.emotions_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.EMOTIONS)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
        }
        view.findViewById<Button>(R.id.activities_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.ACTIVITIES)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
            this.activity?.finish()
        }

        ChartsService.createDaysInRow(view.findViewById(R.id.days_in_row))

        return view
    }
}
