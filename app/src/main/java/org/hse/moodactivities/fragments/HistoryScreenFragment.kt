package org.hse.moodactivities.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.adapters.CalendarAdapter
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.random.Random


class HistoryScreenFragment : Fragment(), CalendarAdapter.OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private lateinit var calendarAdapter: CalendarAdapter
    private var selectedDate: LocalDate? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_screen, container, false)

        selectedDate = LocalDate.now()
        calendarRecyclerView = view.findViewById(R.id.calendar_recycler_view)
        monthYearText = view.findViewById(R.id.month_year_text_view)
        setMonthView()

        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            selectedDate = selectedDate!!.plusMonths(1)
            setMonthView()
            setMonthStatistic()
        }
        view.findViewById<Button>(R.id.previous_button).setOnClickListener {
            selectedDate = selectedDate!!.minusMonths(1)
            setMonthView()
            setMonthStatistic()
        }

        val moodBackground1: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_1_image).background as GradientDrawable
        moodBackground1.setColor(Color.parseColor("#483D8B"))
        val moodBackground2: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_2_image).background as GradientDrawable
        moodBackground2.setColor(Color.parseColor("#6495ED"))
        val moodBackground3: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_3_image).background as GradientDrawable
        moodBackground3.setColor(Color.parseColor("#FFFACD"))
        val moodBackground4: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_4_image).background as GradientDrawable
        moodBackground4.setColor(Color.parseColor("#FFB6C1"))
        val moodBackground5: GradientDrawable =
            view.findViewById<ImageView>(R.id.day_5_image).background as GradientDrawable
        moodBackground5.setColor(Color.parseColor("#90EE90"))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMonthStatistic()
    }

    private fun setMonthStatistic() {
        // todo: ask server for statistic
        val monthStatistic = intArrayOf(
            Random.nextInt(1, 30),
            Random.nextInt(1, 30),
            Random.nextInt(1, 30),
            Random.nextInt(1, 30),
            Random.nextInt(1, 30)
        )
        var sum = 0
        for (i in 0..4) {
            sum += monthStatistic[i]
        }

        view?.findViewById<TextView>(R.id.day_1_text)?.text =
            getPercents(monthStatistic[0], sum).toString() + "%"
        view?.findViewById<TextView>(R.id.day_2_text)?.text =
            getPercents(monthStatistic[1], sum).toString() + "%"
        view?.findViewById<TextView>(R.id.day_3_text)?.text =
            getPercents(monthStatistic[2], sum).toString() + "%"
        view?.findViewById<TextView>(R.id.day_4_text)?.text =
            getPercents(monthStatistic[3], sum).toString() + "%"
        view?.findViewById<TextView>(R.id.day_5_text)?.text =
            getPercents(monthStatistic[4], sum).toString() + "%"
    }

    private fun getPercents(part: Int, summary: Int): Int {
        if (summary == 0) {
            return (100 * part)
        }
        return (100 * part) / summary
    }

    private fun setMonthView() {
        monthYearText!!.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        calendarAdapter = CalendarAdapter(daysInMonth, selectedDate!!.month, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(this.requireContext(), 7)
        calendarRecyclerView!!.setLayoutManager(layoutManager)
        calendarRecyclerView!!.setAdapter(calendarAdapter)
    }

    private fun daysInMonthArray(date: LocalDate?): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.getDayOfWeek().value
        for (i in 1 + 7 * (dayOfWeek / 7)..daysInMonth + dayOfWeek) {
            if (i <= dayOfWeek) {
                daysInMonthArray.add("")
            } else if (i > daysInMonth + dayOfWeek) {
                break
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date!!.format(formatter)
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (!dayText.isNullOrEmpty()) {
            // todo: show history for this day
            val message =
                "Selected Date $dayText" + " " + monthYearFromDate(selectedDate)
            Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}
