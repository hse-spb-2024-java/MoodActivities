package org.hse.moodactivities.models

class StatisticItem(
    private var name: String,
    private var counter: Int,
    private var iconId: Int
) {
    fun getName(): String {
        return name
    }

    fun getCounter() : Int {
        return counter
    }

    fun getIconId(): Int {
        return iconId
    }
}
