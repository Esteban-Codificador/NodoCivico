package com.nodocivico.app.network

import com.nodocivico.app.data.model.Priority
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.model.ReportStatus

/**
 * DTO que representa un reporte tal como lo devuelve la API REST.
 */
data class ReportDto(
    val id: Int,
    val remoteId: String?,
    val userId: Int?,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String,
    val location: String?,
    val pendingSync: Boolean?,
    val createdAt: Long?,
    val updatedAt: Long?
) {
    fun toDomain(): Report = Report(
        id = id.toString(),
        remoteId = (remoteId ?: id.toString()),
        userId = userId ?: 1,
        title = title,
        description = description,
        categoryId = category,
        priority = try { Priority.valueOf(priority) } catch (_: Exception) { Priority.MEDIA },
        status = try { ReportStatus.valueOf(status) } catch (_: Exception) { ReportStatus.OPEN },
        location = location ?: "",
        createdAtMillis = createdAt ?: System.currentTimeMillis(),
        updatedAtMillis = updatedAt ?: System.currentTimeMillis(),
        pendingSync = false
    )
}

/**
 * Cuerpo para crear o actualizar un reporte en la API.
 */
data class ReportRequest(
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String,
    val location: String,
    val userId: Int = 1
)

/**
 * Cuerpo para actualizar solo el estado de un reporte.
 */
data class StatusRequest(
    val status: String
)

/**
 * Respuesta del DELETE /reports/{id}.
 */
data class DeleteResponse(
    val deleted: Boolean,
    val id: Int
)
