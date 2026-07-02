package com.example.dipa_flower.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_reminders")
data class CustomReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean
)
