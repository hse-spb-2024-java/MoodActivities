package org.hse.moodactivities.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.StatisticItemAdapter
import org.hse.moodactivities.models.StatisticItem
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.StatisticMode
import org.hse.moodactivities.utils.UiUtils

class StatisticActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        findViewById<TextView>(R.id.return_tittle).text = buildString {
            append("< Insights")
        }

        findViewById<Button>(R.id.return_button).setOnClickListener {
            this.finish()
        }

        findViewById<TextView>(R.id.title).text = UiUtils.getStatisticTitle()

        ChartsService.createDistributionChart(findViewById<PieChart>(R.id.distribution_chart))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val gridLayoutManager =
            GridLayoutManager(
                applicationContext,
                1,
                LinearLayoutManager.VERTICAL,
                false
            )
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        val items = createItems()
        val itemsAdapters = applicationContext?.let {
            StatisticItemAdapter(it, items)
        }
        recyclerView?.adapter = itemsAdapters
    }

    private fun createItems(): ArrayList<StatisticItem> {
        // todo: replace with asking service
        // ChartService.getStatistic()
        // mock data
        return if (ChartsService.getStatisticMode() == StatisticMode.EMOTIONS) {
            arrayListOf(
                StatisticItem("emotion 1", 30, R.drawable.widget_mood_icon),
                StatisticItem("emotion 2", 20, R.drawable.widget_mood_icon),
                StatisticItem("emotion 3", 10, R.drawable.widget_mood_icon),
                StatisticItem("emotion 4", 5, R.drawable.widget_mood_icon)
            )
        } else {
            arrayListOf(
                StatisticItem("activity 1", 30, R.drawable.widget_mood_icon),
                StatisticItem("activity 2", 20, R.drawable.widget_mood_icon),
                StatisticItem("activity 3", 10, R.drawable.widget_mood_icon),
                StatisticItem("activity 4", 5, R.drawable.widget_mood_icon)
            )
        }
    }
}