package com.nodocivico.app.data.repository

import android.util.Log
import com.nodocivico.app.data.local.ReportDao
import com.nodocivico.app.data.local.ReportEntity
import com.nodocivico.app.network.ApiService
import com.nodocivico.app.network.ReportRequest

/**
 * Gestiona la sincronización bidireccional entre Room (local) y la API REST (servidor).
 *
 * Estrategia:
 *  1. PUSH: sube reportes locales con pendingSync=true al servidor.
 *  2. PULL: descarga reportes del servidor y los inserta/actualiza localmente.
 */
class SyncManager(
    private val dao: ReportDao,
    private val api: ApiService
) {

    companion object {
        private const val TAG = "SyncManager"
    }

    data class SyncResult(
        val pushed: Int = 0,
        val pulled: Int = 0,
        val errors: Int = 0
    )

    /**
     * Ejecuta la sincronización completa: primero push, luego pull.
     */
    suspend fun sync(): SyncResult {
        var pushed = 0
        var pulled = 0
        var errors = 0

        // --- PUSH: enviar reportes pendientes al servidor ---
        val pending = dao.getPendingSync()
        Log.d(TAG, "Reportes pendientes de sync: ${pending.size}")

        for (entity in pending) {
            try {
                val request = entity.toRequest()

                if (entity.remoteId != null) {
                    // Ya existe en el servidor → actualizar
                    val remoteIdInt = entity.remoteId.toIntOrNull()
                    if (remoteIdInt != null) {
                        val response = api.updateReport(remoteIdInt, request)
                        if (response.isSuccessful) {
                            dao.update(entity.copy(pendingSync = false))
                            pushed++
                            Log.d(TAG, "Actualizado en servidor: remoteId=${entity.remoteId}")
                        } else {
                            errors++
                            Log.e(TAG, "Error actualizando remoteId=${entity.remoteId}: ${response.code()}")
                        }
                    }
                } else {
                    // Nuevo reporte → crear en el servidor
                    val response = api.createReport(request)
                    if (response.isSuccessful) {
                        val dto = response.body()
                        if (dto != null) {
                            dao.update(
                                entity.copy(
                                    remoteId = dto.id.toString(),
                                    pendingSync = false
                                )
                            )
                            pushed++
                            Log.d(TAG, "Creado en servidor: localId=${entity.id}, remoteId=${dto.id}")
                        }
                    } else {
                        errors++
                        Log.e(TAG, "Error creando reporte localId=${entity.id}: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                errors++
                Log.e(TAG, "Excepción sincronizando localId=${entity.id}: ${e.message}")
            }
        }

        // --- PULL: descargar reportes del servidor ---
        try {
            val response = api.getReports()
            if (response.isSuccessful) {
                val serverReports = response.body() ?: emptyList()
                Log.d(TAG, "Reportes obtenidos del servidor: ${serverReports.size}")

                for (dto in serverReports) {
                    val remoteId = dto.id.toString()
                    val existing = dao.getByRemoteId(remoteId)

                    if (existing == null) {
                        // Reporte nuevo del servidor → insertar localmente
                        val domain = dto.toDomain()
                        dao.insert(
                            ReportEntity.fromDomain(domain).copy(
                                id = 0,
                                remoteId = remoteId,
                                pendingSync = false
                            )
                        )
                        pulled++
                        Log.d(TAG, "Descargado del servidor: remoteId=$remoteId")
                    } else if (!existing.pendingSync) {
                        // Actualizar con datos del servidor solo si no hay cambios locales pendientes
                        val domain = dto.toDomain()
                        dao.update(
                            ReportEntity.fromDomain(domain).copy(
                                id = existing.id,
                                remoteId = remoteId,
                                pendingSync = false
                            )
                        )
                        pulled++
                    }
                }
            } else {
                errors++
                Log.e(TAG, "Error descargando reportes: ${response.code()}")
            }
        } catch (e: Exception) {
            errors++
            Log.e(TAG, "Excepción descargando reportes: ${e.message}")
        }

        val result = SyncResult(pushed, pulled, errors)
        Log.d(TAG, "Sync completado: $result")
        return result
    }

    private fun ReportEntity.toRequest() = ReportRequest(
        title = title,
        description = description,
        category = categoryId,
        priority = priority.name,
        status = status.name,
        location = location,
        userId = userId
    )
}
