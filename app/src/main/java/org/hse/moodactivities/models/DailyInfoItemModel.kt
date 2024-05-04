package org.hse.moodactivities.models

class DailyInfoItemModel(
    private var shortDescription: String,
    private var moodRating: Int,
    private var question: String,
    private var answerToQuestion: String,
    private var activities: ArrayList<MoodActivity>,
    private var emotions: ArrayList<MoodEmotion>,
    time: String
) : DailyItemModel(DailyItemType.DAILY_INFO, time) {
    fun getShortDescription(): String {
        return shortDescription
    }

    fun getMoodRating(): Int {
        return moodRating
    }

    fun getQuestion(): String {
        return question
    }

    fun getAnswerToQuestion(): String {
        return answerToQuestion
    }

    fun getActivities(): ArrayList<MoodActivity> {
        return activities
    }

    fun getEmotions(): ArrayList<MoodEmotion> {
        return emotions
    }
}
