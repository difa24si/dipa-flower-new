package com.example.dipa_flower.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ChecklistEntity::class, FavoriteNewsEntity::class, DisasterReportEntity::class, CustomReminderEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun checklistDao(): ChecklistDao
    abstract fun favoriteNewsDao(): FavoriteNewsDao
    abstract fun disasterReportDao(): DisasterReportDao
    abstract fun customReminderDao(): CustomReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dipa_flower_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.checklistDao())
                    }
                }
            }

            suspend fun populateDatabase(checklistDao: ChecklistDao) {
                // Pre-populate emergency checklist items
                val defaultItems = listOf(
                    ChecklistEntity(item = "Air Minum", completed = false),
                    ChecklistEntity(item = "Senter", completed = false),
                    ChecklistEntity(item = "Power Bank", completed = false),
                    ChecklistEntity(item = "Obat-obatan", completed = false),
                    ChecklistEntity(item = "Dokumen Penting", completed = false),
                    ChecklistEntity(item = "Makanan Siap Saji", completed = false),
                    ChecklistEntity(item = "Peluit", completed = false)
                )
                checklistDao.insertAll(defaultItems)
            }
        }
    }
}
