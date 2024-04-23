package org.hse.moodactivities.models

class DailyQuestionModel(
    private var dailyQuestion: String,
    private var answerToDailyQuestion: String,
    private var time: String,
) : DailyItemModel(DailyItemType.DAILY_QUESTION) {
    fun getDailyQuestion(): String {
        return dailyQuestion
    }

    fun getAnswerToDailyQuestion(): String {
        return answerToDailyQuestion
    }

    fun getTime(): String {
        return time
    }
}
