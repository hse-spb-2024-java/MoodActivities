package org.hse.moodactivities.utils

interface GptResponseListener {
    fun onResponseReceived(message: String)
    fun onError(error: Throwable)
    fun onStreamCompleted()
}
