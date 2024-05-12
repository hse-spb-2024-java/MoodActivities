package org.hse.moodactivities.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import org.hse.moodactivities.activities.MoodFlowActivity
import org.hse.moodactivities.activities.QuestionsActivity
import org.hse.moodactivities.services.MoodService
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.utils.UiUtils
import org.hse.moodactivities.utils.Utils
import java.time.DayOfWeek
import java.time.LocalDate


class HomeScreenFragment : Fragment() {
    companion object {
        private val dayOfWeekTextId: Array<Int> = arrayOf(
            R.id.week_widget_day_of_week_5,
            R.id.week_widget_day_of_week_4,
            R.id.week_widget_day_of_week_3,
            R.id.week_widget_day_of_week_2,
            R.id.week_widget_day_of_week_1
        )
        private val dayOfMonthTextId: Array<Int> = arrayOf(
            R.id.week_widget_day_of_month_5,
            R.id.week_widget_day_of_month_4,
            R.id.week_widget_day_of_month_3,
            R.id.week_widget_day_of_month_2,
            R.id.week_widget_day_of_month_1
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        view.rootView.findViewById<ImageView>(R.id.mood_widget_icon).setImageResource(
            UiUtils.getMoodImageResourcesIdByIndex(MoodService.getUserDailyMood(this.activity as AppCompatActivity)!!)
        )
        setCurrentDate(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityWidgetButton: Button = view.rootView.findViewById(R.id.activity_widget_button)
        activityWidgetButton.setOnClickListener {
            Log.d("activity button", "clicked!")
        }

        val moodWidgetButton: Button = view.rootView.findViewById(R.id.mood_widget_button)
        moodWidgetButton.setOnClickListener {
            val moodFlowActivityIntent = Intent(this.activity, MoodFlowActivity::class.java)
            startActivity(moodFlowActivityIntent)
            this.activity?.finish()
        }

        val questionWidgetButton: Button = view.rootView.findViewById(R.id.question_widget_button)
        questionWidgetButton.setOnClickListener {
            val questionsActivity = Intent(this.activity, QuestionsActivity::class.java)
            startActivity(questionsActivity)
            this.activity?.finish()
        }

        val askWidgetButton: Button = view.rootView.findViewById(R.id.ask_widget_button)
        askWidgetButton.setOnClickListener {
            Log.d("ask button", "clicked!")
        }

        setColorTheme()
    }

    private fun setCurrentDate(view: View) {
        val localDate = LocalDate.now()

        for (daysBefore in 0L..4L) {
            val localDateBefore = localDate.minusDays(daysBefore)
            setDateToWeekWidget(
                localDateBefore.dayOfWeek,
                localDateBefore.dayOfMonth,
                view.rootView.findViewById(dayOfWeekTextId[daysBefore.toInt()]),
                view.rootView.findViewById(dayOfMonthTextId[daysBefore.toInt()])
            )
        }
    }

    private fun setDateToWeekWidget(
        dayOfWeek: DayOfWeek, day: Int, dayOfWeekTextView: TextView, dayTextView: TextView
    ) {
        dayOfWeekTextView.text = getString(Utils.getStringByDayOfWeek(dayOfWeek))
        dayTextView.text = day.toString()
    }

    private fun setColorTheme() {
        // set background color
        view?.findViewById<LinearLayout>(R.id.home_screen_fragment_layout)
            ?.setBackgroundColor(ThemesService.getBackgroundColor())

        // set colors to activity widget
        view?.findViewById<CardView>(R.id.activity_card)
            ?.setCardBackgroundColor(ThemesService.getColor1())
        view?.findViewById<CardView>(R.id.activity_circle)
            ?.setCardBackgroundColor(ThemesService.getDimmedColor1())

        // set colors to mood flow widget
        view?.findViewById<CardView>(R.id.mood_flow_card)
            ?.setCardBackgroundColor(ThemesService.getColor2())
        view?.findViewById<CardView>(R.id.mood_flow_circle)
            ?.setCardBackgroundColor(ThemesService.getDimmedColor2())

        // set colors to daily question widget
        view?.findViewById<CardView>(R.id.question_card)
            ?.setCardBackgroundColor(ThemesService.getColor3())
        view?.findViewById<CardView>(R.id.question_circle)
            ?.setCardBackgroundColor(ThemesService.getDimmedColor3())

        // set color to days of week widgets
        view?.findViewById<CardView>(R.id.day_1)
            ?.setCardBackgroundColor(ThemesService.getColor4())
        view?.findViewById<CardView>(R.id.day_2)
            ?.setCardBackgroundColor(ThemesService.getColor4())
        view?.findViewById<CardView>(R.id.day_3)
            ?.setCardBackgroundColor(ThemesService.getColor4())
        view?.findViewById<CardView>(R.id.day_4)
            ?.setCardBackgroundColor(ThemesService.getColor4())
        view?.findViewById<CardView>(R.id.day_5)
            ?.setCardBackgroundColor(ThemesService.getColor4())

        // set color to chat widget
        view?.findViewById<CardView>(R.id.chat_widget)
            ?.setCardBackgroundColor(ThemesService.getColor5())
    }
}
