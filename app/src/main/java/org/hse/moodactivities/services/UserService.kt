package org.hse.moodactivities.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class UserService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        const val ADDRESS = "10.0.2.2"
        const val PORT = 12345
    }
}
