package org.hse.moodactivities.models

class User {
    private lateinit var username: String
    private lateinit var login: String

    fun getUsername() : String {
        return this.username
    }
}
