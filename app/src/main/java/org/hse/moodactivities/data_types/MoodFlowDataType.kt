package org.hse.moodactivities.data_types

import org.hse.moodactivities.interfaces.DataType

enum class MoodFlowType {
    DAY_RATING, ACTIVITIES_CHOOSING, EMOTIONS_CHOOSING
}

class MoodFlowDataType(private var dataType: MoodFlowType) : DataType {
    fun getDataType() : MoodFlowType {
        return this.dataType
    }
}