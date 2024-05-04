package org.hse.moodactivities.models

class DailyQuestionItemModel(
    private var dailyQuestion: String,
    private var answerToDailyQuestion: String,
    time: String,
) : DailyItemModel(DailyItemType.DAILY_QUESTION, time) {
    fun getDailyQuestion(): String {
        return dailyQuestion
    }

    fun getAnswerToDailyQuestion(): String {
        return answerToDailyQuestion
    }
}
