package com.example.dipa_flower.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM emergency_checklist")
    suspend fun getAllChecklist(): List<ChecklistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklist(checklist: ChecklistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checklists: List<ChecklistEntity>)

    @Update
    suspend fun updateChecklist(checklist: ChecklistEntity)

    @Delete
    suspend fun deleteChecklist(checklist: ChecklistEntity)

    @Query("SELECT COUNT(*) FROM emergency_checklist")
    suspend fun getCount(): Int
}
