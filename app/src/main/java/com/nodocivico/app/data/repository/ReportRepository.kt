package com.nodocivico.app.data.repository

import com.nodocivico.app.data.local.AppDatabase
import com.nodocivico.app.data.local.ReportEntity
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.network.ApiService
import com.nodocivico.app.network.ReportRequest
import com.nodocivico.app.network.StatusRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReportRepository(
    private val db: AppDatabase,
    private val api: ApiService
) {

    private val dao = db.reportDao()
    private val syncManager = SyncManager(dao, api)

    val allReports: Flow<List<Report>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    val countAll: Flow<Int> = dao.countAll()
    val countPending: Flow<Int> = dao.countPending()
    val countSynced: Flow<Int> = dao.countSynced()

    suspend fun getById(id: Int): Report? =
        dao.getById(id)?.toDomain()

    suspend fun insert(report: Report): Long =
        dao.insert(ReportEntity.fromDomain(report))

    suspend fun update(report: Report) =
        dao.update(ReportEntity.fromDomain(report))

    suspend fun delete(report: Report) {
        val entity = ReportEntity.fromDomain(report)
        // Si tiene remoteId, intentar borrar en el servidor
        entity.remoteId?.toIntOrNull()?.let { remoteId ->
            try { api.deleteReport(remoteId) } catch (_: Exception) {}
        }
        dao.delete(entity)
    }

    suspend fun deleteById(id: Int) {
        val entity = dao.getById(id)
        if (entity != null) {
            entity.remoteId?.toIntOrNull()?.let { remoteId ->
                try { api.deleteReport(remoteId) } catch (_: Exception) {}
            }
            dao.deleteById(id)
        }
    }

    /**
     * Actualiza solo el estado de un reporte, tanto local como en servidor si tiene remoteId.
     */
    suspend fun updateStatus(report: Report, newStatus: com.nodocivico.app.data.model.ReportStatus): Report {
        val updated = report.copy(
            status = newStatus,
            pendingSync = true,
            updatedAtMillis = System.currentTimeMillis()
        )
        dao.update(ReportEntity.fromDomain(updated))

        // Intentar actualizar en el servidor
        updated.remoteId?.toIntOrNull()?.let { remoteId ->
            try {
                val response = api.updateStatus(remoteId, StatusRequest(newStatus.name))
                if (response.isSuccessful) {
                    val synced = updated.copy(pendingSync = false)
                    dao.update(ReportEntity.fromDomain(synced))
                    return synced
                }
            } catch (_: Exception) {}
        }
        return updated
    }

    /**
     * Ejecuta la sincronización bidireccional con el servidor.
     */
    suspend fun sync(): SyncManager.SyncResult = syncManager.sync()
}
