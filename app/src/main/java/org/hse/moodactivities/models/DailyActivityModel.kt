package org.hse.moodactivities.models

class DailyActivityModel(
    private var dailyActivity: String,
    private var userImpressions: String,
    private var time: String,
) : DailyItemModel(DailyItemType.DAILY_ACTIVITY) {
    fun getDailyActivity(): String {
        return dailyActivity
    }

    fun getUserImpressions(): String {
        return userImpressions
    }

    fun getTime(): String {
        return time
    }
}
