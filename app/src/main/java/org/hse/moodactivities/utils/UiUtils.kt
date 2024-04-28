package org.hse.moodactivities.utils

import android.graphics.Color
import org.hse.moodactivities.R

const val BUTTON_DISABLED_ALPHA = 0.5f
const val BUTTON_ENABLED_ALPHA = 1.0f

class UiUtils {
    companion object {
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
                else -> R.drawable.widget_mood_icon // default icon
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

        fun getColorForMoodStatistic(userMood: Int) : Int {
            return when (userMood) {
                // todo: add specific colors for color theme
                0 -> Color.parseColor("#483D8B")
                1 -> Color.parseColor("#6495ED")
                2 -> Color.parseColor("#FFFACD")
                3 -> Color.parseColor("#FFB6C1")
                4 -> Color.parseColor("#90EE90")
                else -> -1 //
            }
        }
    }
}
