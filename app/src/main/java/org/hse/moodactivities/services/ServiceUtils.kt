package org.hse.moodactivities.services

import org.hse.moodactivities.common.proto.requests.defaults.PeriodType

class ServiceUtils {
    companion object {
        fun toPeriodType(timePeriod: TimePeriod.Value): PeriodType {
            return when (timePeriod) {
                TimePeriod.Value.WEEK -> PeriodType.WEEK
                TimePeriod.Value.MONTH -> PeriodType.MONTH
                TimePeriod.Value.YEAR -> PeriodType.YEAR
                TimePeriod.Value.ALL_TIME -> PeriodType.ALL
            }
        }
    }
}
