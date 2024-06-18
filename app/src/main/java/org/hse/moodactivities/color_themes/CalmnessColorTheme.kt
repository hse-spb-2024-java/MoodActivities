package org.hse.moodactivities.color_themes

class CalmnessColorTheme : ColorTheme(
    /* colorThemeType */ ColorThemeType.CALMNESS,
    /* lightMode */ LightMode.DAY,
    /* backgroundColor */ getColorByName("mint cream")!!,
    /* dimmedBackgroundColor */ getColorByName("dimmed mint cream")!!,
    /* fontColor */ getColorByName("space cadet")!!,
    /* color1 */ getColorByName("khaki")!!,
    /* dimmedColor1 */ getColorByName("dimmed khaki")!!,
    /* color2 */ getColorByName("timberwolf")!!,
    /* dimmedColor2 */ getColorByName("dimmed timberwolf")!!,
    /* color3 */ getColorByName("azure")!!,
    /* dimmedColor3 */ getColorByName("dimmed azure")!!,
    /* color4 */ getColorByName("columbia blue")!!,
    /* dimmedColor4 */ getColorByName("dimmed columbia blue")!!,
    /* color5 */ getColorByName("powder blue")!!,
    /* dimmedColor5 */ getColorByName("dimmed powder blue")!!,
    /* color6 */ getColorByName("air superiority blue")!!,
    /* dimmedColor6 */ getColorByName("dimmed air superiority blue")!!,
    /* recordedColor */ getColorByName("napier green")!!,
    /* notRecordedColor */ getColorByName("cornell red")!!,
) {
    companion object {
        fun getColorThemeColor(): Int {
            return getColorByName("columbia blue")
        }
    }
}
