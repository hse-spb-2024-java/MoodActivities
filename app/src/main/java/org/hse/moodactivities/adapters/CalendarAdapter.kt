package org.hse.moodactivities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R


internal class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>, onItemListener: OnItemListener
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

        init {
            dayOfMonth = itemView.findViewById(R.id.cell_day_text)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onItemListener.onItemClick(getAdapterPosition(), dayOfMonth.getText() as String)
        }
    }
}
