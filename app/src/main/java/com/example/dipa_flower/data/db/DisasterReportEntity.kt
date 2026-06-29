package com.example.dipa_flower.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disaster_reports")
data class DisasterReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val disasterType: String,
    val location: String,
    val date: String,
    val description: String,
    val reporterName: String
)
