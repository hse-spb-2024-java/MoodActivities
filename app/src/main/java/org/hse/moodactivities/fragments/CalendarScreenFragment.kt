package org.hse.moodactivities.fragments

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.CalendarDayActivity
import org.hse.moodactivities.adapters.CalendarAdapter
import org.hse.moodactivities.responses.MonthStatisticResponse
import org.hse.moodactivities.services.CalendarService
import org.hse.moodactivities.services.ThemesService
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarScreenFragment : Fragment(), CalendarAdapter.OnItemListener {
    companion object {
        private const val LOG_TAG = "calendar"
        private const val PERCENT_SIGN = "%"
        private const val PERCENT_AMOUNT = 100
        private const val DAYS_OF_WEEK_AMOUNT = 7
        private const val EMPTY_DAY_STRING = ""
    }

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var selectedDate: LocalDate
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_screen, container, false)

        selectedDate = LocalDate.now()
        calendarRecyclerView = view.findViewById(R.id.calendar_recycler_view)
        monthYearText = view.findViewById(R.id.month_year_text_view)

        // button to next month
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            initMonthData()
        }

        // button to previous month
        view.findViewById<Button>(R.id.previous_button).setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            initMonthData()
        }

        // set colors to month statistic images
        // todo in themes: move to ui service
        val moodBackground1: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_1_image).background as GradientDrawable
        moodBackground1.setColor(ThemesService.getMoodIndicatorColorByScore(1))
        val moodBackground2: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_2_image).background as GradientDrawable
        moodBackground2.setColor(ThemesService.getMoodIndicatorColorByScore(2))
        val moodBackground3: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_3_image).background as GradientDrawable
        moodBackground3.setColor(ThemesService.getMoodIndicatorColorByScore(3))
        val moodBackground4: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_4_image).background as GradientDrawable
        moodBackground4.setColor(ThemesService.getMoodIndicatorColorByScore(4))
        val moodBackground5: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_5_image).background as GradientDrawable
        moodBackground5.setColor(ThemesService.getMoodIndicatorColorByScore(5))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMonthData()
        setColorTheme()
    }

    private fun initMonthData() {
        val response = CalendarService.getMonthMoodStatistic(
            this.activity as AppCompatActivity, selectedDate.month.value, selectedDate.year
        )
        setMonthView(response)
        setMonthMoodStatistic(response)
    }

    private fun getPercents(nominator: Int, denominator: Int): Int {
        if (denominator == 0) {
            return (PERCENT_AMOUNT * nominator)
        }
        return (PERCENT_AMOUNT * nominator) / denominator
    }

    private fun createTextWithPercents(nominator: Int, summary: Int): String {
        return buildString {
            append(getPercents(nominator, summary).toString())
            append(PERCENT_SIGN)
        }
    }

    private fun setMonthMoodStatistic(response: MonthStatisticResponse) {
        val monthStatistic = intArrayOf(0, 0, 0, 0, 0)

        val moodRates = response.getMoodRates()
        for (moodRate in moodRates.entries) {
            if (moodRate.value != 0) {
                monthStatistic[moodRate.value - 1]++
            }
        }

        var sum = 0
        for (i in 0..4) {
            sum += monthStatistic[i]
        }

        view?.findViewById<TextView>(R.id.day_1_text)?.text =
            createTextWithPercents(monthStatistic[0], sum)
        view?.findViewById<TextView>(R.id.day_2_text)?.text =
            createTextWithPercents(monthStatistic[1], sum)
        view?.findViewById<TextView>(R.id.day_3_text)?.text =
            createTextWithPercents(monthStatistic[2], sum)
        view?.findViewById<TextView>(R.id.day_4_text)?.text =
            createTextWithPercents(monthStatistic[3], sum)
        view?.findViewById<TextView>(R.id.day_5_text)?.text =
            createTextWithPercents(monthStatistic[4], sum)
    }

    private fun setMonthView(response: MonthStatisticResponse) {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        calendarAdapter = CalendarAdapter(daysInMonth, selectedDate.month, response, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(this.requireContext(), DAYS_OF_WEEK_AMOUNT)
        calendarRecyclerView.setLayoutManager(layoutManager)
        calendarRecyclerView.setAdapter(calendarAdapter)

        Log.i(LOG_TAG, "Create calendar for month " + selectedDate.month)
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.getDayOfWeek().value

        for (i in 1 + DAYS_OF_WEEK_AMOUNT * (dayOfWeek / DAYS_OF_WEEK_AMOUNT)..daysInMonth + dayOfWeek) {
            if (i < dayOfWeek) {
                daysInMonthArray.add(EMPTY_DAY_STRING)
            } else if (i >= daysInMonth + dayOfWeek) {
                break
            } else {
                daysInMonthArray.add((i - dayOfWeek + 1).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (!dayText.isNullOrEmpty()) {
            val day = dayText.toLong()
            val date = selectedDate.plusDays(day - selectedDate.dayOfMonth)
            CalendarService.setDate(date)
            val dayInfoActivityIntent = Intent(this.activity, CalendarDayActivity::class.java)
            startActivity(dayInfoActivityIntent)
        }
    }

    private fun setColorTheme() {
        // set background color
        view?.findViewById<ConstraintLayout>(R.id.calendar_screen_fragment_layout)
            ?.setBackgroundColor(ThemesService.getBackgroundColor())

        // set color to calendar widget background
        view?.findViewById<CardView>(R.id.calendar_card)
            ?.setCardBackgroundColor(ThemesService.getColor3())

        // set color to month statistic widget
        view?.findViewById<CardView>(R.id.month_statistic_card)
            ?.setCardBackgroundColor(ThemesService.getDimmedColor3())
    }
}
