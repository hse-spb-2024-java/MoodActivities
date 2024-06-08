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
    /* recordedColor */ getColorByName("cornell red")!!,
    /* notRecordedColor */ getColorByName("napier green")!!,
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
}
