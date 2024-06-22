package org.hse.moodactivities.responses

import org.hse.moodactivities.common.proto.responses.stats.MoodForTheMonthResponse
import java.time.LocalDate

class MonthStatisticResponse {
    private var moodRates: HashMap<Int, Int> = HashMap()

    fun init(response: MoodForTheMonthResponse) {
        val recordedDays = response.recordedDaysList
        for (dayRecord in recordedDays) {
            val day = LocalDate.parse(dayRecord.date).dayOfMonth
            val moodRate = dayRecord.score.toInt()
            moodRates[day] = moodRate
        }
    }

    // for tests
    fun setMoodRates(moodRates: HashMap<Int, Int>) {
        this.moodRates = moodRates
    }

    fun getMoodRates() : HashMap<Int, Int> {
        return moodRates
    }
}
