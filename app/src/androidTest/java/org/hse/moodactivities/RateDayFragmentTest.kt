package org.hse.moodactivities

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withAlpha
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkObject
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.services.MoodService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class RateDayFragmentTest {
    companion object {
        const val USER_DAILY_MOOD = 1
        const val PRESSED_BUTTON_ALPHA = 1.0f
        const val UNPRESSED_BUTTON_ALPHA = 0.5f
    }

    @Before
    fun setupMock() {
        mockkObject(MoodService)
        every { MoodService.getUserDailyMood(any<AppCompatActivity>()) } returns HomeFragmentTest.USER_DAILY_MOOD
        every {
            MoodService.getUserDailyMood(
                any<AppCompatActivity>(), any<LocalDate>()
            )
        } returns USER_DAILY_MOOD

        // start main activity
        ActivityScenario.launch(
            MainScreenActivity::class.java
        )

        // transit to rate day fragment
        onView(withId(R.id.mood_widget_button)).perform(click())
    }

    /**
     * The test checks the returning to the home screen for when the button is pressed
     */
    @Test
    fun testReturnToHomeScreenOnButtonClick() {
        onView(withId(R.id.return_home_button)).perform(click())
        onView(withId(R.id.home_screen_title)).check(matches(isDisplayed()))
    }

    /**
     * The test checks buttons state for when user change his mood rate
     */
    @Test
    fun testUserChooseMoodOnButtonClick() {
        onView(withId(R.id.button_background)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))

        onView(withId(R.id.mood_5_button)).perform(click())

        // check states for all button
        onView(withId(R.id.mood_1_image)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.mood_2_image)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.mood_3_image)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.mood_4_image)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.mood_5_image)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))

        onView(withId(R.id.button_background)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))
    }

    /**
     * The test checks buttons state for when user change his mood rate
     */
    @Test
    fun testUserChangeMoodOnButtonClick() {
        // user chose mood 5
        onView(withId(R.id.mood_5_button)).perform(click())

        onView(withId(R.id.mood_1_image)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.mood_5_image)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))

        // user changed his mind and chose mood 1
        onView(withId(R.id.mood_1_button)).perform(click())

        onView(withId(R.id.mood_1_image)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.mood_5_image)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))

        onView(withId(R.id.button_background)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))
    }

    /**
     * The test checks the transition to a new fragment
     */
    @Test
    fun testGoToNextFragmentOnButtonClick() {
        // user tried to go to the next fragment without choosing his mood (he can't)
        onView(withId(R.id.next_button)).perform(click())

        onView(withId(R.id.rate_day_screen_title)).check(matches(isDisplayed()))

        // user chose mood 5
        onView(withId(R.id.mood_5_button)).perform(click())

        // user tried to go to the next fragment with choosing his mood (he can)
        onView(withId(R.id.next_button)).perform(click())

        onView(withId(R.id.chose_activities_screen_title)).check(matches(isDisplayed()))
    }
}
