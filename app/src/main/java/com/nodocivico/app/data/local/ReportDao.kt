package com.nodocivico.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Query("SELECT * FROM reports ORDER BY createdAtMillis DESC")
    fun getAll(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getById(id: Int): ReportEntity?

    @Query("SELECT COUNT(*) FROM reports")
    fun countAll(): Flow<Int>

    @Query("SELECT COUNT(*) FROM reports WHERE pendingSync = 1")
    fun countPending(): Flow<Int>

    @Query("SELECT COUNT(*) FROM reports WHERE pendingSync = 0")
    fun countSynced(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ReportEntity): Long

    @Update
    suspend fun update(report: ReportEntity)

    @Delete
    suspend fun delete(report: ReportEntity)

    @Query("DELETE FROM reports WHERE id = :id")
    suspend fun deleteById(id: Int)
}