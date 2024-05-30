package org.hse.moodactivities.app

import android.app.Application
import android.content.Context

class MoodActivitiesApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MoodActivitiesApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
