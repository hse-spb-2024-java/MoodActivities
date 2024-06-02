package org.hse.moodactivities.color_themes

class CalmnessColorTheme : ColorTheme(
    ColorThemeType.CALMNESS,
    LightMode.DAY,
    getColorByName("mint cream")!!, // all colors exist
    getColorByName("dimmed mint cream")!!,
    getColorByName("space cadet")!!,
    getColorByName("khaki")!!,
    getColorByName("dimmed khaki")!!,
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
    override fun getChartsColor(): Int {
        return getColor4()
    }
}
