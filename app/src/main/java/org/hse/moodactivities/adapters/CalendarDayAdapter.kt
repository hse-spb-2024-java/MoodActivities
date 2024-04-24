package org.hse.moodactivities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.models.DailyActivityModel
import org.hse.moodactivities.models.DailyInfoModel
import org.hse.moodactivities.models.DailyItemModel
import org.hse.moodactivities.models.DailyItemType
import org.hse.moodactivities.models.DailyQuestionModel
import org.hse.moodactivities.utils.UiUtils

enum class ViewType(val type: Int) {
    ACTIVITY(0), QUESTION(1), INFO(2)
}

class CalendarDayAdapter(
    private var dailyItemsList: ArrayList<DailyItemModel>
) : RecyclerView.Adapter<CalendarDayAdapter.CalendarDayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayViewHolder {
        val layoutId = when (viewType) {
            ViewType.ACTIVITY.type -> R.layout.widget_daily_activity
            ViewType.QUESTION.type -> R.layout.widget_daily_question
            else -> R.layout.widget_daily_info
        }
        val viewHolder = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return CalendarDayViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return dailyItemsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (dailyItemsList[position].dailyItemType) {
            DailyItemType.DAILY_ACTIVITY -> ViewType.ACTIVITY.type
            DailyItemType.DAILY_QUESTION -> ViewType.QUESTION.type
            else -> ViewType.INFO.type
        }
    }

    override fun onBindViewHolder(holder: CalendarDayViewHolder, position: Int) {
        val dailyItem: DailyItemModel = dailyItemsList[position]

        when (dailyItem.dailyItemType) {
            DailyItemType.DAILY_INFO -> fillHolderFromDailyInfoModel(
                holder, dailyItem as DailyInfoModel
            )

            DailyItemType.DAILY_QUESTION -> fillHolderFromDailyQuestionModel(
                holder, dailyItem as DailyQuestionModel
            )

            DailyItemType.DAILY_ACTIVITY -> fillHolderFromDailyActivityModel(
                holder, dailyItem as DailyActivityModel
            )
        }
    }

    companion object {
        private fun fillHolderFromDailyInfoModel(
            holder: CalendarDayViewHolder, dailyInfoModel: DailyInfoModel
        ) {
            val view = holder.getView()
            view.findViewById<TextView>(R.id.short_description).text =
                dailyInfoModel.getShortDescription()
            view.findViewById<TextView>(R.id.daily_question).text = dailyInfoModel.getQuestion()
            view.findViewById<TextView>(R.id.daily_answer).text =
                dailyInfoModel.getAnswerToQuestion()
            view.findViewById<TextView>(R.id.time).text = dailyInfoModel.getTime()
            view.findViewById<ImageView>(R.id.day_mood).setImageResource(
                UiUtils.getMoodImageResourcesIdByIndex(dailyInfoModel.getMoodRating())
            )
            view.findViewById<TextView>(R.id.activities).text =
                dailyInfoModel.getActivities().joinToString(", ") { it.getName() }
            view.findViewById<TextView>(R.id.emotions).text =
                dailyInfoModel.getEmotions().joinToString(", ") { it.getName() }
        }

        private fun fillHolderFromDailyQuestionModel(
            holder: CalendarDayViewHolder, dailyQuestionModel: DailyQuestionModel
        ) {
            val view = holder.getView()
            view.findViewById<TextView>(R.id.daily_question).text =
                dailyQuestionModel.getDailyQuestion()
            view.findViewById<TextView>(R.id.answer_to_daily_question).text =
                dailyQuestionModel.getAnswerToDailyQuestion()
            view.findViewById<TextView>(R.id.time).text = dailyQuestionModel.getTime()
        }

        private fun fillHolderFromDailyActivityModel(
            holder: CalendarDayViewHolder, dailyActivityModel: DailyActivityModel
        ) {
            val view = holder.getView()
            view.findViewById<TextView>(R.id.daily_activity).text =
                dailyActivityModel.getDailyActivity()
            view.findViewById<TextView>(R.id.user_impressions).text =
                dailyActivityModel.getUserImpressions()
            view.findViewById<TextView>(R.id.time).text = dailyActivityModel.getTime()
        }
    }

    class CalendarDayViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        fun getView(): View {
            return view
        }
    }
}
