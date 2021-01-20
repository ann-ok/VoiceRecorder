package com.example.vaytsu_voicerecorder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecordDatabaseDao {
    @Insert
    fun insert(record: RecordingItem)

    @Update
    fun update(record: RecordingItem)

    @Query("SELECT * from recording WHERE id = :key")
    fun getRecord(key: Long?): RecordingItem?

    @Query("DELETE FROM recording")
    fun clearAll()

    @Query("DELETE FROM recording WHERE id = :key")
    fun removeRecord(key: Long?)

    @Query("SELECT * FROM recording ORDER BY id DESC")
    fun getAllRecords(): LiveData<MutableList<RecordingItem>>

    @Query("SELECT COUNT(*) FROM recording")
    fun getCount(): LiveData<Int>

    // For RecordDatabaseTest
    @Query("SELECT COUNT(*) FROM recording")
    fun getCountInt(): Int
}