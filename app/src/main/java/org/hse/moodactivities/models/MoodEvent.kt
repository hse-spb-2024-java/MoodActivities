package org.hse.moodactivities.models

import org.hse.moodactivities.interfaces.Data
import kotlin.collections.HashSet

class MoodEvent : Data {
    private var moodRating : Int? = null
    private var activitiesChosen : HashSet<String> = HashSet()
    private var emotionsChosen : HashSet<String> = HashSet()
    private var answerForQuestion : String? = null

    fun setMoodRating(moodRating : Int) {
        this.moodRating = moodRating
    }

    fun getMoodRating() : Int? {
        return this.moodRating
    }

    fun setActivitiesChosen(activitiesChosen : HashSet<String>) {
        this.activitiesChosen = activitiesChosen
    }

    fun getActivitiesChosen() : HashSet<String> {
        return this.activitiesChosen
    }

    fun setEmotionsChosen(activitiesChosen : HashSet<String>) {
        this.activitiesChosen = activitiesChosen
    }

    fun getEmotionsChosen() : HashSet<String> {
        return this.activitiesChosen
    }

    fun setAnswerForQuestion(answerForQuestion : String) {
        this.answerForQuestion = answerForQuestion
    }

    fun getAnswerForQuestion() : String? {
        return this.answerForQuestion
    }
}
