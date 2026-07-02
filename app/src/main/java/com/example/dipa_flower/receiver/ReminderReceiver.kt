package com.example.dipa_flower.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dipa_flower.BaseActivity
import com.example.dipa_flower.R
import com.example.dipa_flower.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == "com.example.dipa_flower.ACTION_CUSTOM_REMINDER") {
            val id = intent.getIntExtra("reminder_id", 0)
            val title = intent.getStringExtra("reminder_title") ?: "Pengingat Siaga Bencana"
            val message = intent.getStringExtra("reminder_message") ?: "Rencana kesiapsiagaan mandiri."

            showNotification(context, id, title, message)

            // Reschedule the exact alarm for tomorrow so it repeats daily
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val reminder = db.customReminderDao().getReminderById(id)
                if (reminder != null && reminder.isEnabled) {
                    ReminderScheduler.scheduleReminder(context, reminder)
                }
            }
            return
        }

        val type = intent.getStringExtra("type") ?: return
        when (type) {
            "daily" -> showNotification(
                context,
                101,
                "Checklist Perlengkapan Darurat",
                "Jangan lupa periksa perlengkapan darurat Anda hari ini."
            )
            "weekly" -> showNotification(
                context,
                102,
                "Simulasi Kesiapsiagaan",
                "Saatnya melakukan simulasi kesiapsiagaan bencana bersama keluarga."
            )
        }
    }

    companion object {
        private const val CHANNEL_ID = "emergency_reminder_channel"
        private const val CHANNEL_NAME = "Reminder Siaga Bencana"

        fun showNotification(context: Context, id: Int, title: String, message: String) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Premium high-priority notification channel config
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifikasi pengingat mitigasi dan kesiapsiagaan bencana"
                    enableLights(true)
                    lightColor = Color.RED
                    enableVibration(true)
                    vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
                }
                notificationManager.createNotificationChannel(channel)
            }

            val intent = Intent(context, BaseActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Set beautiful and premium notification styles
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setColor(Color.parseColor("#2E5A27")) // Forest Green theme color
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100))
                .setAutoCancel(true)
                .setSubText("Siaga Bencana")

            notificationManager.notify(id, builder.build())
        }

        fun scheduleAlarms(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // 1. Daily Alarm at 08:00 AM
            val dailyIntent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("type", "daily")
            }
            val dailyPendingIntent = PendingIntent.getBroadcast(
                context,
                201,
                dailyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val dailyCalendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                dailyCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                dailyPendingIntent
            )

            // 2. Weekly Alarm (Saturday at 10:00 AM)
            val weeklyIntent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("type", "weekly")
            }
            val weeklyPendingIntent = PendingIntent.getBroadcast(
                context,
                202,
                weeklyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val weeklyCalendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.WEEK_OF_YEAR, 1)
                }
            }

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                weeklyCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                weeklyPendingIntent
            )
        }
    }
}
