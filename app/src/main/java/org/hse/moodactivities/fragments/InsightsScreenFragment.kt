package org.hse.moodactivities.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.StatisticActivity
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.StatisticMode
import java.time.DayOfWeek


class InsightsScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_insights_screen, container, false)

        val lineChart = view.findViewById<LineChart>(R.id.line_chart)
        ChartsService.createWeekMoodCharts(this.requireActivity().resources, lineChart)

        ChartsService.createFrequentlyUsedEmotions(this.requireActivity().resources, view)
        ChartsService.createFrequentlyUsedActivities(this.requireActivity().resources, view)

        view.findViewById<Button>(R.id.emotions_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.EMOTIONS)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
        }

        view.findViewById<Button>(R.id.activities_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.ACTIVITIES)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
        }
        createDaysInRow(view)

        return view
    }

    private val daysCardId: Array<Int> = arrayOf(
        R.id.day_card_1, R.id.day_card_2,
        R.id.day_card_3, R.id.day_card_4,
        R.id.day_card_5, R.id.day_card_6,
        R.id.day_card_7
    )
    private val daysTittleId: Array<Int> = arrayOf(
        R.id.day_tittle_1, R.id.day_tittle_2,
        R.id.day_tittle_3, R.id.day_tittle_4,
        R.id.day_tittle_5, R.id.day_tittle_6,
        R.id.day_tittle_7
    )

    private fun createDaysInRow(view: View?) {
        // mock data
        val days = arrayOf(
            ChartsService.DayData(true, DayOfWeek.MONDAY),
            ChartsService.DayData(false, DayOfWeek.TUESDAY),
            ChartsService.DayData(false, DayOfWeek.WEDNESDAY),
            ChartsService.DayData(true, DayOfWeek.THURSDAY),
            ChartsService.DayData(true, DayOfWeek.FRIDAY),
            ChartsService.DayData(true, DayOfWeek.SATURDAY),
            ChartsService.DayData(true, DayOfWeek.SUNDAY),
        )

        for (i in 0..6) {
            val color: Int = if (days[i].isNoted) {
                Color.parseColor("#98FB98")
            } else {
                Color.parseColor("#FF6347")
            }
            view?.findViewById<CardView>(daysCardId[i])?.setCardBackgroundColor(color)
            view?.findViewById<TextView>(daysTittleId[i])?.text = days[i].week.name
        }
    }
}
