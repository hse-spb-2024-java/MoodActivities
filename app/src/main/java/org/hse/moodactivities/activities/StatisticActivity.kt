package org.hse.moodactivities.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.StatisticItemAdapter
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.services.TimePeriod
import org.hse.moodactivities.utils.UiUtils

class StatisticActivity : AppCompatActivity() {
    private var currentTimePeriod: TimePeriod.Value = TimePeriod.Value.WEEK
    private var currentActiveCardId: Int = R.id.week_background
    private var currentActiveTextId: Int = R.id.week_text
    private lateinit var chartsService: ChartsService
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        chartsService = ChartsService(this)

        // set screen tittles
        findViewById<TextView>(R.id.return_tittle).text = buildString {
            append(UiUtils.Companion.Strings.RETURN_TO_INSIGHTS)
        }
        findViewById<TextView>(R.id.title).text = UiUtils.getStatisticTitle()
        findViewById<TextView>(R.id.title).setTextColor(ThemesService.getFontColor())

        // button to return to insights
        findViewById<Button>(R.id.return_button).setOnClickListener {
            this.finish()
        }

        // set buttons events in choose time bar
        findViewById<Button>(R.id.week_button).setOnClickListener {
            setData(TimePeriod.Value.WEEK, R.id.week_background, R.id.week_text)
        }
        findViewById<Button>(R.id.month_button).setOnClickListener {
            setData(TimePeriod.Value.MONTH, R.id.month_background, R.id.month_text)
        }
        findViewById<Button>(R.id.year_button).setOnClickListener {
            setData(TimePeriod.Value.YEAR, R.id.year_background, R.id.year_text)
        }
        findViewById<Button>(R.id.all_time_button).setOnClickListener {
            setData(TimePeriod.Value.ALL_TIME, R.id.all_time_background, R.id.all_time_text)
        }

        // init recycler view
        recyclerView = findViewById(R.id.recycler_view)
        val gridLayoutManager = GridLayoutManager(
            applicationContext, 1, LinearLayoutManager.VERTICAL, false
        )
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)

        // init data
        setData(TimePeriod.Value.WEEK, R.id.week_background, R.id.week_text)

        setColorTheme()
    }

    private fun activateTimePeriodBarButton(
        timePeriod: TimePeriod.Value, newActiveCard: Int, newActiveTittle: Int
    ) {
        currentTimePeriod = timePeriod
        findViewById<TextView>(currentActiveTextId).setTextColor(
            ThemesService.getDimmedBackgroundColor()
        )
        findViewById<CardView>(currentActiveCardId).setCardBackgroundColor(
            ThemesService.getColor5()
        )
        currentActiveCardId = newActiveCard
        currentActiveTextId = newActiveTittle
        findViewById<TextView>(currentActiveTextId).setTextColor(
            ThemesService.getBackgroundColor()
        )
        findViewById<CardView>(currentActiveCardId).setCardBackgroundColor(
            ThemesService.getDimmedColor5()
        )
    }

    private fun setData(
        timePeriod: TimePeriod.Value, buttonBackgroundId: Int, buttonTextId: Int
    ) {
        activateTimePeriodBarButton(timePeriod, buttonBackgroundId, buttonTextId)
        chartsService.createDistributionChart(
            findViewById(R.id.distribution_chart), timePeriod
        )
        val items = chartsService.getStatistic(timePeriod)
        val itemsAdapters = applicationContext?.let {
            StatisticItemAdapter(it, items)
        }
        recyclerView.adapter = itemsAdapters
    }

    private fun setColorTheme() {
        // set color to status bar
        window.statusBarColor = ThemesService.getBackgroundColor()

        // set color to background
        findViewById<ConstraintLayout>(R.id.activity_statistic).setBackgroundColor(ThemesService.getBackgroundColor())

        // set color to bar background
        findViewById<CardView>(R.id.bar_background).setCardBackgroundColor(ThemesService.getColor5())

        // set color to bars buttons
        findViewById<CardView>(R.id.week_background).setCardBackgroundColor(ThemesService.getDimmedColor5())
        findViewById<CardView>(R.id.month_background).setCardBackgroundColor(ThemesService.getColor5())
        findViewById<CardView>(R.id.year_background).setCardBackgroundColor(ThemesService.getColor5())
        findViewById<CardView>(R.id.all_time_background).setCardBackgroundColor(ThemesService.getColor5())

        // set color to bars buttons texts
        findViewById<TextView>(R.id.week_text).setTextColor(ThemesService.getBackgroundColor())
        findViewById<TextView>(R.id.month_text).setTextColor(ThemesService.getDimmedBackgroundColor())
        findViewById<TextView>(R.id.year_text).setTextColor(ThemesService.getDimmedBackgroundColor())
        findViewById<TextView>(R.id.all_time_text).setTextColor(ThemesService.getDimmedBackgroundColor())
    }
}
