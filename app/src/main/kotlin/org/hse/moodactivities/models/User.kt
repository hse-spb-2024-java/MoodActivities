package org.hse.moodactivities.models

enum class AuthType {
    PLAIN, GOOGLE;
}

data class User(
    val id: Long,
    val authType: AuthType
)
