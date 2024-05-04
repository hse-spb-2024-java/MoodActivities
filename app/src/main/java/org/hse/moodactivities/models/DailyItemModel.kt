package org.hse.moodactivities.models

enum class DailyItemType {
    DAILY_INFO, DAILY_QUESTION, DAILY_ACTIVITY, DAILY_EMPTY
}
abstract class DailyItemModel (var dailyItemType: DailyItemType, private var time: String) {
    fun getTime(): String {
        return time
    }
}
