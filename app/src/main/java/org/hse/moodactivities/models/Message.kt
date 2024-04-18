package org.hse.moodactivities.models

class Message(
    var text: String,
    var isFromUser: Boolean
) {
    fun getText(): String {
        return text
    }

    fun setText(text: String) {
        this.text = text
    }

    fun isFromUser(): Boolean {
        return isFromUser
    }

    fun setFromUser(fromUser: Boolean) {
        this.isFromUser = fromUser
    }
}
