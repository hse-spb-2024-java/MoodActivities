package org.hse.moodactivities.services

import android.graphics.Color
import org.hse.moodactivities.color_themes.CalmnessColorTheme
import org.hse.moodactivities.color_themes.ColorTheme
import org.hse.moodactivities.color_themes.ColorThemeType
import org.hse.moodactivities.color_themes.ForestColorTheme
import org.hse.moodactivities.color_themes.TwilightColorTheme

class ThemesService {
    companion object {
        private var colorThemes: HashMap<ColorThemeType, ColorTheme> =
            hashMapOf(
                ColorThemeType.CALMNESS to CalmnessColorTheme(),
                ColorThemeType.TWILIGHT to TwilightColorTheme(),
                ColorThemeType.FOREST to ForestColorTheme(),
            )

        private val baseColorThemeType = ColorThemeType.FOREST
        private val baseColorTheme = colorThemes[ColorThemeType.FOREST]!!

        private var lightMode = ColorTheme.LightMode.DAY
        private var colorThemeType = baseColorThemeType
        private var colorTheme: ColorTheme = baseColorTheme

        fun changeColorTheme(newColorThemeType: ColorThemeType) {
            colorThemeType = newColorThemeType
            colorTheme = colorThemes[colorThemeType]!!
        }

        fun changeLightMode(newLightMode: ColorTheme.LightMode) {
            lightMode = newLightMode
        }

        fun getColorTheme(): ColorTheme {
            return colorTheme
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

        fun getFontColor(): Int {
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

        fun getColor6(): Int {
            return colorTheme.getColor6()
        }

        fun getDimmedColor6(): Int {
            return colorTheme.getDimmedColor6()
        }

        fun getHighlightedFontColor(): Int {
            return colorTheme.getHighlightedFontColor()
        }

        fun getInputBackgroundColor(): Int {
            return colorTheme.getInputBackgroundColor()
        }

        fun getInputHintTextColor(): Int {
            return colorTheme.getInputHintTextColor()
        }

        fun getInputTextColor(): Int {
            return colorTheme.getInputTextColor()
        }

        fun getButtonColor(): Int {
            return colorTheme.getButtonColor()
        }

        fun getButtonTextColor(): Int {
            return colorTheme.getButtonTextColor()
        }

        fun getRecordedDayColor(): Int {
            return Color.parseColor("#32CD32")
        }

        fun getNotRecordedDayColor(): Int {
            return Color.parseColor("#D53032")
        }

        fun getChartsColor(): Int {
            return colorTheme.getChartsColor()
        }

        fun getMoodFlowWidgetColor(): Int {
            return colorTheme.getMoodFlowCardColor()
        }
    }
}