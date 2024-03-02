package org.hse.moodactivities.models

import org.hse.moodactivities.interfaces.Data
import kotlin.collections.HashSet

class MoodEvent : Data {
    private var moodRating: Int? = null
    private var chosenActivities: HashSet<String>? = null
    private var chosenEmotions: HashSet<String>? = null
    private var userAnswer: String? = null

    fun setMoodRate(moodRate: Int) {
        this.moodRating = moodRate
    }

    fun getMoodRate(): Int? {
        return this.moodRating
    }

    fun setChosenActivities(chosenActivities: HashSet<String>) {
        this.chosenActivities = chosenActivities
    }

    fun getChosenActivities(): HashSet<String>? {
        return this.chosenActivities
    }

    fun setChosenEmotions(chosenEmotions: HashSet<String>) {
        this.chosenEmotions = chosenEmotions
    }

    fun getChosenEmotions(): HashSet<String>? {
        return this.chosenEmotions
    }

    fun setUserAnswer(userAnswer: String) {
        this.userAnswer = userAnswer
    }

    fun getUserAnswer(): String? {
        return this.userAnswer
    }
}
