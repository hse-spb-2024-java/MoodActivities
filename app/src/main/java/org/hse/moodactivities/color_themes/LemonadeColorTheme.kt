package org.hse.moodactivities.color_themes

class LemonadeColorTheme : ColorTheme(
    ColorThemeType.LEMONADE,
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
    getColorByName("napier green")!!,
    getColorByName("cornell red")!!,
) {
    companion object {
        fun getColorThemeColor(): Int {
            return getColorByName("mindaro")
        }
    }
}
