package org.hse.moodactivities.services

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.hse.moodactivities.R
import java.time.LocalDate
import kotlin.random.Random


class ChartsService {
    companion object {
        private class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                // Show time in local version
                var date = LocalDate.now()
                date = date.minusDays(6 - value.toLong())
                return date.dayOfMonth.toString() + "/" + date.month.value
            }
        }

        private fun genWeekMoodData(resources: Resources): MutableList<Entry> {
            // todo: replace with data from server response

            // mock data
            val mockEntries: MutableList<Entry> = ArrayList()
            val defaultIcon = getResizedDrawable(resources, R.drawable.widget_mood_icon, 90, 90)
            for (dayOfWeek in 0..7) {
                val moodRating: Float = Random.nextInt(5).toFloat() + 1f
                // todo: add icons
                val entry = Entry(dayOfWeek.toFloat(), moodRating, defaultIcon)
                mockEntries.add(entry)
            }
            return mockEntries
        }

        fun createWeekMoodCharts(resources: Resources, lineChart: LineChart) {
            val data = genWeekMoodData(resources)

            // charts' format
            lineChart.axisRight.isEnabled = false
            val yAxis: YAxis = lineChart.axisLeft
            yAxis.setDrawAxisLine(true)
            yAxis.setDrawGridLines(true)
            yAxis.setDrawGridLinesBehindData(true)
            yAxis.setAxisMinimum(0.5f)
            yAxis.setAxisMaximum(5.5f)
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

        private fun getResizedDrawable(
            resources: Resources,
            drawableId: Int,
            width: Int,
            height: Int
        ): Drawable {
            val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            drawable?.setBounds(0, 0, canvas.width, canvas.height)
            drawable?.draw(canvas)

            return BitmapDrawable(resources, bitmap)
        }
    }
}
