package org.hse.moodactivities.models

class ActivatedItem(
    name: String, iconIndex: Int, iconColor: Int, private var isActive: Boolean
) : Item(name, iconIndex, iconColor) {
    fun getIsActive(): Boolean {
        return isActive
    }
}
