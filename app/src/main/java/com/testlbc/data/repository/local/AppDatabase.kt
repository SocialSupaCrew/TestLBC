package com.testlbc.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        const val DATABASE_NAME = "test_lbc_database"
    }
}
