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
import kotlin.math.min
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

    private lateinit var startDate: LocalDate
    private var moodChartPeriod = TimePeriod.Value.WEEK
    private var maxValue: Long = 0

    private inner class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            var date = LocalDate.now()
            date = date.minusDays(value.toLong())
            return date.dayOfMonth.toString() + "/" + date.month.value
        }
    }

    private object DaysInRowSettings {
        const val CARD_INDEX = 0
        const val TEXT_INDEX = 2
        const val TEXT_SIZE = 12f
        const val TEXT_COLOR = Color.BLACK
        const val WEEK_DAYS_AMOUNT = 7
    }

    private object MoodChartSettings {
        const val Y_AXIS_MIN = 0.5f
        const val Y_AXIS_MAX = 5.5f
        const val X_AXIS_MIN = -0.5f
        const val GRID_LINE_WIDTH = 1f
        const val GRANULARITY = 1f
        const val CHARTS_LINE_WIDTH = 2.5f
        const val TEXT_SIZE = 12f
        const val GRID_LINE_LENGTH = 30f
        const val GRID_LINE_SPACE_LENGTH = 40f
        const val GRID_LINE_PHASE = 0f
    }

    private object DistributionChartSettings {
        const val HOLE_RADIUS = 58f
        const val TEXT_SIZE = 12f
    }

    private fun getStartDate(timePeriod: TimePeriod.Value, minDate: LocalDate?) {
        startDate = when (timePeriod) {
            TimePeriod.Value.WEEK -> {
                LocalDate.now().minusDays(7)
            }

            TimePeriod.Value.MONTH -> {
                LocalDate.now().minusMonths(1)
            }

            TimePeriod.Value.YEAR -> {
                LocalDate.now().minusYears(1)
            }

            else -> {
                minDate ?: LocalDate.now()
            }
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

    fun createDaysInRow(daysInRow: LinearLayout) {
        val response = stub.getWeeklyReport(WeeklyReportRequest.getDefaultInstance());
        val days = ArrayList<DayData>()
        for (dayIndex in 0..<DaysInRowSettings.WEEK_DAYS_AMOUNT) {
            // layout of the day
            val layout = daysInRow.getChildAt(2 * dayIndex) as LinearLayout

            // get day data
            val day =
                response.listOfDaysList.find { DayOfWeek.valueOf(it.name) == DayOfWeek.of(dayIndex + 1) }
            if (day != null) {
                days.add(DayData(true, DayOfWeek.valueOf(day.name)))
            } else {
                days.add(DayData(false, DayOfWeek.of(dayIndex + 1)))
            }

            // set color for the day
            val colorTheme = ThemesService.getColorTheme()

            val color: Int = if (days[dayIndex].isRecorded) {
                colorTheme.getRecordedDayColor()
            } else {
                colorTheme.getNotRecordedDayColor()
            }
            val card = layout.getChildAt(DaysInRowSettings.CARD_INDEX) as CardView
            card.setCardBackgroundColor(color)

            // set week name
            val text = layout.getChildAt(DaysInRowSettings.TEXT_INDEX) as TextView
            text.text = days[dayIndex].week.name.lowercase().subSequence(0, 3)
            text.textSize = DaysInRowSettings.TEXT_SIZE
            text.setTypeface(text.typeface, Typeface.BOLD)
            text.textAlignment = View.TEXT_ALIGNMENT_CENTER
            text.setTextColor(DaysInRowSettings.TEXT_COLOR)
        }
    }

    fun createMoodCharts(resources: Resources, lineChart: LineChart, timePeriod: TimePeriod.Value) {
        maxValue = 0
        val data = getMoodData(resources, timePeriod)
        val colorTheme = ThemesService.getColorTheme()

        // set axis settings
        lineChart.axisRight.isEnabled = false

        // set y-axis settings
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

        // set x-axis settings
        val xAxis: XAxis = lineChart.xAxis
        xAxis.textColor = colorTheme.getMoodFlowChartTextColor()
        xAxis.setTextSize(MoodChartSettings.TEXT_SIZE)
        xAxis.setAxisMinimum(MoodChartSettings.X_AXIS_MIN)
        if (timePeriod == TimePeriod.Value.WEEK) {
            maxValue = 7
        } else if (timePeriod == TimePeriod.Value.MONTH) {
            maxValue = 30
        } else if (timePeriod == TimePeriod.Value.YEAR) {
            maxValue = 365
        }
        xAxis.setAxisMaximum(0.5f + maxValue)
        xAxis.setDrawAxisLine(true)
        xAxis.valueFormatter = LineChartXAxisValueFormatter()
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 20.toFloat() / maxValue

        // create dataset
        val dataSet = LineDataSet(data, "week mood")
        dataSet.setColor(colorTheme.getMoodFlowChartColor())
        dataSet.setLineWidth(MoodChartSettings.CHARTS_LINE_WIDTH)

        // set legend description
        val legend: Legend = lineChart.legend
        legend.form = Legend.LegendForm.NONE
        legend.textColor = colorTheme.getMoodFlowChartBackgroundColor()

        // set description enabled
        val description = Description()
        description.isEnabled = false
        lineChart.description = description

        // set data
        val lineData = LineData(dataSet)
        lineData.setDrawValues(false)
        lineChart.setData(lineData)

        lineChart.invalidate()
    }


    fun createDistributionChart(pieChart: PieChart, timePeriod: TimePeriod.Value) {
        val statistic = getStatistic(timePeriod)
        val colorTheme = ThemesService.getColorTheme()

        // create pie chart settings
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.holeRadius = DistributionChartSettings.HOLE_RADIUS
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setHoleColor(colorTheme.getBackgroundColor())

        // create entries for data set
        val entries = ArrayList<PieEntry>()
        for (entry: StatisticItem in statistic) {
            entries.add(PieEntry(entry.getCounter().toFloat()))
        }

        // set center text
        pieChart.setCenterTextSize(16f)
        pieChart.setCenterTextColor(colorTheme.getFontColor())
        if (entries.isEmpty()) {
            pieChart.centerText = "No data"
        } else {
            pieChart.centerText = ""
        }

        // create dataset fot pie chart
        val dataSet = PieDataSet(entries, "statistic")
        dataSet.setDrawIcons(false)

        // create colors for pie chart
        val colors = ArrayList<Int>()
        val rnd = Random.Default
        var previousColor = -1
        for (color in 0..statistic.size) {
            var colorIndex = rnd.nextInt(12)
            while (colorIndex == previousColor) {
                colorIndex = rnd.nextInt(12)
            }
            previousColor = colorIndex
            colors.add(colorTheme.getColorByIndex(colorIndex))
        }
        dataSet.colors = colors

        // create formatted data
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(DistributionChartSettings.TEXT_SIZE)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(colorTheme.getDimmedBackgroundColor())
        pieChart.data = data

        // create legend
        pieChart.legend.isEnabled = false

        pieChart.invalidate()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMoodData(
        resources: Resources, timePeriod: TimePeriod.Value
    ): MutableList<Entry> {
        moodChartPeriod = timePeriod
        val entries: MutableList<Entry> = ArrayList()
        val recordedItems = stub.getUsersMood(
            UsersMoodRequest.newBuilder().setPeriod(toPeriodType(timePeriod)).build()
        )
        val minDate =
            if (recordedItems.usersMoodsList.isEmpty()) null else LocalDate.parse(recordedItems.usersMoodsList[0].date)
        getStartDate(timePeriod, minDate)
        for (item in recordedItems.usersMoodsList) {
            val dateString = item.date
            val score = item.score
            val date = LocalDate.parse(dateString).toEpochDay()
            val startDate = startDate.toEpochDay()
            maxValue = max(maxValue, date - startDate)
            val entry =
                if (timePeriod == TimePeriod.Value.WEEK || timePeriod == TimePeriod.Value.MONTH) {
                    val icon = getCroppedDrawable(
                        resources, UiUtils.getMoodImageResourcesIdByIndex(score), 80, 80
                    )
                    Entry((date - startDate).toFloat(), score + 1f, icon)
                } else {
                    Entry((date - startDate).toFloat(), score + 1f)
                }
            entries.add(entry)
        }
        return entries
    }


    fun createFrequentlyUsedActivities(
        resources: Resources, view: View, timePeriod: TimePeriod.Value
    ) {
        val response = stub.getTopList(
            TopListRequest.newBuilder().setPeriod(toPeriodType(timePeriod))
                .setReportType(ReportType.ACTIVITIES).setAmount(AMOUNT_IN_TOP_BY_FREQUENCY).build()
        )
        val amount = min(response.topReportList.size, AMOUNT_IN_TOP_BY_FREQUENCY)
        val activitiesList = response.topReportList.subList(0, amount)
        for (index in 0..<amount) {
            val item = activitiesList[index]
            Item.getActivityByName(item.name)?.let {
                setItemData(
                    resources,
                    view,
                    getActivityIconId(index),
                    Item.getActivityIconIdByName(item.name)!!,
                    getActivityTextId(index),
                    item.name,
                    getActivityCounterId(index),
                    item.amount
                )
            }
        }
        setDefaultFrequentlyUsedActivitiesItems(resources, view, amount)
    }

    fun createFrequentlyUsedEmotions(
        resources: Resources, view: View, timePeriod: TimePeriod.Value
    ) {
        val response = stub.getTopList(
            TopListRequest.newBuilder().setPeriod(toPeriodType(timePeriod))
                .setReportType(ReportType.EMOTIONS).setAmount(AMOUNT_IN_TOP_BY_FREQUENCY).build()
        )
        val amount = min(AMOUNT_IN_TOP_BY_FREQUENCY, response.topReportList.size)
        val emotionsList = response.topReportList.subList(0, amount)
        for (index in 0..<amount) {
            val item = emotionsList[index]
            Item.getEmotionByName(item.name)?.let {
                setItemData(
                    resources,
                    view,
                    getEmotionIconId(index),
                    Item.getEmotionIconIdByName(item.name)!!,
                    getEmotionTextId(index),
                    item.name,
                    getEmotionCounterId(index),
                    item.amount
                )
            }
        }
        setDefaultFrequentlyUsedEmotionsItems(resources, view, amount)
    }

    private fun setDefaultFrequentlyUsedActivitiesItems(
        resources: Resources, view: View, startIndex: Int
    ) {
        for (index in startIndex..<AMOUNT_IN_TOP_BY_FREQUENCY) {
            setItemData(
                resources,
                view,
                getActivityIconId(index),
                Item.getDefaultIconId(),
                getActivityTextId(index),
                NOT_ENOUGH_DATA,
                getActivityCounterId(index),
                0
            )
        }
    }

    private fun setDefaultFrequentlyUsedEmotionsItems(
        resources: Resources, view: View, startIndex: Int
    ) {
        for (index in startIndex..<AMOUNT_IN_TOP_BY_FREQUENCY) {
            setItemData(
                resources,
                view,
                getEmotionIconId(index),
                Item.getDefaultIconId(),
                getEmotionTextId(index),
                NOT_ENOUGH_DATA,
                getEmotionCounterId(index),
                0
            )
        }
    }

    companion object {
        private const val AMOUNT_IN_TOP_BY_FREQUENCY = 3
        private const val NOT_ENOUGH_DATA = "Not enough data"

        private var statisticMode: StatisticMode = StatisticMode.EMOTIONS

        class DayData(var isRecorded: Boolean, var week: DayOfWeek)

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

        // ui utils
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

        private fun getActivityIconId(index: Int): Int {
            return when (index) {
                0 -> R.id.activity_icon_1
                1 -> R.id.activity_icon_2
                2 -> R.id.activity_icon_3
                else -> -1
            }
        }

        private fun getActivityTextId(index: Int): Int {
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

        private fun getEmotionIconId(index: Int): Int {
            return when (index) {
                0 -> R.id.emotion_icon_1
                1 -> R.id.emotion_icon_2
                2 -> R.id.emotion_icon_3
                else -> -1
            }
        }

        private fun getEmotionTextId(index: Int): Int {
            return when (index) {
                0 -> R.id.emotion_1
                1 -> R.id.emotion_2
                2 -> R.id.emotion_3
                else -> -1
            }
        }

        private fun getEmotionCounterId(index: Int): Int {
            return when (index) {
                0 -> R.id.emotion_counter_1
                1 -> R.id.emotion_counter_2
                2 -> R.id.emotion_counter_3
                else -> -1
            }
        }
    }
}
