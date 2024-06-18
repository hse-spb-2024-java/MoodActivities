package org.hse.moodactivities.color_themes

class EnergeticColorTheme : ColorTheme(
    ColorThemeType.ENERGETIC,
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
    companion object {
        fun getColorThemeColor(): Int {
            return getColorByName("fulvous")
        }
    }
}
