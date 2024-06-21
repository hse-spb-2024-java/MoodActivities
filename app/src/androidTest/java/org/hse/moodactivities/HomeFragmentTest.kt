package org.hse.moodactivities

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class HomeFragmentTest {
    @Before
    fun setup() {
        mockkObject(MoodService)
        // mock user daily mood
        every { MoodService.getUserDailyMood(any<AppCompatActivity>()) } returns 1
        every { MoodService.getUserDailyMood(any<AppCompatActivity>(), any<LocalDate>()) } returns 1

        mockkObject(ActivityService)
        // mock user daily mood
        every { ActivityService.getDailyActivity(any<AppCompatActivity>()) } returns "Go for a walk"

        mockkObject(ActivityService)
        // mock user daily mood
        every { ActivityService.getDailyActivity(any<AppCompatActivity>()) } returns "Go for a walk"
    }

    @Test
    fun openDailyActivityOnButtonClick() {
        ActivityScenario.launch(MainScreenActivity::class.java)

        onView(withId(R.id.activity_widget_button)).perform(click())
        onView(withId(R.id.screen_tittle)).check(matches(isDisplayed()))
    }

    @Test
    fun openMoodFlowOnButtonClick() {
        ActivityScenario.launch(MainScreenActivity::class.java)

        onView(withId(R.id.mood_widget_button)).perform(click())
        onView(withId(R.id.mood_3_image)).check(matches(isDisplayed()))
    }
}