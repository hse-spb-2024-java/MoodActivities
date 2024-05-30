package org.hse.moodactivities.schedulers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.app.MoodActivitiesApp
import org.hse.moodactivities.common.proto.services.PushServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.utils.PreferenceManager
import java.util.Calendar

class TimeCheckUpScheduler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            scheduleCheckup(context)
        } else {
            val channel = ManagedChannelBuilder.forAddress("10.0.2.2", TimeCheckUpScheduler.PORT)
                .usePlaintext()
                .build()

            val gptServiceStub = PushServiceGrpc.newStub(channel)
                .withInterceptors(
                    JwtClientInterceptor {
                        PreferenceManager.getData(
                            MoodActivitiesApp.applicationContext(),
                            "jwtToken"
                        )!!
                    })
        }
    }

    companion object {
        const val PORT = 12345
        fun scheduleCheckup(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, TimeCheckUpScheduler::class.java).apply {
                action = "PERFORM_CHECKUP"
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 19)
                set(Calendar.MINUTE, (55..59).random())
            }

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
}
