package org.hse.moodactivities.services

import org.hse.moodactivities.color_themes.CalmnessColorTheme
import org.hse.moodactivities.color_themes.ColorTheme
import org.hse.moodactivities.color_themes.ColorThemeType

class ThemesService {
    companion object {
        private var colorThemes: HashMap<ColorThemeType, ColorTheme> =
            hashMapOf(ColorThemeType.CALMNESS_THEME to CalmnessColorTheme())

        private var lightMode = ColorTheme.LightMode.DAY
        private var colorThemeType = ColorThemeType.CALMNESS_THEME
        private var colorTheme: ColorTheme = colorThemes[ColorThemeType.CALMNESS_THEME]!!

        fun changeColorTheme(newColorThemeType: ColorThemeType) {
            colorThemeType = newColorThemeType
            colorTheme = colorThemes[colorThemeType]!!
        }

        fun changeLightMode(newLightMode: ColorTheme.LightMode) {
            lightMode = newLightMode
        }

        fun getMoodIndicatorColorByScore(score: Int): Int {
            return ColorTheme.getMoodIndicatorColorByScore(score)
        }

        fun getBackgroundColor(): Int {
            return colorTheme.getBackgroundColor()
        }

        fun getDimmedBackgroundColor(): Int {
            return colorTheme.getDimmedBackgroundColor()
        }

        fun getButtonColor(): Int {
            return colorTheme.getButtonColor()
        }

        fun getFontColor() : Int {
            return colorTheme.getFontColor()
        }

        fun getColor1(): Int {
            return colorTheme.getColor1()
        }

        fun getDimmedColor1(): Int {
            return colorTheme.getDimmedColor1()
        }

        fun getColor2(): Int {
            return colorTheme.getColor2()
        }

        fun getDimmedColor2(): Int {
            return colorTheme.getDimmedColor2()
        }

        fun getColor3(): Int {
            return colorTheme.getColor3()
        }

        fun getDimmedColor3(): Int {
            return colorTheme.getDimmedColor3()
        }

        fun getColor4(): Int {
            return colorTheme.getColor4()
        }

        fun getDimmedColor4(): Int {
            return colorTheme.getDimmedColor4()
        }

        fun getColor5(): Int {
            return colorTheme.getColor5()
        }

        fun getDimmedColor5(): Int {
            return colorTheme.getDimmedColor5()
        }
    }
}