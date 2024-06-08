package org.hse.moodactivities.color_themes

class TwilightColorTheme : ColorTheme(
    ColorThemeType.TWILIGHT,
    LightMode.DAY,
    getColorByName("light timberwolf")!!,
    getColorByName("dimmed light timberwolf")!!,
    getColorByName("wine")!!,
    getColorByName("silver")!!,
    getColorByName("dimmed silver")!!,
    getColorByName("champagne pink")!!,
    getColorByName("dimmed champagne pink")!!,
    getColorByName("pale dogwood")!!,
    getColorByName("dimmed pale dogwood")!!,
    getColorByName("rosy brown")!!,
    getColorByName("dimmed rosy brown")!!,
    getColorByName("rosy brown dark")!!,
    getColorByName("dimmed rosy brown dark")!!,
    getColorByName("rosy taupe")!!,
    getColorByName("dimmed rosy taupe")!!,
    getColorByName("cornell red")!!,
    getColorByName("napier green")!!,
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
