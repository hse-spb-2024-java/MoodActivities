package org.hse.moodactivities.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.StatisticItemAdapter
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.utils.UiUtils

class StatisticActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        findViewById<TextView>(R.id.return_tittle).text = buildString {
            append(UiUtils.Companion.Strings.RETURN_TO_INSIGHTS)
        }

        findViewById<Button>(R.id.return_button).setOnClickListener {
            this.finishActivity(0)
        }

        findViewById<TextView>(R.id.title).text = UiUtils.getStatisticTitle()

        ChartsService.createDistributionChart(findViewById(R.id.distribution_chart))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val gridLayoutManager = GridLayoutManager(
            applicationContext, 1, LinearLayoutManager.VERTICAL, false
        )
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        val items = ChartsService.getStatistic()
        val itemsAdapters = applicationContext?.let {
            StatisticItemAdapter(it, items)
        }
        recyclerView?.adapter = itemsAdapters
    }
}
