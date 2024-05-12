package org.hse.moodactivities.models

enum class DailyItemType {
    DAILY_INFO, DAILY_QUESTION, DAILY_ACTIVITY, DAILY_EMPTY
}
abstract class DailyItemModel (private var dailyItemType: DailyItemType, private var time: String) {
    fun getTime(): String {
        return time
    }

    fun getDailyItemType(): DailyItemType {
        return dailyItemType
    }

    companion object {
        const val DEFAULT_TIME = "00:00.0000"
    }
}
