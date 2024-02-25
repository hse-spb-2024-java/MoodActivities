package org.hse.moodactivities.utils

import org.hse.moodactivities.R
import java.time.DayOfWeek

class Utils {

    companion object {
        public fun getStringByDayOfWeek(dayOfWeek: DayOfWeek): Int {
            return when (dayOfWeek) {
                DayOfWeek.MONDAY -> R.string.monday
                DayOfWeek.TUESDAY -> R.string.tuesday
                DayOfWeek.WEDNESDAY -> R.string.wednesday
                DayOfWeek.THURSDAY -> R.string.thursday
                DayOfWeek.FRIDAY -> R.string.friday
                DayOfWeek.SATURDAY -> R.string.saturday
                DayOfWeek.SUNDAY -> R.string.sunday
            }
        }
    }
}
