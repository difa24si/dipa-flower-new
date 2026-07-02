package com.example.dipa_flower.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dipa_flower.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Restore legacy system alarms
            ReminderReceiver.scheduleAlarms(context)

            // Reschedule active custom reminders
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val activeReminders = db.customReminderDao().getAllReminders()
                    .filter { it.isEnabled }
                
                for (reminder in activeReminders) {
                    ReminderScheduler.scheduleReminder(context, reminder)
                }
            }
        }
    }
}
