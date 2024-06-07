package org.hse.moodactivities.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.responses.MonthStatisticResponse
import org.hse.moodactivities.utils.UiUtils
import java.time.LocalDate
import java.time.Month


internal class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>, private val currentMonth: Month,
    response: MonthStatisticResponse,
    onItemListener: OnItemListener,
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    companion object {
        const val LINE_AMOUNT_IN_CALENDAR = 6
    }

    private val onItemListener: OnItemListener
    private val moodRates: HashMap<Int, Int>

    init {
        this.onItemListener = onItemListener
        this.moodRates = response.getMoodRates()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height / LINE_AMOUNT_IN_CALENDAR)
        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.getDayOfMonth().text = daysOfMonth[position]
        if (holder.getDayOfMonth().text.isEmpty()) {
            val backgroundColor: GradientDrawable =
                holder.getDayOfMonthIndicator().background as GradientDrawable
            backgroundColor.setColor(Color.WHITE)
        } else {
            val moodIndicatorBackground: GradientDrawable =
                holder.getDayOfMonthIndicator().background as GradientDrawable

            val day = daysOfMonth[position].toInt()
            val moodRate: Int = if (moodRates.containsKey(day)) moodRates[day]!! else -1
            val backgroundColor = UiUtils.getColorForMoodStatistic(moodRate - 1)
            moodIndicatorBackground.setColor(backgroundColor)
        }
        if (currentMonth == LocalDate.now().month && holder.getDayOfMonth().text == LocalDate.now().dayOfMonth.toString()) {
            holder.getDayOfMonth().setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }

    class CalendarViewHolder(itemView: View, private val onItemListener: OnItemListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val dayOfMonth: TextView
        private val dayOfMonthIndicator: ImageView

        init {
            dayOfMonth = itemView.findViewById(R.id.cell_day_text)
            dayOfMonthIndicator = itemView.findViewById(R.id.mood_indicator)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onItemListener.onItemClick(getAdapterPosition(), dayOfMonth.getText() as String)
        }

        fun getDayOfMonth(): TextView {
            return dayOfMonth
        }

        fun getDayOfMonthIndicator(): ImageView {
            return dayOfMonthIndicator
        }
    }
}
