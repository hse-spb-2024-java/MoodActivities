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

class DailyItemAdapter(
    private var dailyItemsList: ArrayList<DailyItemModel>
) : RecyclerView.Adapter<DailyItemAdapter.DayItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayItemViewHolder {
        val viewId = when (viewType) {
            0 -> {
                R.layout.widget_daily_activity
            }

            1 -> {
                R.layout.widget_daily_question
            }

            else -> {
                R.layout.widget_daily_info
            }
        }
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(viewId, parent, false)
        return DayItemViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return dailyItemsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (dailyItemsList[position].dailyItemType) {
            DailyItemType.DAILY_ACTIVITY -> {
                0
            }

            DailyItemType.DAILY_QUESTION -> {
                1
            }

            else -> {
                2
            }
        }
    }

    override fun onBindViewHolder(holder: DayItemViewHolder, position: Int) {
        val dailyItem: DailyItemModel = dailyItemsList[position]

        when (dailyItem.dailyItemType) {
            DailyItemType.DAILY_INFO -> {
                fillHolderFromDailyInfoModel(holder, dailyItem as DailyInfoModel)
            }

            DailyItemType.DAILY_QUESTION -> {
                fillHolderFromDailyQuestionModel(holder, dailyItem as DailyQuestionModel)
            }

            DailyItemType.DAILY_ACTIVITY -> {
                fillHolderFromDailyActivityModel(holder, dailyItem as DailyActivityModel)
            }
        }
    }

    companion object {
        private fun fillHolderFromDailyInfoModel(
            holder: DayItemViewHolder, dailyInfoModel: DailyInfoModel
        ) {
            holder.view.findViewById<TextView>(R.id.short_description).text =
                dailyInfoModel.getShortDescription()
            holder.view.findViewById<TextView>(R.id.daily_question).text =
                dailyInfoModel.getQuestion()
            holder.view.findViewById<TextView>(R.id.daily_answer).text =
                dailyInfoModel.getAnswerToQuestion()
            holder.view.findViewById<TextView>(R.id.time).text = dailyInfoModel.getTime()
            holder.view.findViewById<ImageView>(R.id.day_mood).setImageResource(
                UiUtils.getMoodImageResourcesIdByIndex(dailyInfoModel.getMoodRating())
            )
            holder.view.findViewById<TextView>(R.id.activities).text =
                dailyInfoModel.getActivities().joinToString(", ") { it.getName() }
            holder.view.findViewById<TextView>(R.id.emotions).text =
                dailyInfoModel.getEmotions().joinToString(", ") { it.getName() }
        }

        private fun fillHolderFromDailyQuestionModel(
            holder: DayItemViewHolder, dailyQuestionModel: DailyQuestionModel
        ) {
            holder.view.findViewById<TextView>(R.id.daily_question).text =
                dailyQuestionModel.getDailyQuestion()
            holder.view.findViewById<TextView>(R.id.answer_to_daily_question).text =
                dailyQuestionModel.getAnswerToDailyQuestion()
            holder.view.findViewById<TextView>(R.id.time).text = dailyQuestionModel.getTime()
        }

        private fun fillHolderFromDailyActivityModel(
            holder: DayItemViewHolder, dailyActivityModel: DailyActivityModel
        ) {
            holder.view.findViewById<TextView>(R.id.daily_activity).text =
                dailyActivityModel.getDailyActivity()
            holder.view.findViewById<TextView>(R.id.user_impressions).text =
                dailyActivityModel.getUserImpressions()
            holder.view.findViewById<TextView>(R.id.time).text = dailyActivityModel.getTime()
        }
    }

    class DayItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var view: View = itemView
    }
}
