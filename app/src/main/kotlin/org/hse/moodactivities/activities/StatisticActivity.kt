package org.hse.moodactivities.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        findViewById<TextView>(R.id.title).text = UiUtils.getStatisticTitle()
        findViewById<TextView>(R.id.title).setTextColor(
            ThemesService.getColorTheme().getFontColor()
        )

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
        val colorTheme = ThemesService.getColorTheme()

        currentTimePeriod = timePeriod
        findViewById<TextView>(currentActiveTextId).setTextColor(
            colorTheme.getDimmedBackgroundColor()
        )
        findViewById<CardView>(currentActiveCardId).setCardBackgroundColor(
            colorTheme.getTimePeriodBarColor()
        )
        currentActiveCardId = newActiveCard
        currentActiveTextId = newActiveTittle
        findViewById<TextView>(currentActiveTextId).setTextColor(
            colorTheme.getBackgroundColor()
        )
        findViewById<CardView>(currentActiveCardId).setCardBackgroundColor(
            colorTheme.getTimePeriodBarButtonColor()
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
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        window.statusBarColor = colorTheme.getBackgroundColor()

        // set color to background
        findViewById<ConstraintLayout>(R.id.activity_statistic).setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to return button
        findViewById<ImageView>(R.id.return_image)?.setColorFilter(colorTheme.getFontColor())

        // set color to bar background
        findViewById<CardView>(R.id.bar_background).setCardBackgroundColor(colorTheme.getTimePeriodBarColor())

        // set color to bars buttons
        findViewById<CardView>(R.id.week_background).setCardBackgroundColor(colorTheme.getTimePeriodBarButtonColor())
        findViewById<CardView>(R.id.month_background).setCardBackgroundColor(colorTheme.getTimePeriodBarColor())
        findViewById<CardView>(R.id.year_background).setCardBackgroundColor(colorTheme.getTimePeriodBarColor())
        findViewById<CardView>(R.id.all_time_background).setCardBackgroundColor(colorTheme.getTimePeriodBarColor())

        // set color to bars buttons texts
        findViewById<TextView>(R.id.week_text).setTextColor(colorTheme.getBackgroundColor())
        findViewById<TextView>(R.id.month_text).setTextColor(colorTheme.getDimmedBackgroundColor())
        findViewById<TextView>(R.id.year_text).setTextColor(colorTheme.getDimmedBackgroundColor())
        findViewById<TextView>(R.id.all_time_text).setTextColor(colorTheme.getDimmedBackgroundColor())
    }
}
