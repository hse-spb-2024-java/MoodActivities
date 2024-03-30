package org.hse.moodactivities.utils

import org.hse.moodactivities.R
import org.hse.moodactivities.services.ChartsService

const val BUTTON_DISABLED_ALPHA = 0.6f
const val BUTTON_ENABLED_ALPHA = 1.0f

class UiUtils {
    companion object {
        object Strings {
            var RETURN_TO_INSIGHTS = "< Insights"
        }

        fun getMoodImageIdByIndex(index: Int): Int {
            return when (index) {
                0 -> R.id.mood_1_image
                1 -> R.id.mood_2_image
                2 -> R.id.mood_3_image
                3 -> R.id.mood_4_image
                4 -> R.id.mood_5_image
                else -> -1 // unreachable
            }
        }

        fun getMoodImageResourcesIdByIndex(index: Int): Int {
            return when (index) {
                0 -> R.drawable.mood_flow_1
                1 -> R.drawable.mood_flow_2
                2 -> R.drawable.mood_flow_3
                3 -> R.drawable.mood_flow_4
                4 -> R.drawable.mood_flow_5
                else -> -1 // unreachable
            }
        }

        fun getMoodButtonIdByIndex(index: Int): Int {
            return when (index) {
                0 -> R.id.mood_1_button
                1 -> R.id.mood_2_button
                2 -> R.id.mood_3_button
                3 -> R.id.mood_4_button
                4 -> R.id.mood_5_button
                else -> -1 // unreachable
            }
        }

        fun getStatisticTitle(): String {
            return buildString {
                append(ChartsService.getStatisticMode().toString())
                    .append(" statistic")
            }
        }
    }
}
