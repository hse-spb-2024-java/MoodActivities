package org.hse.moodactivities.services

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import org.hse.moodactivities.R
import org.hse.moodactivities.models.StatisticItem
import org.hse.moodactivities.utils.UiUtils
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.random.Random

enum class StatisticMode {
    EMOTIONS, ACTIVITIES;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}

class TimePeriod(var value: Value) {
    enum class Value {
        WEEK, MONTH, YEAR, ALL_TIME;

        override fun toString(): String {
            if (this == ALL_TIME) {
                return "all time"
            }
            return super.toString().lowercase()
        }
    }
}



class ChartsService {
    companion object {
        private var statisticMode: StatisticMode = StatisticMode.EMOTIONS

        private class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                // todo: parse date based on response
                var date = LocalDate.now()
                date = date.minusDays(6 - value.toLong())
                return date.dayOfMonth.toString() + "/" + date.month.value
            }
        }

        fun getStatisticMode(): StatisticMode {
            return statisticMode
        }

        fun getStatistic(): ArrayList<StatisticItem> {
            // mock data
            // todo: add custom icons (create frontend class to manipulate with emotions and activities)
            return if (getStatisticMode() == StatisticMode.EMOTIONS) {
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

        fun setStatisticMode(statisticMode: StatisticMode) {
            this.statisticMode = statisticMode
        }

        private object DaysInRowSettings {
            const val RECORDED_COLOR = "#98FB98"
            const val NOT_RECORDED_COLOR = "#FF6347"
            const val CARD_INDEX = 0
            const val TEXT_INDEX = 2
            const val TEXT_SIZE = 12f
            const val TEXT_COLOR = Color.BLACK
        }

        fun createDaysInRow(daysInRow: LinearLayout) {
            // mock data
            val days = arrayOf(
                DayData(true, DayOfWeek.MONDAY),
                DayData(false, DayOfWeek.TUESDAY),
                DayData(false, DayOfWeek.WEDNESDAY),
                DayData(true, DayOfWeek.THURSDAY),
                DayData(true, DayOfWeek.FRIDAY),
                DayData(true, DayOfWeek.SATURDAY),
                DayData(true, DayOfWeek.SUNDAY),
            )

            for (dayIndex in 0..6) {
                val layout = daysInRow.getChildAt(2 * dayIndex) as LinearLayout

                val color: Int = if (days[dayIndex].isRecorded) {
                    Color.parseColor(DaysInRowSettings.RECORDED_COLOR)
                } else {
                    Color.parseColor(DaysInRowSettings.NOT_RECORDED_COLOR)
                }
                val card = layout.getChildAt(DaysInRowSettings.CARD_INDEX) as CardView
                card.setCardBackgroundColor(color)

                val text = layout.getChildAt(DaysInRowSettings.TEXT_INDEX) as TextView
                text.text = days[dayIndex].week.name.lowercase().subSequence(0, 3)
                text.textSize = DaysInRowSettings.TEXT_SIZE
                text.setTypeface(text.typeface, Typeface.BOLD)
                text.textAlignment = View.TEXT_ALIGNMENT_CENTER
                text.setTextColor(DaysInRowSettings.TEXT_COLOR)
            }
        }

        private object DistributionChartSettings {
            const val HOLE_RADIUS = 58f
            const val TEXT_SIZE = 14f
        }

        fun createDistributionChart(pieChart: PieChart) {
            val statistic = getStatistic()

            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.holeRadius = DistributionChartSettings.HOLE_RADIUS
            pieChart.isRotationEnabled = true
            pieChart.isHighlightPerTapEnabled = true

            val entries = ArrayList<PieEntry>()
            for (entry: StatisticItem in statistic) {
                entries.add(PieEntry(entry.getCounter().toFloat()))
            }

            val dataSet = PieDataSet(entries, "statistic")
            dataSet.setDrawIcons(false)

            val colors = ArrayList<Int>()
            val rnd = Random.Default
            for (color in 0..statistic.size) {
                colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
            }
            dataSet.colors = colors

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(DistributionChartSettings.TEXT_SIZE)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)

            pieChart.data = data

            pieChart.legend.isEnabled = false

            pieChart.invalidate()
        }

        private fun getWeekMoodData(resources: Resources): MutableList<Entry> {
            // todo: replace with data from server response
            // mock data
            val mockEntries: MutableList<Entry> = ArrayList()
            for (dayOfWeek in 0..6) {
                val moodRating = Random.nextInt(5)
                val icon = getCroppedDrawable(
                    resources, UiUtils.getMoodImageResourcesIdByIndex(moodRating), 80, 80
                )
                val entry = Entry(dayOfWeek.toFloat(), moodRating.toFloat() + 1f, icon)
                mockEntries.add(entry)
            }
            return mockEntries
        }

        private fun setItemData(
            resources: Resources,
            view: View,
            imageId: Int,
            imageIconId: Int,
            tittleId: Int,
            tittle: String,
            counterId: Int,
            counter: Int
        ) {
            view.findViewById<ImageView>(imageId)
                .setImageDrawable(getCroppedDrawable(resources, imageIconId, 90, 90))
            view.findViewById<TextView>(tittleId).text = tittle
            view.findViewById<TextView>(counterId).text = createCounterText(counter)
        }

        fun createCounterText(counter: Int): String {
            return buildString {
                append("x ")
                append(counter.toString())
            }
        }

        private object MoodChartSettings {
            const val Y_AXIS_MIN = 0.5f
            const val Y_AXIS_MAX = 5.5f
            const val X_AXIS_MIN = -0.5f
            const val X_AXIS_MAX = 7.5f
            const val GRID_LINE_WIDTH = 1f
            const val GRANULARITY = 1f
            const val CHARTS_COLOR = "#55878D"
            const val CHARTS_LINE_WIDTH = 2.5f
            const val TEXT_SIZE = 12f
            const val TEXT_COLOR = Color.LTGRAY
            const val GRID_LINE_LENGTH = 30f
            const val GRID_LINE_SPACE_LENGTH = 40f
            const val GRID_LINE_PHASE = 0f
        }

        fun createWeekMoodCharts(resources: Resources, lineChart: LineChart) {
            val data = getWeekMoodData(resources)

            lineChart.axisRight.isEnabled = false
            val yAxis: YAxis = lineChart.axisLeft
            yAxis.setDrawAxisLine(true)
            yAxis.setDrawGridLines(true)
            yAxis.setDrawGridLinesBehindData(true)
            yAxis.setAxisMinimum(MoodChartSettings.Y_AXIS_MIN)
            yAxis.setAxisMaximum(MoodChartSettings.Y_AXIS_MAX)
            yAxis.granularity = MoodChartSettings.GRANULARITY
            yAxis.gridLineWidth = MoodChartSettings.GRID_LINE_WIDTH
            yAxis.gridColor = Color.LTGRAY
            yAxis.enableGridDashedLine(
                MoodChartSettings.GRID_LINE_LENGTH,
                MoodChartSettings.GRID_LINE_SPACE_LENGTH,
                MoodChartSettings.GRID_LINE_PHASE
            )

            val xAxis: XAxis = lineChart.xAxis
            xAxis.textColor = MoodChartSettings.TEXT_COLOR
            xAxis.setTextSize(MoodChartSettings.TEXT_SIZE)
            xAxis.setAxisMinimum(MoodChartSettings.X_AXIS_MIN)
            xAxis.setAxisMaximum(MoodChartSettings.X_AXIS_MAX)
            xAxis.setDrawAxisLine(true)
            xAxis.valueFormatter = LineChartXAxisValueFormatter()
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = MoodChartSettings.GRANULARITY

            val dataSet = LineDataSet(data, "week mood")
            dataSet.setColor(Color.parseColor(MoodChartSettings.CHARTS_COLOR))
            dataSet.setLineWidth(MoodChartSettings.CHARTS_LINE_WIDTH)

            val legend: Legend = lineChart.legend
            legend.form = Legend.LegendForm.NONE
            legend.textColor = Color.WHITE

            val description = Description()
            description.isEnabled = false
            lineChart.description = description

            val lineData = LineData(dataSet)
            lineData.setDrawValues(false)
            lineChart.setData(lineData)

            lineChart.invalidate()
        }

        fun createFrequentlyUsedActivities(resources: Resources, view: View) {
            // todo: set images id based on server response
            // mock data
            setItemData(
                resources,
                view,
                R.id.activity_icon_1,
                R.drawable.widget_ask_icon,
                R.id.activity_1,
                "study",
                R.id.activity_counter_1,
                24
            )
            setItemData(
                resources,
                view,
                R.id.activity_icon_2,
                R.drawable.widget_ask_icon,
                R.id.activity_2,
                "work",
                R.id.activity_counter_2,
                20
            )
            setItemData(
                resources,
                view,
                R.id.activity_icon_3,
                R.drawable.widget_ask_icon,
                R.id.activity_3,
                "sport",
                R.id.activity_counter_3,
                7
            )
        }

        fun createFrequentlyUsedEmotions(resources: Resources, view: View) {
            // todo: set images id based on server response
            // mock data
            setItemData(
                resources,
                view,
                R.id.emotion_icon_1,
                R.drawable.widget_ask_icon,
                R.id.emotion_1,
                "sadness",
                R.id.emotion_counter_1,
                100
            )
            setItemData(
                resources,
                view,
                R.id.emotion_icon_2,
                R.drawable.widget_ask_icon,
                R.id.emotion_2,
                "anxiety",
                R.id.emotion_counter_2,
                66
            )
            setItemData(
                resources,
                view,
                R.id.emotion_icon_3,
                R.drawable.widget_ask_icon,
                R.id.emotion_3,
                "shock",
                R.id.emotion_counter_3,
                50
            )
        }

        private fun getCroppedDrawable(
            resources: Resources, drawableId: Int, width: Int, height: Int
        ): Drawable {
            val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            drawable?.setBounds(0, 0, canvas.width, canvas.height)
            drawable?.draw(canvas)

            return BitmapDrawable(resources, bitmap)
        }
    }

    class DayData(var isRecorded: Boolean, var week: DayOfWeek)
}
