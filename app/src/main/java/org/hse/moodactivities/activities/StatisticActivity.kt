package org.hse.moodactivities.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.StatisticItemAdapter
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.TimePeriod
import org.hse.moodactivities.utils.UiUtils

class StatisticActivity : AppCompatActivity() {
    private var currentTimePeriod: TimePeriod.Value = TimePeriod.Value.WEEK
    private var currentActiveCard: Int = R.id.week_background
    private var currentActiveText: Int = R.id.week_text
    private lateinit var chartsService: ChartsService
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        chartsService = ChartsService(this)

        findViewById<TextView>(R.id.return_tittle).text = buildString {
            append(UiUtils.Companion.Strings.RETURN_TO_INSIGHTS)
        }

        findViewById<Button>(R.id.return_button).setOnClickListener {
            this.finishActivity(0)
        }

        findViewById<Button>(R.id.week_button).setOnClickListener {
            activateTimePeriodBarButton(TimePeriod.Value.WEEK, R.id.week_background, R.id.week_text)
            chartsService.createDistributionChart(findViewById(R.id.distribution_chart), TimePeriod.Value.WEEK)
            val items = chartsService.getStatistic(TimePeriod.Value.WEEK)
            val itemsAdapters = applicationContext?.let {
                StatisticItemAdapter(it, items)
            }
            recyclerView.adapter = itemsAdapters
            recyclerView.adapter?.notifyDataSetChanged()
        }
        findViewById<Button>(R.id.month_button).setOnClickListener {
            activateTimePeriodBarButton(
                TimePeriod.Value.MONTH, R.id.month_background, R.id.month_text
            )
            chartsService.createDistributionChart(findViewById(R.id.distribution_chart), TimePeriod.Value.MONTH)
            val items = chartsService.getStatistic(TimePeriod.Value.WEEK)
            val itemsAdapters = applicationContext?.let {
                StatisticItemAdapter(it, items)
            }
            recyclerView.adapter = itemsAdapters
            recyclerView.adapter?.notifyDataSetChanged()
        }
        findViewById<Button>(R.id.year_button).setOnClickListener {
            activateTimePeriodBarButton(TimePeriod.Value.YEAR, R.id.year_background, R.id.year_text)
            chartsService.createDistributionChart(findViewById(R.id.distribution_chart), TimePeriod.Value.YEAR)
            val items = chartsService.getStatistic(TimePeriod.Value.WEEK)
            val itemsAdapters = applicationContext?.let {
                StatisticItemAdapter(it, items)
            }
            recyclerView.adapter = itemsAdapters
            recyclerView.adapter?.notifyDataSetChanged()
        }
        findViewById<Button>(R.id.all_time_button).setOnClickListener {
            activateTimePeriodBarButton(
                TimePeriod.Value.ALL_TIME, R.id.all_time_background, R.id.all_time_text
            )
            chartsService.createDistributionChart(findViewById(R.id.distribution_chart), TimePeriod.Value.ALL_TIME)
            val items = chartsService.getStatistic(TimePeriod.Value.WEEK)
            val itemsAdapters = applicationContext?.let {
                StatisticItemAdapter(it, items)
            }
            recyclerView.adapter = itemsAdapters
            recyclerView.adapter?.notifyDataSetChanged()
        }

        findViewById<TextView>(R.id.title).text = UiUtils.getStatisticTitle()

        chartsService.createDistributionChart(findViewById(R.id.distribution_chart), TimePeriod.Value.WEEK)

        recyclerView = findViewById(R.id.recycler_view)
        val gridLayoutManager = GridLayoutManager(
            applicationContext, 1, LinearLayoutManager.VERTICAL, false
        )
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val items = chartsService.getStatistic(TimePeriod.Value.WEEK)
        val itemsAdapters = applicationContext?.let {
            StatisticItemAdapter(it, items)
        }
        recyclerView.adapter = itemsAdapters
    }

    private fun activateTimePeriodBarButton(
        timePeriod: TimePeriod.Value, newActiveCard: Int, newActiveTittle: Int
    ) {
        currentTimePeriod = timePeriod
        findViewById<TextView>(currentActiveText).setTextColor(
            ContextCompat.getColor(
                applicationContext, R.color.light_slate_gray
            )
        )
        findViewById<CardView>(currentActiveCard).setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext, R.color.light_gray
            )
        )
        currentActiveCard = newActiveCard
        currentActiveText = newActiveTittle
        findViewById<TextView>(currentActiveText).setTextColor(
            ContextCompat.getColor(
                applicationContext, R.color.dark_slate_gray
            )
        )
        findViewById<CardView>(currentActiveCard).setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext, R.color.dark_gray
            )
        )
    }
}
