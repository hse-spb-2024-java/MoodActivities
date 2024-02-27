package org.hse.moodactivities.models

class ActivityItem(
    private var iconIndex: Int,
    private var text: String,
    private var iconColor: Int
) {
    fun getText(): String {
        return text
    }

    fun getIconColor(): Int {
        return iconColor
    }

    fun getIconIndex(): Int {
        return iconIndex
    }
}