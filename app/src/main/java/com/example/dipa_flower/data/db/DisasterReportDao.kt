package com.example.dipa_flower.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DisasterReportDao {
    @Query("SELECT * FROM disaster_reports ORDER BY id DESC")
    suspend fun getAllReports(): List<DisasterReportEntity>

    @Query("SELECT * FROM disaster_reports WHERE id = :reportId LIMIT 1")
    suspend fun getReportById(reportId: Int): DisasterReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: DisasterReportEntity): Long

    @Update
    suspend fun updateReport(report: DisasterReportEntity)

    @Delete
    suspend fun deleteReport(report: DisasterReportEntity)
}
