package org.hse.moodactivities.color_themes

import android.graphics.Color
import org.hse.moodactivities.services.ThemesService

enum class ColorThemeType {
    ENERGY_THEME, CALMNESS_THEME, CHEERFULNESS_THEME, INSPIRATION_THEME, PRODUCTIVITY_THEME,
    ROMANTIC_THEME, CONFIDENCE_THEME
}

open class ColorTheme(
    private var colorThemeType: ColorThemeType,
    private var lightMode: LightMode,
    private var backgroundColor: Int,
    private var color1: Int,
    private var dimmedColor1: Int,
    private var color2: Int,
    private var dimmedColor2: Int,
    private var color3: Int,
    private var dimmedColor3: Int,
    private var color4: Int,
    private var dimmedColor4: Int,
    private var color5: Int,
) {
    enum class LightMode {
        DAY, NIGHT
    }

    fun getColorThemeType(): ColorThemeType {
        return colorThemeType
    }

    fun getMode(): LightMode {
        return lightMode
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun getColor1(): Int {
        return color1
    }

    fun getDimmedColor1(): Int {
        return dimmedColor1
    }

    fun getColor2(): Int {
        return color2
    }

    fun getDimmedColor2(): Int {
        return dimmedColor2
    }

    fun getColor3(): Int {
        return color3
    }

    fun getDimmedColor3(): Int {
        return dimmedColor3
    }

    fun getColor4(): Int {
        return color4
    }

    fun getDimmedColor4(): Int {
        return dimmedColor4
    }

    fun getColor5(): Int {
        return color5
    }

    companion object {
        private val colors: HashMap<String, Int> =
            hashMapOf(
                "pastel red" to Color.parseColor("#E66360"),
                "pastel orange" to Color.parseColor("#F0B365"),
                "pastel yellow" to Color.parseColor("#E9EC6B"),
                "bright green" to Color.parseColor("#B3E820"),
                "green" to Color.parseColor("#00AE58"),
                "mint cream" to Color.parseColor("#F7FBF8"),
                "khaki" to Color.parseColor("#BFA89E"),
                "dimmed khaki" to Color.parseColor("#B19589"),
                "timberwolf" to Color.parseColor("#D5CFC6"),
                "dimmed timberwolf" to Color.parseColor("#C4BBAE"),
                "azure" to Color.parseColor("#CFE0E2"),
                "dimmed azure" to Color.parseColor("#B4CFD2"),
                "columbia blue" to Color.parseColor("#B2CBD5"),
                "dimmed columbia blue" to Color.parseColor("#99BAC7"),
                "powder blue" to Color.parseColor("#95B6C8"),
            )

        fun getColorByName(name: String): Int? {
            return colors[name]
        }

        fun getMoodIndicatorColorByScore(score: Int): Int {
            return when (score) {
                1 -> colors["pastel red"]!!
                2 -> colors["pastel orange"]!!
                3 -> colors["pastel yellow"]!!
                4 -> colors["bright green"]!!
                5 -> colors["green"]!!
                else -> ThemesService.getColor3()
            }

        }
    }
}