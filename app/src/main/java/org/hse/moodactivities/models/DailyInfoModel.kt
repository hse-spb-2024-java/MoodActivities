package org.hse.moodactivities.models

class DailyInfoModel(
    private var shortDescription: String,
    private var time: String,
    private var moodRating: Int,
    private var question: String,
    private var answerToQuestion: String,
    private var activities: ArrayList<MoodActivity>,
    private var emotions: ArrayList<MoodEmotion>,
) : DailyItemModel(DailyItemType.DAILY_INFO) {
    fun getShortDescription(): String {
        return shortDescription
    }

    fun getTime(): String {
        return time
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
