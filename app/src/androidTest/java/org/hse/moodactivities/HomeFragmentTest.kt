package org.hse.moodactivities

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkObject
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.responses.WeekAnalyticsResponse
import org.hse.moodactivities.services.ActivityService
import org.hse.moodactivities.services.MoodService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    companion object {
        const val BUTTON_OPEN_TEXT = "open"
        const val BUTTON_CLOSE_TEXT = "close"
        const val ANALYTICS_SUMMARY = "All good"
        const val ANALYTICS_RECOMMENDATION = "Try harder"
        const val DAILY_ACTIVITY = "Go for a walk"
        const val USER_DAILY_MOOD = 1
    }

    @Before
    fun setup() {
        // mock for user daily mood
        mockkObject(MoodService)
        every { MoodService.getUserDailyMood(any<AppCompatActivity>()) } returns USER_DAILY_MOOD
        every {
            MoodService.getUserDailyMood(
                any<AppCompatActivity>(), any<LocalDate>()
            )
        } returns USER_DAILY_MOOD
    }

    /**
     * The test checks the opening of the daily activity screen for when the button is pressed
     */
    @Test
    fun openDailyActivityOnButtonClick() {
        ActivityScenario.launch(
            MainScreenActivity::class.java
        )

        // mock for daily activity
        mockkObject(ActivityService)
        every { ActivityService.getDailyActivity(any<AppCompatActivity>()) } returns DAILY_ACTIVITY

        onView(withId(R.id.activity_widget_button)).perform(click())
        onView(withId(R.id.daily_activity_screen_title)).check(matches(isDisplayed()))
    }

    /**
     * The test checks the opening of the mood flow screen for when the button is pressed
     */
    @Test
    fun openMoodFlowOnButtonClick() {
        ActivityScenario.launch(MainScreenActivity::class.java)

        onView(withId(R.id.mood_widget_button)).perform(click())
        onView(withId(R.id.rate_day_screen_title)).check(matches(isDisplayed()))
    }

    /**
     * The test checks the text on the analytics widget for when the button is pressed
     */
    @Test
    fun openAnalyticsOnButtonClick() {
        ActivityScenario.launch(MainScreenActivity::class.java)

        // mock for analytics
        mockkObject(MoodService)
        every { MoodService.getWeekAnalytics(any<AppCompatActivity>()) } returns WeekAnalyticsResponse(
            ANALYTICS_SUMMARY, ANALYTICS_RECOMMENDATION
        )

        onView(withId(R.id.analytics_button_text)).check(matches(withText(BUTTON_OPEN_TEXT)))
        onView(withId(R.id.analytics_button)).perform(click())
        onView(withId(R.id.analytics_button_text)).check(matches(withText(BUTTON_CLOSE_TEXT)))

        onView(withId(R.id.analytics_summary)).check(matches(withText(ANALYTICS_SUMMARY)))
        onView(withId(R.id.analytics_recommendations)).check(
            matches(
                withText(
                    ANALYTICS_RECOMMENDATION
                )
            )
        )

    }

    /**
     * The test checks the text on the analytics widget for when the button is pressed
     */
    @Test
    fun checkWeekStatisticIconIsDisplayed() {
        ActivityScenario.launch(MainScreenActivity::class.java)

        onView(withId(R.id.week_widget_image_1)).check(matches(withDrawable(R.drawable.mood_flow_2)))
        onView(withId(R.id.week_widget_image_2)).check(matches(withDrawable(R.drawable.mood_flow_2)))
        onView(withId(R.id.week_widget_image_3)).check(matches(withDrawable(R.drawable.mood_flow_2)))
        onView(withId(R.id.week_widget_image_4)).check(matches(withDrawable(R.drawable.mood_flow_2)))
        onView(withId(R.id.week_widget_image_5)).check(matches(withDrawable(R.drawable.mood_flow_2)))
    }

    private fun withDrawable(@DrawableRes drawableResId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has drawable resource id: ").appendValue(drawableResId)
            }

            override fun matchesSafely(target: View?): Boolean {
                if (target !is ImageView) {
                    return false
                }
                val expectedDrawable =
                    ContextCompat.getDrawable(target.context, drawableResId) ?: return false
                val actualBitmap = (target.drawable as BitmapDrawable).bitmap
                val expectedBitmap = (expectedDrawable as BitmapDrawable).bitmap
                return actualBitmap.sameAs(expectedBitmap)
            }
        }
    }

}