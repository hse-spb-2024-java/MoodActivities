package org.hse.moodactivities

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withAlpha
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkObject
import org.hamcrest.Matcher
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.adapters.ItemAdapter
import org.hse.moodactivities.services.MoodService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ChooseActivitiesFragmentTest {
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

        // transit to choose activities fragment
        onView(withId(R.id.mood_1_button)).perform(click())
        onView(withId(R.id.next_button)).perform(click())
    }

    @Test
    fun testTransitToPreviousFragment() {
        // user tried to go to the previous fragment without choosing his mood
        onView(withId(R.id.back_button)).perform(click())

        onView(withId(R.id.rate_day_screen_title)).check(matches(isDisplayed()))
    }

    @Test
    fun testTransitToNextFragment() {
        // user tried to go to the next fragment without choosing his mood (he can't)
        onView(withId(R.id.button_background)).check(
            matches(
                withAlpha(
                    UNPRESSED_BUTTON_ALPHA
                )
            )
        )
        onView(withId(R.id.next_button)).perform(click())

        onView(withId(R.id.recycler_view)).perform(
            actionOnItemAtPosition<ItemAdapter.ItemHolder>(1, clickItemWithId(R.id.button))
        )

        onView(withId(R.id.button_background)).check(
            matches(
                withAlpha(
                    PRESSED_BUTTON_ALPHA
                )
            )
        )

        // user tried to go to the next fragment with choosing his mood (he can)
        onView(withId(R.id.next_button)).perform(click())

        onView(withId(R.id.choose_emotions_screen_title)).check(matches(isDisplayed()))
    }

    private fun clickItemWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val itemView: View = view.findViewById(id)
                itemView.performClick()
            }
        }
    }
}
