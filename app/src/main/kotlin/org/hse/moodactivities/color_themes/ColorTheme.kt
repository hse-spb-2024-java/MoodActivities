package org.hse.moodactivities.color_themes

import android.graphics.Color

enum class ColorThemeType {
    CALMNESS, LEMONADE, FOREST, ENERGETIC
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
    private var recordedColor: Int,
    private var notRecordedColor: Int,
) {
    enum class LightMode {
        DAY, NIGHT
    }

    fun getColorThemeType(): ColorThemeType {
        return colorThemeType
    }

    fun getLightMode(): LightMode {
        return lightMode
    }

    fun setLightMode(newLightMode: LightMode) {
         lightMode = newLightMode
    }

    open fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun getDimmedBackgroundColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getFontColor(): Int {
        return fontColor
    }

    fun getColorThemeColor(): Int {
        return color2
    }

    /* return color for index from 0 to 11
    * else return color1 as fallback */
    fun getColorByIndex(colorIndex: Int): Int {
        return when (colorIndex) {
            0 -> color1
            1 -> dimmedColor1
            2 -> color2
            3 -> dimmedColor2
            4 -> color3
            5 -> dimmedColor3
            6 -> color4
            7 -> dimmedColor4
            8 -> color5
            9 -> dimmedColor5
            10 -> color6
            11 -> dimmedColor6
            else -> color1 // fallback
        }
    }

    open fun getInputBackgroundColor(): Int {
        return color3
    }

    open fun getInputHintTextColor(): Int {
        return dimmedColor3
    }

    open fun getInputTextColor(): Int {
        return getFontColor()
    }

    open fun getButtonColor(): Int {
        return color4
    }

    open fun getButtonTextColor(): Int {
        return dimmedBackgroundColor
    }

    // colors for main screen
    open fun getBottomMenuColor(): Int {
        return dimmedColor6
    }

    open fun getBottomMenuTextColor(): Int {
        return color6
    }

    // colors for home fragment
    // color for daily activity widget
    open fun getDailyActivityWidgetColor(): Int {
        return color1
    }

    open fun getDailyActivityWidgetIconColor(): Int {
        return dimmedColor1
    }

    open fun getDailyActivityWidgetTextColor(): Int {
        return fontColor
    }

    open fun getDailyActivityWidgetIconTextColor(): Int {
        return dimmedBackgroundColor
    }

    // colors for mood flow widget
    open fun getMoodFlowWidgetColor(): Int {
        return color2
    }

    open fun getMoodFlowWidgetIconColor(): Int {
        return dimmedColor2
    }

    open fun getMoodFlowWidgetTextColor(): Int {
        return fontColor
    }

    open fun getMoodFlowWidgetIconTextColor(): Int {
        return dimmedBackgroundColor
    }

    // color for daily question widget
    open fun getDailyQuestionWidgetColor(): Int {
        return color3
    }

    open fun getDailyQuestionWidgetIconColor(): Int {
        return dimmedColor3
    }

    open fun getDailyQuestionWidgetTextColor(): Int {
        return fontColor
    }

    open fun getDailyQuestionWidgetIconTextColor(): Int {
        return dimmedBackgroundColor
    }

    // color for week statistic day widget
    open fun getWeekStatisticDayWidgetColor(): Int {
        return color4
    }

    open fun getWeekStatisticDayWidgetTextColor(): Int {
        return dimmedBackgroundColor
    }

    // color for chat widget
    open fun getChatWidgetColor(): Int {
        return color5
    }

    open fun getChatWidgetColorTextColor(): Int {
        return dimmedBackgroundColor
    }

    // colors for activity widget

    open fun getWeekAnalyticsWidgetColor(): Int {
        return color4
    }

    open fun getWeekAnalyticsWidgetTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getWeekAnalyticsWidgetTitleTextColor(): Int {
        return fontColor
    }

    // colors for calendar fragment
    open fun getCalendarWidgetColor(): Int {
        return dimmedColor3
    }

    open fun getCalendarWidgetTextColor(): Int {
        return backgroundColor
    }

    open fun getCalendarDayOfWeekWidgetTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getCalendarWidgetCurrentDayTextColor(): Int {
        return Color.parseColor("#138808")
    }

    // color for week statistic day widget
    open fun getCalendarMonthStatisticDayWidgetColor(): Int {
        return dimmedColor4
    }

    open fun getCalendarMonthStatisticDayWidgetTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getMoodIndicatorColorByScore(score: Int): Int {
        return when (score) {
            1 -> colors["pastel red"]!!
            2 -> colors["pastel orange"]!!
            3 -> colors["pastel yellow"]!!
            4 -> colors["bright green"]!!
            5 -> colors["green"]!!
            else -> getCalendarWidgetColor()
        }
    }

    // colors for insights fragment
    // colors for days in rows
    open fun getDaysInRowColor(): Int {
        return color1
    }

    open fun getDaysInRowTextColor(): Int {
        return fontColor
    }

    // colors for mood flow chart
    open fun getMoodFlowChartBackgroundColor(): Int {
        return color2
    }

    open fun getMoodFlowChartColor(): Int {
        return dimmedColor2
    }

    open fun getMoodFlowChartTextColor(): Int {
        return fontColor
    }

    open fun getMoodFlowChartLabelColor(): Int {
        return dimmedColor2
    }

    open fun getMoodFlowChartLabelTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getRecordedDayColor(): Int {
        return recordedColor
    }

    open fun getNotRecordedDayColor(): Int {
        return notRecordedColor
    }

    // colors for chat fragment
    open fun getMessageInputColor(): Int {
        return color3
    }

    open fun getMessageInputTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getMessageInputHintTextColor(): Int {
        return dimmedColor3
    }

    open fun getUserMessageColor(): Int {
        return color2
    }

    open fun getUserMessageTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getChatMessageColor(): Int {
        return color1
    }

    open fun getChatMessageTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getSendButtonImageColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getTimePeriodBarColor(): Int {
        return color5
    }

    open fun getTimePeriodBarButtonColor(): Int {
        return dimmedColor5
    }

    open fun getTermsAndConditionsFontColor(): Int {
        return color5
    }

    open fun getStatisticItemColor(): Int {
        return color6
    }

    open fun getStatisticItemTextColor(): Int {
        return dimmedBackgroundColor
    }

    // color for choose * chart
    open fun getFrequentlyUsedColor(): Int {
        return dimmedColor3
    }

    open fun getFrequentlyUsedItemColor(): Int {
        return color3
    }

    open fun getFrequentlyUsedItemNameColor(): Int {
        return backgroundColor
    }

    open fun getFrequentlyUsedItemCounterColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getFrequentlyUsedTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getFrequentlyUsedLabelColor(): Int {
        return getColorByName("dark air superiority blue-2")!!
    }

    open fun getFrequentlyUsedLabelTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getTimePeriodDialogCardColor(): Int {
        return color1
    }

    // color for weather chart
    open fun getWeatherChartBackgroundColor(): Int {
        return color4
    }

    open fun getWeatherChartTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getWeatherChartColor(): Int {
        return dimmedColor4
    }

    open fun getWeatherChartLabelTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getWeatherChartLabelColor(): Int {
        return dimmedColor5
    }

    // color for step charts

    open fun getStepsChartTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getStepsChartColor(): Int {
        return dimmedColor3
    }

    open fun getStepsLabelTextColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getStepsLabelColor(): Int {
        return getColorByName("dark air superiority blue-2")!!
    }

    open fun getSettingsWidgetTitleColor(): Int {
        return dimmedBackgroundColor
    }

    open fun getSettingsWidgetColor(): Int {
        return dimmedColor4
    }

    open fun getSettingsWidgetFieldColor(): Int {
        return color4
    }

    companion object {
        private val colors: HashMap<String, Int> = hashMapOf(
            "cornell red" to Color.parseColor("#c90016"),
            "napier green" to Color.parseColor("#0bda51"),
            "forest font color" to Color.parseColor("#2B3B49"),
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
            "dimmed azure" to Color.parseColor("#8CB4B9"),
            "columbia blue" to Color.parseColor("#B2CBD5"),
            "dimmed columbia blue" to Color.parseColor("#73A0B2"),
            "powder blue" to Color.parseColor("#95B6C8"),
            "dimmed powder blue" to Color.parseColor("#7EA6BC"),
            "air superiority blue" to Color.parseColor("#6493B1"),
            "dimmed air superiority blue" to Color.parseColor("#294252"),
            "dark air superiority blue" to Color.parseColor("#7693AE"),
            "dark dark air superiority blue" to Color.parseColor("#5C7D9C"),
            "dark air superiority blue-2" to Color.parseColor("#577693"),
            "dimmed dark air superiority blue" to Color.parseColor("#41586E"),
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
            "dark dimmed dark powder blue" to Color.parseColor("#2B4B76"),
            "dark dark powder blue" to Color.parseColor("#4475B9"),
            "vista blue" to Color.parseColor("#7999BE"),
            "dark vista blue" to Color.parseColor("#587FAE"),
            "dimmed vista blue" to Color.parseColor("#364F6E"),
            "light payne's grey" to Color.parseColor("#DDE4E8"),
            "payne's grey" to Color.parseColor("#5A7684"),
            "dimmed payne's grey" to Color.parseColor("#485E6A"),
            "dark payne's grey" to Color.parseColor("#34444d"),
            "feldgrau" to Color.parseColor("#4A696A"),
            "dimmed feldgrau" to Color.parseColor("#3B5455"),
            "brunswick green" to Color.parseColor("#5A8E69"),
            "dimmed brunswick green" to Color.parseColor("#233729"),
            "dark midnight blue" to Color.parseColor("#003366"),
            "mindaro" to Color.parseColor("#D2E090"),
            "fulvous" to Color.parseColor("#E8871E"),
            "hunyadi yellow" to Color.parseColor("#F1C379"),
            "honeydew" to Color.parseColor("#F1F7E5"),
            "ecru" to Color.parseColor("#E1C481"),
            "fulvous" to Color.parseColor("#BE6C13"),
        )

        private const val DEFAULT_COLOR = Color.WHITE

        fun getColorByName(name: String): Int {
            return colors.getOrDefault(name, DEFAULT_COLOR)
        }
    }
}
