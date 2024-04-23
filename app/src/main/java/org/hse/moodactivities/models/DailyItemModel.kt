package org.hse.moodactivities.models

enum class DailyItemType {
    DAILY_INFO, DAILY_QUESTION, DAILY_ACTIVITY
}
abstract class DailyItemModel (var dailyItemType: DailyItemType)
