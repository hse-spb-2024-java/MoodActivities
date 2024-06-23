package org.hse.moodactivities

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkObject
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.services.ActivityService
import org.hse.moodactivities.services.MoodService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class DailyActivityTest {
    companion object {
        const val DAILY_ACTIVITY = "Go for a walk"
        const val USERS_IMPRESSIONS = "I like the weather"
        const val UNAVAILABLE_MESSAGE = "You completed today's activity,\ncome back later!"
        const val USER_DAILY_MOOD = 1
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
    }

    @Test
    fun testDailyActivityFlowNotFinished() {
        // mock for daily activity
        mockkObject(ActivityService)
        every { ActivityService.getDailyActivity(any<AppCompatActivity>()) } returns DAILY_ACTIVITY

        // transit to daily activity screen
        onView(withId(R.id.activity_widget_button)).perform(click())

        onView(withId(R.id.activity_text)).check(
            matches(
                withText(
                    DAILY_ACTIVITY
                )
            )
        )

        // click button to complete daily activity
        onView(withId(R.id.complete_button)).perform(click())

        // fill dialog with users impressions about activity
        onView(withId(R.id.users_impressions)).perform(typeText(USERS_IMPRESSIONS))
            .check(matches(withText(USERS_IMPRESSIONS)))

        mockkObject(ActivityService)
        every {
            ActivityService.recordDailyActivity(
                any<AppCompatActivity>(), any<String>()
            )
        } returns Unit

        // press finish button
        onView(withId(R.id.finish_button)).perform(click())
    }

    @Test
    fun testDailyActivityFlowFinished() {
        // mock for daily activity
        mockkObject(ActivityService)
        every { ActivityService.getDailyActivity(any<AppCompatActivity>()) } returns null

        // transit to daily activity screen
        onView(withId(R.id.activity_widget_button)).perform(click())

        onView(withId(R.id.activity_tittle)).check(
            matches(
                withText(
                    UNAVAILABLE_MESSAGE
                )
            )
        )
    }

    @Test
    fun testReturnToHomeScreen() {
        // mock for daily activity
        mockkObject(ActivityService)
        every { ActivityService.getDailyActivity(any<AppCompatActivity>()) } returns null

        // transit to daily activity screen
        onView(withId(R.id.activity_widget_button)).perform(click())

        // transit to home screen screen
        onView(withId(R.id.return_home_button)).perform(click())

        onView(withId(R.id.home_screen_title)).check(matches(ViewMatchers.isDisplayed()))
    }
}
