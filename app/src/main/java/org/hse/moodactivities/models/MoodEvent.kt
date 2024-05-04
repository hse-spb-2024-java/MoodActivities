package org.hse.moodactivities.models

import org.hse.moodactivities.interfaces.Data

class MoodEvent : Data {
    private var moodRating: Int? = null
    private var chosenActivities: HashSet<String>? = null
    private var chosenEmotions: HashSet<String>? = null
    private var question: String? = null
    private var userAnswer: String? = null
    private var time: String? = null
    private var description: String? = null

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

    fun setQuestion(question: String){
        this.question = question
    }

    fun getQuestion(): String? {
        return this.question
    }

    fun setTime(time: String) {
        this.time = time
    }

    fun getTime(): String? {
        return this.time
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getDescription(): String? {
        return this.description
    }
}
