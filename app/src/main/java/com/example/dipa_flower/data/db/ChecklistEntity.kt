package com.example.dipa_flower.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_checklist")
data class ChecklistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val item: String,
    val completed: Boolean
)
