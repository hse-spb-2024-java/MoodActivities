package org.hse.moodactivities.services

import org.hse.moodactivities.models.MoodEvent

class MoodService {

    companion object {
        fun sendMoodEvent(moodEvent : MoodEvent) {

        }

        fun getDayRate(moodData : MoodEvent) : String {
            return "It's okay"
        }

        // GPT describes user's day in one word
        fun getGptShortResponse() : String {
            val answer = "amazing"
            return "Your day was " + answer
        }

        // GPT describes user's day
        fun getGptLongResponse() : String {
            return "It was quite interesting day"
        }
    }
}
