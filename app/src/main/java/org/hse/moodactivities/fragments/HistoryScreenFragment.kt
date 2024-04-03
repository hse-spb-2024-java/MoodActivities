package org.hse.moodactivities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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


class HistoryScreenFragment : Fragment(), CalendarAdapter.OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
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
        }
        view.findViewById<Button>(R.id.previous_button).setOnClickListener {
            selectedDate = selectedDate!!.minusMonths(1)
            setMonthView()
        }

        return view
    }

    private fun setMonthView() {
        monthYearText!!.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
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
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
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
        if (dayText != "") {
            // todo: show history for this day
            val message =
                "Selected Date $dayText" + " " + monthYearFromDate(selectedDate)
            Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}
