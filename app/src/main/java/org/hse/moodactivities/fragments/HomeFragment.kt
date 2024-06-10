package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.ChatActivity
import org.hse.moodactivities.activities.DailyActivity
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.activities.QuestionsActivity
import org.hse.moodactivities.services.MoodService
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.UiUtils
import org.hse.moodactivities.utils.Utils
import java.time.DayOfWeek
import java.time.LocalDate


class HomeFragment : Fragment() {
    companion object {
        const val AMOUNT_OF_DAYS = 5

        private val dayOfWeekCardId: Array<Int> = arrayOf(
            R.id.day_5,
            R.id.day_4,
            R.id.day_3,
            R.id.day_2,
            R.id.day_1,
        )
        private val dayOfWeekTextId: Array<Int> = arrayOf(
            R.id.week_widget_day_of_week_5,
            R.id.week_widget_day_of_week_4,
            R.id.week_widget_day_of_week_3,
            R.id.week_widget_day_of_week_2,
            R.id.week_widget_day_of_week_1,
        )
        private val dayOfMonthTextId: Array<Int> = arrayOf(
            R.id.week_widget_day_of_month_5,
            R.id.week_widget_day_of_month_4,
            R.id.week_widget_day_of_month_3,
            R.id.week_widget_day_of_month_2,
            R.id.week_widget_day_of_month_1,
        )
        private val dayOfWeekImageId: Array<Int> = arrayOf(
            R.id.week_widget_image_5,
            R.id.week_widget_image_4,
            R.id.week_widget_image_3,
            R.id.week_widget_image_2,
            R.id.week_widget_image_1,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        view.rootView.findViewById<ImageView>(R.id.mood_widget_icon).setImageResource(
            UiUtils.getMoodImageResourcesIdByIndex(MoodService.getUserDailyMood(this.activity as AppCompatActivity))
        )
        setCurrentDate(view)

        setColorTheme(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityWidgetButton: Button = view.rootView.findViewById(R.id.activity_widget_button)
        activityWidgetButton.setOnClickListener {
            val dailyActivityIntent = Intent(this.activity, DailyActivity::class.java)
            startActivity(dailyActivityIntent)
        }

        val moodWidgetButton: Button = view.rootView.findViewById(R.id.mood_widget_button)
        moodWidgetButton.setOnClickListener {
            val moodFlowActivityIntent = Intent(this.activity, MoodFlowActivity::class.java)
            startActivity(moodFlowActivityIntent)
        }

        val questionWidgetButton: Button = view.rootView.findViewById(R.id.question_widget_button)
        questionWidgetButton.setOnClickListener {
            val questionsActivity = Intent(this.activity, QuestionsActivity::class.java)
            startActivity(questionsActivity)
        }

        val askWidgetButton: Button = view.rootView.findViewById(R.id.ask_widget_button)
        askWidgetButton.setOnClickListener {
            val chatActivity = Intent(this.activity, ChatActivity::class.java)
            startActivity(chatActivity)
        }
    }

    private fun setCurrentDate(view: View) {
        val localDate = LocalDate.now()

        for (daysBefore in 0L..<AMOUNT_OF_DAYS) {
            val localDateBefore = localDate.minusDays(daysBefore)
            setDateToWeekWidget(
                localDateBefore.dayOfWeek,
                localDateBefore.dayOfMonth,
                view.rootView.findViewById(dayOfWeekTextId[daysBefore.toInt()]),
                view.rootView.findViewById(dayOfMonthTextId[daysBefore.toInt()])
            )
            view.rootView.findViewById<ImageView>(dayOfWeekImageId[daysBefore.toInt()])
                .setImageResource(
                    UiUtils.getMoodImageResourcesIdByIndex(
                        MoodService.getUserDailyMood(
                            this.activity as AppCompatActivity, localDateBefore
                        )
                    )
                )
        }
    }

    private fun setDateToWeekWidget(
        dayOfWeek: DayOfWeek, day: Int, dayOfWeekTextView: TextView, dayTextView: TextView
    ) {
        dayOfWeekTextView.text = Utils.getStringByDayOfWeek(dayOfWeek)
        dayTextView.text = day.toString()
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        // set background color
        view.findViewById<LinearLayout>(R.id.layout)
            ?.setBackgroundColor(colorTheme.getBackgroundColor())

        // set font color to titles
        view.findViewById<TextView>(R.id.home_screen_title)
            ?.setTextColor(colorTheme.getFontColor())

        // set colors to daily activity widget
        view.findViewById<CardView>(R.id.activity_card)
            ?.setCardBackgroundColor(colorTheme.getDailyActivityWidgetColor())
        view.findViewById<CardView>(R.id.activity_circle)
            ?.setCardBackgroundColor(colorTheme.getDailyActivityWidgetIconColor())
        view.findViewById<TextView>(R.id.activity_widget_title)
            ?.setTextColor(colorTheme.getDailyActivityWidgetTextColor())

        // set colors to mood flow widget
        view.findViewById<CardView>(R.id.mood_flow_card)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowWidgetColor())
        view.findViewById<CardView>(R.id.mood_flow_circle)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowWidgetIconColor())
        view.findViewById<TextView>(R.id.mood_widget_title)
            ?.setTextColor(colorTheme.getMoodFlowWidgetTextColor())

        // set colors to daily question widget
        view.findViewById<CardView>(R.id.question_card)
            ?.setCardBackgroundColor(colorTheme.getDailyQuestionWidgetColor())
        view.findViewById<CardView>(R.id.question_circle)
            ?.setCardBackgroundColor(colorTheme.getDailyQuestionWidgetIconColor())
        view.findViewById<TextView>(R.id.question_widget_tittle)
            ?.setTextColor(colorTheme.getDailyQuestionWidgetTextColor())

        // set color to week statistic widgets
        view.findViewById<TextView>(R.id.week_widget_tittle)
            ?.setTextColor(colorTheme.getFontColor())

        for (day in 0..<AMOUNT_OF_DAYS) {
            view.findViewById<TextView>(R.id.week_widget_tittle)
                ?.setTextColor(colorTheme.getFontColor())
            view.findViewById<CardView>(dayOfWeekCardId[day])
                ?.setCardBackgroundColor(colorTheme.getWeekStatisticDayWidgetColor())
            view.findViewById<TextView>(dayOfWeekTextId[day])
                ?.setTextColor(colorTheme.getWeekStatisticDayWidgetTextColor())
            view.findViewById<TextView>(dayOfMonthTextId[day])
                ?.setTextColor(colorTheme.getWeekStatisticDayWidgetTextColor())
        }

        // set color to chat widget
        view.findViewById<CardView>(R.id.chat_widget)
            ?.setCardBackgroundColor(colorTheme.getChatWidgetColor())
        view.findViewById<TextView>(R.id.ask_widget_text)
            ?.setTextColor(colorTheme.getChatWidgetColorTextColor())
    }
}
