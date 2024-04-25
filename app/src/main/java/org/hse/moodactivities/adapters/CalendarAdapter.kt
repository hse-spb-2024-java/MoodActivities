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
import org.hse.moodactivities.utils.UiUtils
import java.time.LocalDate
import java.time.Month
import kotlin.random.Random


internal class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>, private val currentMonth: Month,
    onItemListener: OnItemListener,
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    private val onItemListener: OnItemListener

    init {
        this.onItemListener = onItemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonth[position]
        if (holder.dayOfMonth.text.isEmpty()) {
            val backgroundColor: GradientDrawable =
                holder.dayOfMonthIndicator.background as GradientDrawable
            backgroundColor.setColor(Color.WHITE)
        } else {
            val moodIndicatorBackground: GradientDrawable =
                holder.dayOfMonthIndicator.background as GradientDrawable
            // todo: ask server of user's mood at specific date
            val randomUserMood: Int = Random.nextInt(0, 5)
            val backgroundColor: Int = UiUtils.getColorForMoodStatistic(randomUserMood)
            moodIndicatorBackground.setColor(backgroundColor)
        }
        if (currentMonth == LocalDate.now().month && holder.dayOfMonth.text == LocalDate.now().dayOfMonth.toString()) {
            holder.dayOfMonth.setTextColor(Color.RED)
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
        val dayOfMonth: TextView
        val dayOfMonthIndicator: ImageView

        init {
            dayOfMonth = itemView.findViewById(R.id.cell_day_text)
            dayOfMonthIndicator = itemView.findViewById(R.id.mood_indicator)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onItemListener.onItemClick(getAdapterPosition(), dayOfMonth.getText() as String)
        }
    }
}
