package org.hse.moodactivities.fragments

import GoogleSignInManager
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import org.hse.moodactivities.R
import org.hse.moodactivities.activities.StatisticActivity
import org.hse.moodactivities.managers.FitnessDataManager
import org.hse.moodactivities.models.GoogleFitRepositoryImpl
import org.hse.moodactivities.services.ChartsService
import org.hse.moodactivities.services.HealthService
import org.hse.moodactivities.services.StatisticMode
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.services.TimePeriod
import org.hse.moodactivities.viewmodels.UserViewModel


class InsightsScreenFragment : Fragment() {
    companion object {
        val DEFAULT_TIME_PERIOD = TimePeriod.Value.WEEK
        const val FITNESS_ENABLE_SYNCHRONIZATION = "Tap to synchronize"
    }

    enum class ChartsType {
        ACTIVITIES_CHART, EMOTIONS_CHART, MOOD_CHART, WEATHER_BY_TEMPERATURE_CHART, WEATHER_BY_HUMIDITY_CHART, WEATHER_DESCRIPTION_CHART, STEPS_CHART
    }

    private lateinit var dialog: Dialog
    private lateinit var resources: Resources

    // charts settings
    private lateinit var chartsService: ChartsService
    private lateinit var moodChart: LineChart
    private lateinit var weatherByTemperatureChart: LineChart
    private lateinit var weatherByHumidityChart: LineChart
    private lateinit var weatherDescriptionChart: LineChart
    private lateinit var stepsChart: LineChart
    private var changingTimePeriodChartType: ChartsType = ChartsType.MOOD_CHART
    private lateinit var moodChartLabel: TextView
    private lateinit var emotionsChartLabel: TextView
    private lateinit var activitiesChartLabel: TextView
    private lateinit var weatherByTemperatureChartLabel: TextView
    private lateinit var weatherByHumidityChartLabel: TextView
    private lateinit var weatherDescriptionChartLabel: TextView
    private lateinit var stepsLabel: TextView

    private lateinit var userViewModel: UserViewModel
    private lateinit var healthService: HealthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setTimePeriodToChart(timePeriod: TimePeriod.Value) {
        when (changingTimePeriodChartType) {
            ChartsType.MOOD_CHART -> {
                chartsService.createMoodCharts(resources, moodChart, timePeriod)
                changeTimeLabel(moodChartLabel, timePeriod)
            }

            ChartsType.EMOTIONS_CHART -> {
                chartsService.createFrequentlyUsedEmotions(resources, requireView(), timePeriod)
                changeTimeLabel(emotionsChartLabel, timePeriod)
            }

            ChartsType.ACTIVITIES_CHART -> {
                chartsService.createFrequentlyUsedActivities(resources, requireView(), timePeriod)
                changeTimeLabel(activitiesChartLabel, timePeriod)
            }

            ChartsType.WEATHER_BY_TEMPERATURE_CHART -> {
                chartsService.createWeatherCharts(weatherByTemperatureChart, timePeriod)
                changeTimeLabel(weatherByTemperatureChartLabel, timePeriod)
            }

            ChartsType.WEATHER_BY_HUMIDITY_CHART -> {
                chartsService.createHumidityCharts(weatherByHumidityChart, timePeriod)
                changeTimeLabel(weatherByHumidityChartLabel, timePeriod)
            }

            ChartsType.WEATHER_DESCRIPTION_CHART -> {
                chartsService.createWeatherDescriptionCharts(weatherDescriptionChart, timePeriod)
                changeTimeLabel(weatherDescriptionChartLabel, timePeriod)
            }

            ChartsType.STEPS_CHART -> {
                chartsService.createStepsCharts(resources, stepsChart, timePeriod)
                changeTimeLabel(stepsLabel, timePeriod)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_insights_screen, container, false)
        resources = this.requireActivity().resources
        chartsService = ChartsService(this.requireActivity() as AppCompatActivity)

        // create mood chart
        moodChart = view.findViewById(R.id.week_mood_chart)
        moodChartLabel = view.findViewById(R.id.mood_flow_time_label)
        changeTimeLabel(moodChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createMoodCharts(resources, moodChart, DEFAULT_TIME_PERIOD)

        // create weather chart
        weatherByTemperatureChart = view.findViewById(R.id.weather_chart)
        weatherByTemperatureChartLabel = view.findViewById(R.id.weather_time_label)
        changeTimeLabel(weatherByTemperatureChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createWeatherCharts(
            weatherByTemperatureChart, DEFAULT_TIME_PERIOD
        )

        // create humidity chart
        weatherByHumidityChart = view.findViewById(R.id.humidity_chart)
        weatherByHumidityChartLabel = view.findViewById(R.id.humidity_time_label)
        changeTimeLabel(weatherByHumidityChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createHumidityCharts(
            weatherByHumidityChart, DEFAULT_TIME_PERIOD
        )

        // create description chart
        weatherDescriptionChart = view.findViewById(R.id.description_chart)
        weatherDescriptionChartLabel = view.findViewById(R.id.description_time_label)
        changeTimeLabel(weatherDescriptionChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createWeatherDescriptionCharts(
            weatherDescriptionChart, DEFAULT_TIME_PERIOD
        )

        // create emotions chart
        emotionsChartLabel = view.findViewById(R.id.emotions_time_label)
        changeTimeLabel(emotionsChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createFrequentlyUsedEmotions(resources, view, DEFAULT_TIME_PERIOD)
        view.findViewById<Button>(R.id.emotions_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.EMOTIONS)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
        }

        // create activities chart
        activitiesChartLabel = view.findViewById(R.id.activities_time_label)
        changeTimeLabel(activitiesChartLabel, DEFAULT_TIME_PERIOD)
        chartsService.createFrequentlyUsedActivities(resources, view, DEFAULT_TIME_PERIOD)
        view.findViewById<Button>(R.id.activities_statistic).setOnClickListener {
            ChartsService.setStatisticMode(StatisticMode.ACTIVITIES)
            startActivity(Intent(this.activity, StatisticActivity::class.java))
            this.activity?.finish()
        }

        // create days in row
        chartsService.createDaysInRow(view.findViewById(R.id.days_in_row))

        // create dialog to change time periods in charts
        dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.dialog_choose_time_period)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true)

        // set week button
        dialog.findViewById<Button>(R.id.week).setOnClickListener {
            pressDialogButton(TimePeriod.Value.WEEK)
        }
        // set month button
        dialog.findViewById<Button>(R.id.month).setOnClickListener {
            pressDialogButton(TimePeriod.Value.MONTH)
        }
        // set year button
        dialog.findViewById<Button>(R.id.year).setOnClickListener {
            pressDialogButton(TimePeriod.Value.YEAR)
        }
        // set all time button
        dialog.findViewById<Button>(R.id.all_time).setOnClickListener {
            pressDialogButton(TimePeriod.Value.ALL_TIME)
        }

        // create time labels to change time periods
        // create mood time label
        view.findViewById<Button>(R.id.mood_flow_time_label_button).setOnClickListener {
            changingTimePeriodChartType = ChartsType.MOOD_CHART
            dialog.show()
        }
        // create emotions time label
        view.findViewById<Button>(R.id.emotions_time_label_button).setOnClickListener {
            changingTimePeriodChartType = ChartsType.EMOTIONS_CHART
            dialog.show()
        }
        // create activities time label
        view.findViewById<Button>(R.id.activities_time_label_button).setOnClickListener {
            changingTimePeriodChartType = ChartsType.ACTIVITIES_CHART
            dialog.show()
        }

        // create temperature time label
        view.findViewById<Button>(R.id.weather_time_label_button).setOnClickListener {
            changingTimePeriodChartType = ChartsType.WEATHER_BY_TEMPERATURE_CHART
            dialog.show()
        }

        // create humidity time label
        view.findViewById<Button>(R.id.humidity_time_label_button).setOnClickListener {
            changingTimePeriodChartType = ChartsType.WEATHER_BY_HUMIDITY_CHART
            dialog.show()
        }

        // create weather description time label
        view.findViewById<Button>(R.id.description_time_label_button).setOnClickListener {
            changingTimePeriodChartType = ChartsType.WEATHER_DESCRIPTION_CHART
            dialog.show()
        }

        // set users steps
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val googleFitRepository = GoogleFitRepositoryImpl(requireContext())
        val fitnessDataManager = FitnessDataManager(googleFitRepository)
        healthService = HealthService(activity as AppCompatActivity, fitnessDataManager)
        healthService.onCreate()

        val stepsCounterTextView = view.findViewById<TextView>(R.id.steps_counter)
        val stepsWidget = view.findViewById<CardView>(R.id.steps_widget)
        if (GoogleSignIn.getLastSignedInAccount(requireContext()) == null) {
            // No synchronization, sorry :(
            stepsCounterTextView.text = FITNESS_ENABLE_SYNCHRONIZATION
            stepsWidget.setOnClickListener {
                performGoogleSignIn()
            }

            // create steps chart
            stepsChart = view.findViewById(R.id.steps_insights_chart)
            stepsLabel = view.findViewById(R.id.steps_insights_time_label)
            chartsService.createStepsCharts(resources, stepsChart, DEFAULT_TIME_PERIOD)
            changeTimeLabel(stepsLabel, DEFAULT_TIME_PERIOD)
            view.findViewById<Button>(R.id.steps_insights_time_label_button).setOnClickListener {
                changingTimePeriodChartType = ChartsType.STEPS_CHART
                dialog.show()
            }
        } else {
            stepsWidget.setOnClickListener(null)
            healthService.loadAndSendFitnessData()
            healthService.fitnessData.observe(viewLifecycleOwner) { data ->
                stepsCounterTextView.text = data.steps.toString()
            }
        }

        setColorTheme(view)

        healthService.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(
                    view, it, Snackbar.LENGTH_LONG
                ).show()
                healthService.clearErrorMessage()
            }
        }

        return view
    }

    private fun changeTimeLabel(textView: TextView, timePeriod: TimePeriod.Value) {
        textView.text = timePeriod.toString()
    }

    private fun pressDialogButton(timePeriod: TimePeriod.Value) {
        setTimePeriodToChart(timePeriod)
        dialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleSignInManager.RETURN_CODE_SIGN_IN) {
            // Reload current fragment
            val fragmentTransaction = requireFragmentManager().beginTransaction()
            fragmentTransaction.detach(this).attach(this).commit()
        }
    }

    private fun performGoogleSignIn() {
        val signInIntent = GoogleSignInManager.getSignInIntent()
        startActivityForResult(signInIntent, GoogleSignInManager.RETURN_CODE_SIGN_IN)
    }

    private fun setColorTheme(view: View) {
        val colorTheme = ThemesService.getColorTheme()

        // set color to status bar
        activity?.window?.statusBarColor = colorTheme.getBackgroundColor()

        // set color to background
        view.findViewById<ConstraintLayout>(R.id.layout)
            ?.setBackgroundColor(colorTheme.getBackgroundColor())

        // set color to title
        view.findViewById<TextView>(R.id.title)?.setTextColor(colorTheme.getFontColor())

        // set color to days in rows
        view.findViewById<CardView>(R.id.days_in_row_background)
            ?.setCardBackgroundColor(colorTheme.getDaysInRowColor())
        view.findViewById<TextView>(R.id.days_in_row_text)
            ?.setTextColor(colorTheme.getDaysInRowTextColor())

        // set color to mood flow
        view.findViewById<CardView>(R.id.mood_flow_background)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowChartBackgroundColor())
        view.findViewById<TextView>(R.id.mood_flow_text)
            ?.setTextColor(colorTheme.getMoodFlowChartTextColor())
        view.findViewById<CardView>(R.id.mood_flow_time_label_background)
            ?.setCardBackgroundColor(colorTheme.getMoodFlowChartLabelColor())
        view.findViewById<TextView>(R.id.mood_flow_time_label)
            ?.setTextColor(colorTheme.getMoodFlowChartLabelTextColor())

        // set colors to frequently used emotions
        // set background
        view.findViewById<CardView>(R.id.frequently_used_emotions)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedColor())

        // set title
        view.findViewById<TextView>(R.id.frequently_used_emotions_text)
            ?.setTextColor(colorTheme.getFrequentlyUsedTextColor())

        // set time label colors
        view.findViewById<CardView>(R.id.emotions_time_label_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedLabelColor())
        view.findViewById<TextView>(R.id.emotions_time_label)
            ?.setTextColor(colorTheme.getFrequentlyUsedLabelTextColor())

        // set emotion card 1 colors
        view.findViewById<CardView>(R.id.emotion_icon_1_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedItemColor())
        view.findViewById<TextView>(R.id.emotion_1)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemNameColor())
        view.findViewById<TextView>(R.id.emotion_counter_1)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemCounterColor())

        // set emotion card 2 colors
        view.findViewById<CardView>(R.id.emotion_icon_2_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedItemColor())
        view.findViewById<TextView>(R.id.emotion_2)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemNameColor())
        view.findViewById<TextView>(R.id.emotion_counter_2)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemCounterColor())

        // set emotion card 3 colors
        view.findViewById<CardView>(R.id.emotion_icon_3_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedItemColor())
        view.findViewById<TextView>(R.id.emotion_3)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemNameColor())
        view.findViewById<TextView>(R.id.emotion_counter_3)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemCounterColor())

        // next statistic button
        view.findViewById<TextView>(R.id.more_emotions)
            ?.setTextColor(colorTheme.getFrequentlyUsedTextColor())

        // set colors to frequently used activities
        // set background
        view.findViewById<CardView>(R.id.frequently_used_activities)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedColor())

        // set title
        view.findViewById<TextView>(R.id.frequently_used_activities_text)
            ?.setTextColor(colorTheme.getFrequentlyUsedTextColor())

        // set time label colors
        view.findViewById<CardView>(R.id.activities_time_label_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedLabelColor())
        view.findViewById<TextView>(R.id.activities_time_label)
            ?.setTextColor(colorTheme.getFrequentlyUsedLabelTextColor())

        // set activity icon 1 colors
        view.findViewById<CardView>(R.id.activity_icon_1_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedItemColor())
        view.findViewById<TextView>(R.id.activity_1)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemNameColor())
        view.findViewById<TextView>(R.id.activity_counter_1)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemCounterColor())

        // set activity icon 2 colors
        view.findViewById<CardView>(R.id.activity_icon_2_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedItemColor())
        view.findViewById<TextView>(R.id.activity_2)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemNameColor())
        view.findViewById<TextView>(R.id.activity_counter_2)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemCounterColor())

        // set activity icon 1 colors
        view.findViewById<CardView>(R.id.activity_icon_3_card)
            ?.setCardBackgroundColor(colorTheme.getFrequentlyUsedItemColor())
        view.findViewById<TextView>(R.id.activity_3)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemNameColor())
        view.findViewById<TextView>(R.id.activity_counter_3)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemCounterColor())

        // next statistic button
        view.findViewById<TextView>(R.id.more_activities)
            ?.setTextColor(colorTheme.getFrequentlyUsedItemCounterColor())

        // set color to dialog background
        dialog.findViewById<CardView>(R.id.dialog_background)
            .setCardBackgroundColor(colorTheme.getTimePeriodDialogCardColor())

        // set colors to dialog buttons
        dialog.findViewById<CardView>(R.id.week_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
        dialog.findViewById<TextView>(R.id.week_text).setTextColor(colorTheme.getButtonTextColor())

        dialog.findViewById<CardView>(R.id.month_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
        dialog.findViewById<TextView>(R.id.month_text).setTextColor(colorTheme.getButtonTextColor())

        dialog.findViewById<CardView>(R.id.year_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
        dialog.findViewById<TextView>(R.id.year_text).setTextColor(colorTheme.getButtonTextColor())

        dialog.findViewById<CardView>(R.id.all_time_background)
            .setCardBackgroundColor(colorTheme.getButtonColor())
        dialog.findViewById<TextView>(R.id.all_time_text)
            .setTextColor(colorTheme.getButtonTextColor())

        // set colors to steps widget
        view.findViewById<CardView>(R.id.steps_widget)
            ?.setCardBackgroundColor(colorTheme.getWeatherChartBackgroundColor())
        view.findViewById<TextView>(R.id.steps_tittle)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())
        view.findViewById<TextView>(R.id.steps_counter)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())

        // set color to weather chart
        view.findViewById<CardView>(R.id.weather_background)
            ?.setCardBackgroundColor(colorTheme.getWeatherChartBackgroundColor())
        view.findViewById<TextView>(R.id.weather_text)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())
        view.findViewById<CardView>(R.id.weather_time_label_background)
            ?.setCardBackgroundColor(colorTheme.getWeatherChartLabelColor())
        view.findViewById<TextView>(R.id.weather_time_label)
            ?.setTextColor(colorTheme.getWeatherChartLabelTextColor())

        // set color to weather chart
        view.findViewById<CardView>(R.id.humidity_background)
            ?.setCardBackgroundColor(colorTheme.getWeatherChartBackgroundColor())
        view.findViewById<TextView>(R.id.humidity_text)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())
        view.findViewById<CardView>(R.id.humidity_time_label_background)
            ?.setCardBackgroundColor(colorTheme.getWeatherChartLabelColor())
        view.findViewById<TextView>(R.id.humidity_time_label)
            ?.setTextColor(colorTheme.getWeatherChartLabelTextColor())

        // set color to weather chart
        view.findViewById<CardView>(R.id.description_background)
            ?.setCardBackgroundColor(colorTheme.getWeatherChartBackgroundColor())
        view.findViewById<TextView>(R.id.description_text)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())

        view.findViewById<TextView>(R.id.windy_weather)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())
        view.findViewById<TextView>(R.id.snowy_weather)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())
        view.findViewById<TextView>(R.id.rainy_weather)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())
        view.findViewById<TextView>(R.id.cloudy_weather)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())
        view.findViewById<TextView>(R.id.clear_weather)
            ?.setTextColor(colorTheme.getWeatherChartTextColor())

        view.findViewById<CardView>(R.id.description_time_label_background)
            ?.setCardBackgroundColor(colorTheme.getWeatherChartLabelColor())
        view.findViewById<TextView>(R.id.description_time_label)
            ?.setTextColor(colorTheme.getWeatherChartLabelTextColor())

        // set colors to steps chart
        view.findViewById<TextView>(R.id.steps_tittle)
            ?.setTextColor(colorTheme.getStepsChartTextColor())
        view.findViewById<TextView>(R.id.steps_counter)
            ?.setTextColor(colorTheme.getStepsChartTextColor())
        view.findViewById<CardView>(R.id.steps_widget)
            ?.setCardBackgroundColor(colorTheme.getStepsChartColor())

        view.findViewById<CardView>(R.id.steps_insights_background)
            ?.setCardBackgroundColor(colorTheme.getStepsChartColor())
        view.findViewById<TextView>(R.id.steps_insights_text)
            ?.setTextColor(colorTheme.getStepsChartTextColor())
        view.findViewById<CardView>(R.id.steps_insights_time_label_background)
            ?.setCardBackgroundColor(colorTheme.getStepsLabelColor())
        view.findViewById<TextView>(R.id.steps_insights_time_label)
            ?.setTextColor(colorTheme.getStepsLabelTextColor())
    }
}
