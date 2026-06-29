package com.example.dipa_flower.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dipa_flower.BaseActivity
import com.example.dipa_flower.R
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifikasi pengingat mitigasi dan kesiapsiagaan bencana"
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

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_warning) // fallback to warning icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

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
                // Jika waktu sudah lewat, set ke besok hari
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

            // 2. Weekly Alarm (e.g., Saturday at 10:00 AM)
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
