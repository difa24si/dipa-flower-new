package com.example.dipa_flower.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CustomReminderDao {
    @Query("SELECT * FROM custom_reminders ORDER BY id DESC")
    suspend fun getAllReminders(): List<CustomReminderEntity>

    @Query("SELECT * FROM custom_reminders WHERE id = :reminderId LIMIT 1")
    suspend fun getReminderById(reminderId: Int): CustomReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: CustomReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: CustomReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: CustomReminderEntity)
}
