package org.hse.moodactivities.color_themes

import android.graphics.Color
import org.hse.moodactivities.services.ThemesService

enum class ColorThemeType {
    CALMNESS, TWILIGHT, FOREST, ENERGY_THEME, CHEERFULNESS_THEME, INSPIRATION_THEME, PRODUCTIVITY_THEME, ROMANTIC_THEME, CONFIDENCE_THEME
}

abstract class ColorTheme(
    private var colorThemeType: ColorThemeType,
    private var lightMode: LightMode,
    private var backgroundColor: Int,
    private var dimmedBackgroundColor: Int,
    private var fontColor: Int,
    private var color1: Int,
    private var dimmedColor1: Int,
    private var color2: Int,
    private var dimmedColor2: Int,
    private var color3: Int,
    private var dimmedColor3: Int,
    private var color4: Int,
    private var dimmedColor4: Int,
    private var color5: Int,
    private var dimmedColor5: Int,
    private var color6: Int,
    private var dimmedColor6: Int,
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

    fun getDimmedBackgroundColor(): Int {
        return dimmedBackgroundColor
    }

    fun getFontColor(): Int {
        return fontColor
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

    fun getDimmedColor5(): Int {
        return dimmedColor5
    }

    fun getColor6(): Int {
        return color6
    }

    fun getDimmedColor6(): Int {
        return dimmedColor6
    }

    abstract fun getButtonColor(): Int

    abstract fun getButtonTextColor(): Int

    // color of charts lines
    abstract fun getChartsColor(): Int

    companion object {
        private val colors: HashMap<String, Int> = hashMapOf(
            "space cadet" to Color.parseColor("#283044"),
            "pastel red" to Color.parseColor("#E66360"),
            "pastel orange" to Color.parseColor("#F0B365"),
            "pastel yellow" to Color.parseColor("#E9EC6B"),
            "bright green" to Color.parseColor("#B3E820"),
            "mint cream" to Color.parseColor("#F7FBF8"),
            "dimmed mint cream" to Color.parseColor("#EDF6EF"),
            "payne's gray" to Color.parseColor("#3C4F60"),
            "green" to Color.parseColor("#00AE58"),
            "khaki" to Color.parseColor("#BFA89E"),
            "dimmed khaki" to Color.parseColor("#B19589"),
            "dark khaki" to Color.parseColor("#846557"),
            "light timberwolf" to Color.parseColor("#F2F1EE"),
            "dimmed light timberwolf" to Color.parseColor("#EAE7E2"),
            "timberwolf" to Color.parseColor("#D5CFC6"),
            "dimmed timberwolf" to Color.parseColor("#C4BBAE"),
            "azure" to Color.parseColor("#CFE0E2"),
            "dimmed azure" to Color.parseColor("#B4CFD2"),
            "columbia blue" to Color.parseColor("#B2CBD5"),
            "dimmed columbia blue" to Color.parseColor("#99BAC7"),
            "powder blue" to Color.parseColor("#95B6C8"),
            "dimmed powder blue" to Color.parseColor("#7EA6BC"),
            "air superiority blue" to Color.parseColor("#6493B1"),
            "dimmed air superiority blue" to Color.parseColor("#294252"),
            "dark air superiority blue" to Color.parseColor("#7693AE"),
            "dimmed dark air superiority blue" to Color.parseColor("#577693"),
            "silver" to Color.parseColor("#CEBEBE"),
            "dimmed silver" to Color.parseColor("#AC9191"),
            "champagne pink" to Color.parseColor("#E1CEC1"),
            "dimmed champagne pink" to Color.parseColor("#C6A189"),
            "pale dogwood" to Color.parseColor("#D5B9B2"),
            "dimmed pale dogwood" to Color.parseColor("#B98B7F"),
            "rosy brown" to Color.parseColor("#D5B9B2"),
            "dimmed rosy brown" to Color.parseColor("#B98B7F"),
            "rosy brown dark" to Color.parseColor("#BC908E"),
            "dimmed rosy brown dark" to Color.parseColor("#A36765"),
            "rosy taupe" to Color.parseColor("#884B58"),
            "dimmed rosy taupe" to Color.parseColor("#5F343D"),
            "black" to Color.parseColor("#000000"),
            "light lavender" to Color.parseColor("#EBEFF8"),
            "dimmed light lavender" to Color.parseColor("#D6DFF1"),
            "wine" to Color.parseColor("#6D2E46"),
            "dark green" to Color.parseColor("#1F2F16"),
            "dark powder blue" to Color.parseColor("#92AFD7"),
            "dimmed dark powder blue" to Color.parseColor("#5D88C4"),
            "vista blue" to Color.parseColor("#84A1C3"),
            "dimmed vista blue" to Color.parseColor("#587FAE"),
            "payne's grey" to Color.parseColor("#5A7684"),
            "dimmed payne's grey" to Color.parseColor("#485E6A"),
            "feldgrau" to Color.parseColor("#4A696A"),
            "dimmed feldgrau" to Color.parseColor("#3B5455"),
            "brunswick green" to Color.parseColor("#5A8E69"),
            "dimmed brunswick green" to Color.parseColor("#233729"),
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
