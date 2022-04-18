package com.testlbc.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM song ORDER BY albumId ASC, id ASC")
    fun getAll(): Flow<List<Song>>

    @Query("SELECT * FROM song WHERE albumId = :albumId")
    fun getSongs(albumId: Int): Flow<List<Song>>

    @Query("SELECT * FROM song WHERE id = :songId")
    suspend fun getSong(songId: Int): Song

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audioFile: Song)
}
