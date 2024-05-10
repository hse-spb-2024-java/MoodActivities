package org.hse.moodactivities.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
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
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.R
import org.hse.moodactivities.common.proto.requests.defaults.PeriodType
import org.hse.moodactivities.common.proto.requests.stats.ReportType
import org.hse.moodactivities.common.proto.requests.stats.TopListRequest
import org.hse.moodactivities.common.proto.requests.stats.UsersMoodRequest
import org.hse.moodactivities.common.proto.requests.stats.WeeklyReportRequest
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.models.Item
import org.hse.moodactivities.models.StatisticItem
import org.hse.moodactivities.utils.UiUtils
import org.hse.moodactivities.viewmodels.AuthViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.max
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


class ChartsService(activity: AppCompatActivity) {
    private val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 12345).usePlaintext().build()
    private val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
    private val stub =
        StatsServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
            authViewModel.getToken(
                activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
            )!!
        })

    private class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            var date = LocalDate.now()
            date = date.minusDays(value.toLong())
            return date.dayOfMonth.toString() + "/" + date.month.value
        }
    }

    private fun toPeriodType(timePeriod: TimePeriod.Value): PeriodType {
        return when (timePeriod) {
            TimePeriod.Value.WEEK -> PeriodType.WEEK
            TimePeriod.Value.MONTH -> PeriodType.MONTH
            TimePeriod.Value.YEAR -> PeriodType.YEAR
            TimePeriod.Value.ALL_TIME -> PeriodType.ALL
        }
    }

    private fun toReportType(): ReportType {
        return when (statisticMode) {
            StatisticMode.EMOTIONS -> ReportType.EMOTIONS
            StatisticMode.ACTIVITIES -> ReportType.ACTIVITIES
        }
    }

    fun getStatistic(
        timePeriod: TimePeriod.Value
    ): ArrayList<StatisticItem> {
        val request = TopListRequest.newBuilder().setPeriod(toPeriodType(timePeriod))
            .setReportType(toReportType()).build()
        val response = stub.getTopList(request).topReportList

        val responseList = ArrayList<StatisticItem>(response.size)
        for (item in response) {
            (if (toReportType() == ReportType.EMOTIONS) Item.getEmotionByName(item.name)
                ?.getIconIndex()
            else Item.getActivityByName(item.name)?.getIconIndex())?.let {
                StatisticItem(
                    item.name, item.amount, it
                )
            }?.let { responseList.add(it) }
        }
        return responseList
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
        val response = stub.getWeeklyReport(WeeklyReportRequest.getDefaultInstance());
        val days = ArrayList<DayData>()
        for (dayIndex in 0..6) {
            val layout = daysInRow.getChildAt(2 * dayIndex) as LinearLayout
            val day =
                response.listOfDaysList.find { DayOfWeek.valueOf(it.name) == DayOfWeek.of(dayIndex + 1) }
            if (day != null) {
                days.add(DayData(true, DayOfWeek.valueOf(day.name)))
            } else {
                days.add(DayData(false, DayOfWeek.of(dayIndex + 1)))
            }
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

    fun createDistributionChart(pieChart: PieChart, timePeriod: TimePeriod.Value) {
        val statistic = getStatistic(timePeriod)

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

        // todo: add colors in themes
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

    private var moodChartPeriod = TimePeriod.Value.WEEK
    private var maxValue : Long = 0

    @SuppressLint("SimpleDateFormat")
    private fun getMoodData(
        resources: Resources, period: TimePeriod.Value
    ): MutableList<Entry> {
        moodChartPeriod = period
        val entries: MutableList<Entry> = ArrayList()
        val recordedItems =
            stub.getUsersMood(UsersMoodRequest.newBuilder().setPeriod(toPeriodType(period)).build())
        for (item in recordedItems.usersMoodsList) {
            val dateString = item.date
            val score = item.score
            val date = LocalDate.parse(dateString).toEpochDay()
            val localDate = LocalDate.now().toEpochDay()
            val icon = getCroppedDrawable(
                resources, UiUtils.getMoodImageResourcesIdByIndex(score), 80, 80
            )
            Log.i("mood", (localDate - date).toString())
            maxValue = max(maxValue, localDate - date)
            val entry = Entry((localDate - date).toFloat(), score + 1f, icon)
            entries.add(entry)
        }
        return entries
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

    fun createMoodCharts(resources: Resources, lineChart: LineChart, period: TimePeriod.Value) {
        val data = getMoodData(resources, period)

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
        if (period == TimePeriod.Value.WEEK) {
            maxValue = 7
        } else if (period == TimePeriod.Value.MONTH) {
            maxValue = 30
        } else if (period == TimePeriod.Value.YEAR) {
            maxValue = 365
        }
        xAxis.setAxisMaximum(0.5f + maxValue)
        xAxis.setDrawAxisLine(true)
        xAxis.valueFormatter = LineChartXAxisValueFormatter()
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 20.toFloat() / maxValue

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

    private fun getActivityIcon(index: Int): Int {
        return when (index) {
            0 -> R.id.activity_icon_1
            1 -> R.id.activity_icon_2
            2 -> R.id.activity_icon_3
            else -> -1
        }
    }

    private fun getActivityText(index: Int): Int {
        return when (index) {
            0 -> R.id.activity_1
            1 -> R.id.activity_2
            2 -> R.id.activity_3
            else -> -1
        }
    }

    private fun getActivityCounterId(index: Int): Int {
        return when (index) {
            0 -> R.id.activity_counter_1
            1 -> R.id.activity_counter_2
            2 -> R.id.activity_counter_3
            else -> -1
        }
    }

    fun createFrequentlyUsedActivities(
        resources: Resources, view: View, timePeriod: TimePeriod.Value
    ) {
        val response = stub.getTopList(
            TopListRequest.newBuilder()
                .setPeriod(toPeriodType(timePeriod))
                .setReportType(ReportType.ACTIVITIES)
                .setAmount(AMOUNT_IN_TOP_BY_FREQUENCY)
                .build()
        )
        val activitiesList = response.topReportList.subList(0, AMOUNT_IN_TOP_BY_FREQUENCY)
        for (index in 0..<AMOUNT_IN_TOP_BY_FREQUENCY) {
            val item = activitiesList[index]
            Item.getActivityByName(item.name)?.let {
                setItemData(
                    resources,
                    view,
                    getActivityIcon(index),
                    Item.getActivityIconIdByName(item.name)!!,
                    getActivityText(index),
                    item.name,
                    getActivityCounterId(index),
                    item.amount
                )
            }
        }
        // todo: set default, when response has less than 3 items
    }

    private fun getEmotionsIcon(index: Int): Int {
        return when (index) {
            0 -> R.id.emotion_icon_1
            1 -> R.id.emotion_icon_2
            2 -> R.id.emotion_icon_3
            else -> -1
        }
    }

    private fun getEmotionsText(index: Int): Int {
        return when (index) {
            0 -> R.id.emotion_1
            1 -> R.id.emotion_2
            2 -> R.id.emotion_3
            else -> -1
        }
    }

    private fun getEmotionsCounterId(index: Int): Int {
        return when (index) {
            0 -> R.id.emotion_counter_1
            1 -> R.id.emotion_counter_2
            2 -> R.id.emotion_counter_3
            else -> -1
        }
    }

    fun createFrequentlyUsedEmotions(
        resources: Resources, view: View, timePeriod: TimePeriod.Value
    ) {
        val response = stub.getTopList(
            TopListRequest.newBuilder()
                .setPeriod(toPeriodType(timePeriod))
                .setReportType(ReportType.EMOTIONS)
                .setAmount(AMOUNT_IN_TOP_BY_FREQUENCY)
                .build()
        )
        val emotionsList = response.topReportList.subList(0, AMOUNT_IN_TOP_BY_FREQUENCY)
        for (index in 0..<AMOUNT_IN_TOP_BY_FREQUENCY) {
            val item = emotionsList[index]
            Item.getEmotionByName(item.name)?.let {
                setItemData(
                    resources,
                    view,
                    getEmotionsIcon(index),
                    Item.getEmotionIconIdByName(item.name)!!,
                    getEmotionsText(index),
                    item.name,
                    getEmotionsCounterId(index),
                    item.amount
                )
            }
        }
        // todo: set default, when response has less than 3 items
    }

    companion object {
        private var statisticMode: StatisticMode = StatisticMode.EMOTIONS

        private const val AMOUNT_IN_TOP_BY_FREQUENCY = 3

        fun createCounterText(counter: Int): String {
            return buildString {
                append("x ")
                append(counter.toString())
            }
        }

        fun getStatisticMode(): StatisticMode {
            return statisticMode
        }

        fun setStatisticMode(statisticMode: StatisticMode) {
            this.statisticMode = statisticMode
        }

        private object MoodChartSettings {
            const val Y_AXIS_MIN = 0.5f
            const val Y_AXIS_MAX = 5.5f
            const val X_AXIS_MIN = -0.5f
            const val X_AXIS_MAX = 9.5f
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

        class DayData(var isRecorded: Boolean, var week: DayOfWeek)
    }
}
