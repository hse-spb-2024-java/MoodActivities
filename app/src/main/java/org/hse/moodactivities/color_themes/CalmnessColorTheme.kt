package org.hse.moodactivities.color_themes

class CalmnessColorTheme : ColorTheme(
    /* colorThemeType */ ColorThemeType.CALMNESS,
    /* lightMode */ LightMode.DAY,
    /* backgroundColor */ getColorByName("mint cream")!!,
    /* dimmedBackgroundColor */ getColorByName("dimmed mint cream")!!,
    /* fontColor */ getColorByName("space cadet")!!,
    /* color1 */ getColorByName("khaki")!!,
    /* dimmedColor1 */ getColorByName("dimmed khaki")!!,
    getColorByName("timberwolf")!!,
    getColorByName("dimmed timberwolf")!!,

    getColorByName("azure")!!,
    getColorByName("dimmed azure")!!,

    getColorByName("columbia blue")!!,
    getColorByName("dimmed columbia blue")!!,
    getColorByName("powder blue")!!,
    getColorByName("dimmed powder blue")!!,
    getColorByName("air superiority blue")!!,
    getColorByName("dimmed air superiority blue")!!,
) {
    override fun getButtonColor(): Int {
        return getColor4()
    }

    override fun getButtonTextColor(): Int {
        return getDimmedBackgroundColor()
    }

    override fun getChartsColor(): Int {
        return getColor4()
    }

    override fun getMoodFlowCardColor(): Int {
        return getColor3()
    }
}
