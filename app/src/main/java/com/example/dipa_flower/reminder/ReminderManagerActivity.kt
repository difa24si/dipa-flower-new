package com.example.dipa_flower.reminder

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dipa_flower.data.db.AppDatabase
import com.example.dipa_flower.data.db.CustomReminderEntity
import com.example.dipa_flower.databinding.ActivityReminderManagerBinding
import com.example.dipa_flower.databinding.DialogAddReminderBinding
import com.example.dipa_flower.receiver.ReminderScheduler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class ReminderManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderManagerBinding
    private lateinit var adapter: CustomReminderAdapter

    private var selectedHour: Int = 8
    private var selectedMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutHeader) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        setupRecyclerView()
        loadReminders()

        binding.btnBack.setOnClickListener { finish() }

        binding.fabAddReminder.setOnClickListener {
            showAddReminderDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        loadReminders()
    }

    private fun setupRecyclerView() {
        binding.rvReminders.layoutManager = LinearLayoutManager(this)
        adapter = CustomReminderAdapter(
            reminders = emptyList(),
            onToggle = { reminder, isEnabled ->
                toggleReminder(reminder, isEnabled)
            },
            onDelete = { reminder ->
                showDeleteConfirmationDialog(reminder)
            }
        )
        binding.rvReminders.adapter = adapter
    }

    private fun loadReminders() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ReminderManagerActivity)
            val list = withContext(Dispatchers.IO) {
                db.customReminderDao().getAllReminders()
            }

            if (list.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvReminders.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvReminders.visibility = View.VISIBLE
            }
            adapter.updateData(list)
        }
    }

    private fun toggleReminder(reminder: CustomReminderEntity, isEnabled: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@ReminderManagerActivity)
            val updated = reminder.copy(isEnabled = isEnabled)
            db.customReminderDao().updateReminder(updated)

            if (isEnabled) {
                ReminderScheduler.scheduleReminder(this@ReminderManagerActivity, updated)
                withContext(Dispatchers.Main) {
                    val timeStr = String.format(Locale.getDefault(), "%02d:%02d", reminder.hour, reminder.minute)
                    Toast.makeText(this@ReminderManagerActivity, "Pengingat aktif pukul $timeStr", Toast.LENGTH_SHORT).show()
                }
            } else {
                ReminderScheduler.cancelReminder(this@ReminderManagerActivity, updated)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ReminderManagerActivity, "Pengingat dinonaktifkan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(reminder: CustomReminderEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Hapus Pengingat?")
            .setMessage("Apakah Anda yakin ingin menghapus pengingat \"${reminder.title}\"?")
            .setPositiveButton("Hapus") { dialog, _ ->
                deleteReminder(reminder)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteReminder(reminder: CustomReminderEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@ReminderManagerActivity)
            ReminderScheduler.cancelReminder(this@ReminderManagerActivity, reminder)
            db.customReminderDao().deleteReminder(reminder)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ReminderManagerActivity, "Pengingat berhasil dihapus", Toast.LENGTH_SHORT).show()
                loadReminders()
            }
        }
    }

    private fun showAddReminderDialog() {
        // Reset selected time to current time
        val now = Calendar.getInstance()
        selectedHour = now.get(Calendar.HOUR_OF_DAY)
        selectedMinute = now.get(Calendar.MINUTE)

        val dialogBinding = DialogAddReminderBinding.inflate(layoutInflater)
        dialogBinding.tvSelectedTime.text =
            String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)

        dialogBinding.btnSelectTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    dialogBinding.tvSelectedTime.text =
                        String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                },
                selectedHour,
                selectedMinute,
                true
            ).show()
        }

        MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan Pengingat") { dialog, _ ->
                val title = dialogBinding.etReminderTitle.text.toString().trim()
                val message = dialogBinding.etReminderMessage.text.toString().trim()

                if (title.isEmpty()) {
                    dialogBinding.etReminderTitle.error = "Judul tidak boleh kosong"
                    return@setPositiveButton
                }
                if (message.isEmpty()) {
                    dialogBinding.etReminderMessage.error = "Pesan tidak boleh kosong"
                    return@setPositiveButton
                }

                saveNewReminder(title, message, selectedHour, selectedMinute)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun saveNewReminder(title: String, message: String, hour: Int, minute: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@ReminderManagerActivity)
            val reminder = CustomReminderEntity(
                title = title,
                message = message,
                hour = hour,
                minute = minute,
                isEnabled = true
            )
            val insertedId = db.customReminderDao().insertReminder(reminder).toInt()
            val savedReminder = reminder.copy(id = insertedId)
            ReminderScheduler.scheduleReminder(this@ReminderManagerActivity, savedReminder)

            withContext(Dispatchers.Main) {
                val timeStr = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                Toast.makeText(
                    this@ReminderManagerActivity,
                    "Pengingat \"$title\" aktif setiap pukul $timeStr",
                    Toast.LENGTH_LONG
                ).show()
                loadReminders()
            }
        }
    }
}
