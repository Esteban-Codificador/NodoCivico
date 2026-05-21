package com.nodocivico.app.data.repository

import com.nodocivico.app.data.local.AppDatabase
import com.nodocivico.app.data.local.ReportEntity
import com.nodocivico.app.data.model.Report
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReportRepository(private val db: AppDatabase) {

    val allReports: Flow<List<Report>> =
        db.reportDao().getAll().map { list -> list.map { it.toDomain() } }

    val countAll: Flow<Int> = db.reportDao().countAll()
    val countPending: Flow<Int> = db.reportDao().countPending()
    val countSynced: Flow<Int> = db.reportDao().countSynced()

    suspend fun getById(id: Int): Report? =
        db.reportDao().getById(id)?.toDomain()

    suspend fun insert(report: Report): Long =
        db.reportDao().insert(ReportEntity.fromDomain(report))

    suspend fun update(report: Report) =
        db.reportDao().update(ReportEntity.fromDomain(report))

    suspend fun delete(report: Report) =
        db.reportDao().delete(ReportEntity.fromDomain(report))

    suspend fun deleteById(id: Int) =
        db.reportDao().deleteById(id)
}