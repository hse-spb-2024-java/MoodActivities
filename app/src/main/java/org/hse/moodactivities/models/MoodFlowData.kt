package org.hse.moodactivities.models

import org.hse.moodactivities.interfaces.Data
import kotlin.collections.HashSet

class MoodFlowData : Data {
    private var moodRating : Int? = null
    private var activitiesChosen : HashSet<String> = HashSet()

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
}
