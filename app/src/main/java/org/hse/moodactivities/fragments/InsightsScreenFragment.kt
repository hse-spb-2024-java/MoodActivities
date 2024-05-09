package org.hse.moodactivities.fragments

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.StatisticActivity
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.StatisticMode
import org.hse.moodactivities.services.TimePeriod

class InsightsScreenFragment : Fragment() {
    companion object {
        val DEFAULT_TIME_PERIOD = TimePeriod.Value.WEEK
    }

    enum class ChartsType {
        ACTIVITIES_CHART, EMOTIONS_CHART, MOOD_CHART
    }

    private lateinit var dialog: Dialog
    private lateinit var resources: Resources

    // charts settings
    private lateinit var chartsService: ChartsService
    private lateinit var moodChart: LineChart
    private var chartType: ChartsType = ChartsType.MOOD_CHART
    private lateinit var moodChartLabel: TextView
    private lateinit var emotionsChartLabel: TextView
    private lateinit var activitiesChartLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setTimePeriodToChart(timePeriod: TimePeriod.Value) {
        when (chartType) {
            ChartsType.MOOD_CHART -> {
                chartsService.createMoodCharts(resources, moodChart, timePeriod)
                changeTimeLabel(moodChartLabel, timePeriod)
            }

            ChartsType.EMOTIONS_CHART -> {
                chartsService.createFrequentlyUsedEmotions(resources, requireView(), timePeriod)
                changeTimeLabel(emotionsChartLabel, timePeriod)
            }

            ChartsType.ACTIVITIES_CHART -> {
                chartsService.createFrequentlyUsedEmotions(resources, requireView(), timePeriod)
                changeTimeLabel(activitiesChartLabel, timePeriod)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_insights_screen, container, false)
        resources = this.requireActivity().resources
        chartsService = ChartsService(this.requireActivity() as AppCompatActivity)

        // create mood chart
        moodChart = view.findViewById<LineChart>(R.id.week_mood_chart)
        moodChartLabel = view.findViewById(R.id.mood_flow_time_label)
        changeTimeLabel(moodChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createMoodCharts(resources, moodChart, DEFAULT_TIME_PERIOD)

        // create emotions chart
        emotionsChartLabel = view.findViewById(R.id.emotions_time_label)
        changeTimeLabel(emotionsChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createFrequentlyUsedEmotions(resources, view, DEFAULT_TIME_PERIOD)
        view.findViewById<Button>(R.id.emotions_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.EMOTIONS)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
        }

        // create activities chart
        activitiesChartLabel = view.findViewById(R.id.activities_time_label)
        changeTimeLabel(activitiesChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createFrequentlyUsedActivities(resources, view, DEFAULT_TIME_PERIOD)
        view.findViewById<Button>(R.id.activities_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.ACTIVITIES)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
            this.activity?.finish()
        }

        // create days in row
        chartsService.createDaysInRow(view.findViewById(R.id.days_in_row))

        // create dialog to change time periods in charts
        dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.dialog_choose_time_period)
        dialog.setCancelable(true)

        // set week button
        dialog.findViewById<Button>(R.id.week).setOnClickListener {
            pressDialogButton(TimePeriod.Value.WEEK)
        }
        // set month button
        dialog.findViewById<Button>(R.id.month).setOnClickListener {
            pressDialogButton(TimePeriod.Value.MONTH)
        }
        // set year button
        dialog.findViewById<Button>(R.id.year).setOnClickListener {
            pressDialogButton(TimePeriod.Value.YEAR)
        }
        // set all time button
        dialog.findViewById<Button>(R.id.all_time).setOnClickListener {
            pressDialogButton(TimePeriod.Value.ALL_TIME)
        }

        // create time labels to change time periods
        // create mood time label
        view.findViewById<Button>(R.id.mood_flow_time_label_button).setOnClickListener {
            chartType = ChartsType.MOOD_CHART
            dialog.show()
        }
        // create emotions time label
        view.findViewById<Button>(R.id.emotions_time_label_button).setOnClickListener {
            chartType = ChartsType.EMOTIONS_CHART
            dialog.show()
        }
        // create activities time label
        view.findViewById<Button>(R.id.activities_time_label_button).setOnClickListener {
            chartType = ChartsType.ACTIVITIES_CHART
            dialog.show()
        }

        return view
    }

    private fun changeTimeLabel(textView: TextView, timePeriod: TimePeriod.Value) {
        textView.text = timePeriod.toString()
    }

    private fun pressDialogButton(timePeriod: TimePeriod.Value) {
        setTimePeriodToChart(timePeriod)
        dialog.dismiss()
    }
}
