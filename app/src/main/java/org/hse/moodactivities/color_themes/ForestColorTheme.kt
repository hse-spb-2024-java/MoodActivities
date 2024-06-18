package org.hse.moodactivities.color_themes

class ForestColorTheme : ColorTheme(
    /* colorThemeType */ ColorThemeType.FOREST,
    /* lightMode */ LightMode.DAY,
    /* backgroundColor */ getColorByName("light lavender")!!,
    /* dimmedBackgroundColor */ getColorByName("dimmed light lavender")!!,
    /* fontColor */ getColorByName("forest font color")!!,
    /* color1 */ getColorByName("dark powder blue")!!,
    /* dimmedColor1 */ getColorByName("dimmed dark powder blue")!!,
    /* color2 */ getColorByName("vista blue")!!,
    /* dimmedColor2 */ getColorByName("dimmed vista blue")!!,
    /* color3 */ getColorByName("dark air superiority blue")!!,
    /* dimmedColor3 */getColorByName("dimmed dark air superiority blue")!!,
    /* color4 */getColorByName("payne's grey")!!,
    /* dimmedColor4 */getColorByName("dimmed payne's grey")!!,
    /* color5 */getColorByName("feldgrau")!!,
    /* dimmedColor5 */getColorByName("dimmed feldgrau")!!,
    /* color6 */getColorByName("brunswick green")!!,
    /* dimmedColor6 */getColorByName("dimmed brunswick green")!!,
    /* recordedColor */ getColorByName("napier green")!!,
    /* notRecordedColor */ getColorByName("cornell red")!!,
) {
    override fun getBackgroundColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("light lavender")
        }
        return getColorByName("dark payne's grey")
    }

    override fun getFontColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("forest font color")
        }
        return getColorByName("light payne's grey")
    }

    override fun getDailyActivityWidgetColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("dark powder blue")
        }
        return getColorByName("dark dark powder blue")
    }

    override fun getDailyActivityWidgetIconColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("dimmed dark powder blue")
        }
        return getColorByName("dark dimmed dark powder blue")
    }

    override fun getDailyActivityWidgetTextColor(): Int {
        return getColorByName("forest font color")
    }

    override fun getMoodFlowWidgetColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("vista blue")
        }
        return getColorByName("dark vista blue")
    }

    override fun getDailyQuestionWidgetColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("dark air superiority blue")
        }
        return getColorByName("dark dark air superiority blue")
    }

    override fun getWeekStatisticDayWidgetColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("payne's grey")
        }
        return getColorByName("dimmed payne's grey")
    }

    override fun getChatWidgetColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("feldgrau")
        }
        return getColorByName("dimmed feldgrau")
    }

    override fun getWeekAnalyticsWidgetColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("payne's grey")
        }
        return getColorByName("dimmed payne's grey")
    }

    override fun getWeekAnalyticsWidgetTitleTextColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("forest font color")
        }
        return getColorByName("light payne's grey")
    }

    override fun getDaysInRowColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("vista blue")
        }
        return getColorByName("dark vista blue")
    }

    override fun getMoodFlowChartBackgroundColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("dark powder blue")
        }
        return getColorByName("dark dark powder blue")
    }

    override fun getButtonColor(): Int {
        if (getLightMode() == LightMode.DAY) {
            return getColorByName("payne's grey")
        }
        return getColorByName("dimmed payne's grey")
    }

    companion object {
        fun getColorThemeColor(): Int {
            return getColorByName("dark powder blue")
        }
    }
}
