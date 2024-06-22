package org.hse.moodactivities.models

class DailyActivityItemModel(
    private var dailyActivity: String,
    private var userImpressions: String,
    time: String,
) : DailyItemModel(DailyItemType.DAILY_ACTIVITY, time) {
    fun getDailyActivity(): String {
        return dailyActivity
    }

    fun getUserImpressions(): String {
        return userImpressions
    }
}
