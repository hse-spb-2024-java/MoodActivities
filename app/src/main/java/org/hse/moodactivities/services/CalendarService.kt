package org.hse.moodactivities.services

class CalendarService {
    companion object {
        private lateinit var chosenDate: String

        fun setDate(date: String) {
            chosenDate = date
        }

        fun getDate(): String {
            return chosenDate
        }
    }
}
