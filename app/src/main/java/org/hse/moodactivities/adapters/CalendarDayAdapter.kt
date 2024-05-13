package org.hse.moodactivities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.hse.moodactivities.R
import org.hse.moodactivities.models.DailyActivityItemModel
import org.hse.moodactivities.models.DailyInfoItemModel
import org.hse.moodactivities.models.DailyItemModel
import org.hse.moodactivities.models.DailyItemType
import org.hse.moodactivities.models.DailyQuestionItemModel
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.UiUtils

enum class ViewType(val type: Int) {
    ACTIVITY(0), QUESTION(1), INFO(2), EMPTY(3)
}

class CalendarDayAdapter(
    private var dailyItemsList: ArrayList<DailyItemModel>
) : RecyclerView.Adapter<CalendarDayAdapter.CalendarDayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayViewHolder {
        val layoutId = getLayoutId(viewType)
        val viewHolder = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return CalendarDayViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return dailyItemsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (dailyItemsList[position].getDailyItemType()) {
            DailyItemType.DAILY_ACTIVITY -> ViewType.ACTIVITY.type
            DailyItemType.DAILY_QUESTION -> ViewType.QUESTION.type
            DailyItemType.DAILY_INFO -> ViewType.INFO.type
            else -> ViewType.EMPTY.type
        }
    }

    override fun onBindViewHolder(holder: CalendarDayViewHolder, position: Int) {
        val dailyItem: DailyItemModel = dailyItemsList[position]

        when (dailyItem.getDailyItemType()) {
            DailyItemType.DAILY_INFO -> fillHolderFromDailyInfoModel(
                holder, dailyItem as DailyInfoItemModel
            )

            DailyItemType.DAILY_QUESTION -> fillHolderFromDailyQuestionModel(
                holder, dailyItem as DailyQuestionItemModel
            )

            DailyItemType.DAILY_ACTIVITY -> fillHolderFromDailyActivityModel(
                holder, dailyItem as DailyActivityItemModel
            )

            DailyItemType.DAILY_EMPTY -> {}
        }
    }

    companion object {
        private fun getLayoutId(viewType: Int): Int {
            return when (viewType) {
                ViewType.ACTIVITY.type -> R.layout.widget_daily_activity_item
                ViewType.QUESTION.type -> R.layout.widget_daily_question_item
                ViewType.INFO.type -> R.layout.widget_daily_info_item
                else -> R.layout.widget_daily_empty_item
            }
        }

        private fun fillHolderFromDailyInfoModel(
            holder: CalendarDayViewHolder, dailyInfoItemModel: DailyInfoItemModel
        ) {
            val view = holder.getView()
            view.findViewById<TextView>(R.id.short_description).text =
                dailyInfoItemModel.getShortDescription()
            view.findViewById<TextView>(R.id.daily_question).text = dailyInfoItemModel.getQuestion()
            view.findViewById<TextView>(R.id.daily_answer).text =
                dailyInfoItemModel.getAnswerToQuestion()
            view.findViewById<TextView>(R.id.time).text = dailyInfoItemModel.getTime()
            view.findViewById<ImageView>(R.id.day_mood).setImageResource(
                UiUtils.getMoodImageResourcesIdByIndex(dailyInfoItemModel.getMoodRating() - 1)
            )
            view.findViewById<TextView>(R.id.activities).text =
                dailyInfoItemModel.getActivities().joinToString(", ") { it.getName() }
            view.findViewById<TextView>(R.id.emotions).text =
                dailyInfoItemModel.getEmotions().joinToString(", ") { it.getName() }

            // set color theme
            view.findViewById<CardView>(R.id.widget_card)
                .setCardBackgroundColor(ThemesService.getColor4())
            view.findViewById<CardView>(R.id.question_card)
                .setCardBackgroundColor(ThemesService.getDimmedColor4())
            view.findViewById<CardView>(R.id.activities_card)
                .setCardBackgroundColor(ThemesService.getDimmedColor4())
            view.findViewById<CardView>(R.id.emotions_card)
                .setCardBackgroundColor(ThemesService.getDimmedColor4())
        }

        private fun fillHolderFromDailyQuestionModel(
            holder: CalendarDayViewHolder, dailyQuestionItemModel: DailyQuestionItemModel
        ) {
            val view = holder.getView()
            view.findViewById<TextView>(R.id.daily_question).text =
                dailyQuestionItemModel.getDailyQuestion()
            view.findViewById<TextView>(R.id.answer_to_daily_question).text =
                dailyQuestionItemModel.getAnswerToDailyQuestion()
            view.findViewById<TextView>(R.id.time).text = dailyQuestionItemModel.getTime()

            // set color theme
            view.findViewById<CardView>(R.id.widget_card)
                .setCardBackgroundColor(ThemesService.getColor2())
            view.findViewById<CardView>(R.id.question_card)
                .setCardBackgroundColor(ThemesService.getDimmedColor2())
        }

        private fun fillHolderFromDailyActivityModel(
            holder: CalendarDayViewHolder, dailyActivityItemModel: DailyActivityItemModel
        ) {
            val view = holder.getView()
            view.findViewById<TextView>(R.id.daily_activity).text =
                dailyActivityItemModel.getDailyActivity()
            view.findViewById<TextView>(R.id.user_impressions).text =
                dailyActivityItemModel.getUserImpressions()
            view.findViewById<TextView>(R.id.time).text = dailyActivityItemModel.getTime()
            // set color theme
            view.findViewById<CardView>(R.id.widget_card)
                .setCardBackgroundColor(ThemesService.getColor5())
            view.findViewById<CardView>(R.id.activity_card)
                .setCardBackgroundColor(ThemesService.getDimmedColor5())
        }
    }

    class CalendarDayViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        fun getView(): View {
            return view
        }
    }
}
