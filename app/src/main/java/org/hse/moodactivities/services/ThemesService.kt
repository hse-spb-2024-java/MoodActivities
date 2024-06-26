package org.hse.moodactivities.services

import org.hse.moodactivities.color_themes.CalmnessColorTheme
import org.hse.moodactivities.color_themes.ColorTheme
import org.hse.moodactivities.color_themes.ColorThemeType
import org.hse.moodactivities.color_themes.EnergeticColorTheme
import org.hse.moodactivities.color_themes.ForestColorTheme
import org.hse.moodactivities.color_themes.LemonadeColorTheme

class ThemesService {
    companion object {
        private var colorThemes: HashMap<ColorThemeType, ColorTheme> =
            hashMapOf(
                ColorThemeType.CALMNESS to CalmnessColorTheme(),
                ColorThemeType.LEMONADE to LemonadeColorTheme(),
                ColorThemeType.FOREST to ForestColorTheme(),
                ColorThemeType.ENERGETIC to EnergeticColorTheme(),
            )

        private val baseColorThemeType = ColorThemeType.FOREST
        private val baseColorTheme = colorThemes[ColorThemeType.FOREST]!!

        private var colorThemeType = baseColorThemeType
        private var colorTheme: ColorTheme = baseColorTheme

        fun changeColorTheme(newColorThemeType: ColorThemeType) {
            colorThemeType = newColorThemeType
            colorTheme = colorThemes[colorThemeType]!!
        }

        fun getLightMode(): ColorTheme.LightMode {
            return colorTheme.getLightMode()
        }

        fun setLightMode(newLightMode: ColorTheme.LightMode) {
            colorTheme.setLightMode(newLightMode)
        }

        fun getColorTheme(): ColorTheme {
            return colorTheme
        }
    }
}
