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
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.animation.Easing
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
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import org.hse.moodactivities.R
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

        fun setStatisticMode(statisticMode: StatisticMode) {
            this.statisticMode = statisticMode
        }

        fun createDistributionChart(pieChart: PieChart) {
            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.setExtraOffsets(5f, 5f, 5f, 5f)
            pieChart.dragDecelerationFrictionCoef = 0.95f
            pieChart.isDrawHoleEnabled = true
            pieChart.setHoleColor(Color.WHITE)
            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)
            pieChart.holeRadius = 58f
            pieChart.transparentCircleRadius = 61f
            pieChart.setDrawCenterText(true)
            pieChart.rotationAngle = 0f
            pieChart.isRotationEnabled = true
            pieChart.isHighlightPerTapEnabled = true

            val entries = ArrayList<PieEntry>()
            entries.add(PieEntry(70f))
            entries.add(PieEntry(20f))
            entries.add(PieEntry(10f))

            val dataSet = PieDataSet(entries, "Mobile OS")
            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            val colors = ArrayList<Int>()
            colors.add(ColorTemplate.getHoloBlue())
            colors.add(Color.RED)
            colors.add(Color.GREEN)
            dataSet.colors = colors

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)

            pieChart.data = data

            pieChart.animateY(1400, Easing.EaseInOutQuad)

            pieChart.legend.isEnabled = false
            pieChart.setEntryLabelColor(Color.WHITE)
            pieChart.setEntryLabelTextSize(12f)

            pieChart.invalidate()

        }

        private fun getWeekMoodData(resources: Resources): MutableList<Entry> {
            // todo: replace with data from server response

            // mock data
            val mockEntries: MutableList<Entry> = ArrayList()
            for (dayOfWeek in 0..7) {
                val moodRating = Random.nextInt(5)
                val icon = getCroppedDrawable(
                    resources,
                    UiUtils.getMoodImageResourcesIdByIndex(moodRating),
                    80,
                    80
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
        }

        fun createWeekMoodCharts(resources: Resources, lineChart: LineChart) {

            val data = getWeekMoodData(resources)

            // charts' format
            lineChart.axisRight.isEnabled = false
            val yAxis: YAxis = lineChart.axisLeft
            yAxis.setDrawAxisLine(true)
            yAxis.setDrawGridLines(true)
            yAxis.setDrawGridLinesBehindData(true)
            yAxis.setAxisMinimum(MoodChartSettings.Y_AXIS_MIN)
            yAxis.setAxisMaximum(MoodChartSettings.Y_AXIS_MAX)
            yAxis.granularity = 1f
            yAxis.gridLineWidth = 1f
            yAxis.gridColor = Color.LTGRAY
            yAxis.enableGridDashedLine(30f, 40f, 0f)

            val xAxis: XAxis = lineChart.xAxis
            xAxis.textColor = Color.parseColor("#333333")
            xAxis.setTextSize(11f)
            xAxis.setAxisMinimum(-0.5f)
            xAxis.setAxisMaximum(7.5f)
            xAxis.setDrawAxisLine(true)
            xAxis.valueFormatter = LineChartXAxisValueFormatter()
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setGranularity(1f)

            val dataSet = LineDataSet(data, "Label")
            dataSet.setColor(Color.parseColor("#55878D"))
            dataSet.setLineWidth(2.5f)

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
                resources, view, R.id.activity_icon_1, R.drawable.widget_ask_icon,
                R.id.activity_1, "study",
                R.id.activity_counter_1, 24
            )
            setItemData(
                resources, view, R.id.activity_icon_2, R.drawable.widget_ask_icon,
                R.id.activity_2, "work",
                R.id.activity_counter_2, 20
            )
            setItemData(
                resources, view, R.id.activity_icon_3, R.drawable.widget_ask_icon,
                R.id.activity_3, "sport",
                R.id.activity_counter_3, 7
            )
        }

        fun createFrequentlyUsedEmotions(resources: Resources, view: View) {
            // todo: set images id based on server response
            // mock data
            setItemData(
                resources, view, R.id.emotion_icon_1, R.drawable.widget_ask_icon,
                R.id.emotion_1, "sadness",
                R.id.emotion_counter_1, 100
            )
            setItemData(
                resources, view, R.id.emotion_icon_2, R.drawable.widget_ask_icon,
                R.id.emotion_2, "anxiety",
                R.id.emotion_counter_2, 66
            )
            setItemData(
                resources, view, R.id.emotion_icon_3, R.drawable.widget_ask_icon,
                R.id.emotion_3, "shock",
                R.id.emotion_counter_3, 50
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

    class DayData(var isNoted: Boolean, var week: DayOfWeek)
}
