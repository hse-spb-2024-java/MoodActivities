package org.hse.moodactivities

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withAlpha
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkObject
import org.hse.moodactivities.activities.MainScreenActivity
import org.hse.moodactivities.color_themes.ColorTheme
import org.hse.moodactivities.color_themes.ColorThemeType
import org.hse.moodactivities.services.MoodService
import org.hse.moodactivities.services.ThemesService
import org.hse.moodactivities.services.UserService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {
    companion object {
        const val USER_DAILY_MOOD = 1
        const val USERNAME = "Test User"
        const val BIRTH_DATE = "01-01-2000"
        const val LOGIN = "test_user"
        const val EMAIL = "test@user.ru"
        const val UNPRESSED_BUTTON_ALPHA = 0.5f
        const val PRESSED_BUTTON_ALPHA = 1.0f
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

        mockkObject(UserService)
        every { UserService.uploadUserInfoFromServer(any<AppCompatActivity>()) } returns Unit
        every { UserService.getUsername() } returns USERNAME
        every { UserService.getLogin() } returns LOGIN
        every { UserService.getEmail() } returns EMAIL
        every { UserService.getBirthDate() } returns BIRTH_DATE

        // transit to profile fragment
        onView(withId(R.id.bottom_menu_profile)).perform(click())
    }

    @Test
    fun testDefaultUserInfo() {
        // check username
        onView(withId(R.id.name)).check(matches(withText(USERNAME)))

        // check birth date
        onView(withId(R.id.birth_date)).check(matches(withText(BIRTH_DATE)))

        // check login
        onView(withId(R.id.login)).check(matches(withText(LOGIN)))

        // check email
        onView(withId(R.id.email)).check(matches(withText(EMAIL)))
    }

    @Test
    fun testChangeColorThemeOnButtonPress() {
        onView(withId(R.id.color_theme_forest)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.color_theme_calmness)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.color_theme_energetic)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.color_theme_lemonade)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        assert(ThemesService.getColorTheme().getColorThemeType() == ColorThemeType.FOREST)

        onView(withId(R.id.color_theme_button_energetic)).perform(click())

        onView(withId(R.id.color_theme_forest)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.color_theme_calmness)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.color_theme_energetic)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.color_theme_lemonade)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        assert(ThemesService.getColorTheme().getColorThemeType() == ColorThemeType.ENERGETIC)
    }

    @Test
    fun testChangeLightModeOnButtonPress() {
        onView(withId(R.id.light_mode_background)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.dark_mode_background)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        assert(ThemesService.getLightMode() == ColorTheme.LightMode.DAY)

        onView(withId(R.id.dark_mode_button)).perform(click())

        onView(withId(R.id.light_mode_background)).check(matches(withAlpha(UNPRESSED_BUTTON_ALPHA)))
        onView(withId(R.id.dark_mode_background)).check(matches(withAlpha(PRESSED_BUTTON_ALPHA)))
        assert(ThemesService.getLightMode() == ColorTheme.LightMode.NIGHT)
    }

    @Test
    fun testOpeningChangeNameActivityOnButtonPress() {
        onView(withId(R.id.name_button)).perform(click())

        onView(withId(R.id.change_name_title)).check(matches(isDisplayed()))
    }

    @Test
    fun testOpeningChangeBirthDateActivityOnButtonPress() {
        onView(withId(R.id.birth_date_button)).perform(click())

        onView(withId(R.id.change_birth_date_title)).check(matches(isDisplayed()))
    }

    @Test
    fun testOpeningChangePasswordActivityOnButtonPress() {
        onView(withId(R.id.password_button)).perform(click())

        onView(withId(R.id.change_password_title)).check(matches(isDisplayed()))
    }
}
