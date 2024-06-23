package org.hse.moodactivities

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkObject
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.responses.MonthStatisticResponse
import org.hse.moodactivities.services.CalendarService
import org.hse.moodactivities.services.MoodService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class CalendarFragmentTest {
    companion object {
        const val USER_DAILY_MOOD = 1
        const val DATE_PATTERN = "MMMM yyyy"
    }

    @Before
    fun setupMocks() {
        mockkObject(MoodService)
        every { MoodService.getUserDailyMood(any<AppCompatActivity>()) } returns USER_DAILY_MOOD
        every {
            MoodService.getUserDailyMood(
                any<AppCompatActivity>(), any<LocalDate>()
            )
        } returns USER_DAILY_MOOD

        // start main activity
        ActivityScenario.launch(
            MainScreenActivity::class.java
        )

        mockkObject(CalendarService)
        val monthStatisticResponse = MonthStatisticResponse()
        monthStatisticResponse.setMoodRates(
            hashMapOf(
                1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5
            )
        )

        every {
            CalendarService.getMonthMoodStatistic(
                any<AppCompatActivity>(), any<Int>(), any<Int>()
            )
        } returns monthStatisticResponse

        // transit to calendar fragment
        onView(withId(R.id.bottom_menu_history)).perform(click())
    }

    @Test
    fun testCheckCurrentMonth() {
        val date = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val formattedDate = date.format(formatter)

        // check date correctness
        onView(withId(R.id.month_year_text_view)).check(
            matches(
                withText(
                    formattedDate
                )
            )
        )
    }

    @Test
    fun testCheckWeekStatistic() {
        // check week statistic correctness
        onView(withId(R.id.day_1_text)).check(
            matches(
                withText(
                    "20%"
                )
            )
        )
        onView(withId(R.id.day_2_text)).check(
            matches(
                withText(
                    "20%"
                )
            )
        )
        onView(withId(R.id.day_3_text)).check(
            matches(
                withText(
                    "20%"
                )
            )
        )
        onView(withId(R.id.day_4_text)).check(
            matches(
                withText(
                    "20%"
                )
            )
        )
        onView(withId(R.id.day_5_text)).check(
            matches(
                withText(
                    "20%"
                )
            )
        )
    }

    @Test
    fun testCheckPreviousMonthOnPressButton() {
        var date = LocalDate.now()
        date = date.minusMonths(1)
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val formattedDate = date.format(formatter)

        // change month to previous month
        onView(withId(R.id.previous_button)).perform(click())

        // check date correctness
        onView(withId(R.id.month_year_text_view)).check(
            matches(
                withText(
                    formattedDate
                )
            )
        )
    }

    @Test
    fun testCheckNextMonthOnPressButton() {
        var date = LocalDate.now()
        date = date.plusMonths(1)
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        val formattedDate = date.format(formatter)

        // change month to next month
        onView(withId(R.id.next_button)).perform(click())

        // check date correctness
        onView(withId(R.id.month_year_text_view)).check(
            matches(
                withText(
                    formattedDate
                )
            )
        )
    }
}
