package org.hse.moodactivities.color_themes

import android.graphics.Color

enum class ColorThemeType {
    ENERGY_THEME, CALMNESS_THEME, CHEERFULNESS_THEME, INSPIRATION_THEME, PRODUCTIVITY_THEME,
    ROMANTIC_THEME, CONFIDENCE_THEME
}

open class ColorTheme(
    private var colorThemeType: ColorThemeType,
    private var mode: Mode,
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
    enum class Mode {
        DAY, NIGHT
    }

    fun getColorThemeType(): ColorThemeType {
        return colorThemeType
    }

    fun getMode(): Mode {
        return mode
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
    }
}