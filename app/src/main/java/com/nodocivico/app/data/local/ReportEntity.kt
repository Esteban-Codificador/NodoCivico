package com.nodocivico.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nodocivico.app.data.model.Priority
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.model.ReportStatus

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteId: String? = null,
    val userId: Int = 1,
    val title: String,
    val description: String,
    val categoryId: String,
    val priority: Priority,
    val status: ReportStatus,
    val location: String,
    val createdAtMillis: Long,
    val updatedAtMillis: Long = createdAtMillis,
    val evidenceUri: String? = null,
    val pendingSync: Boolean = true
) {
    fun toDomain(): Report = Report(
        id = id.toString(),
        remoteId = remoteId,
        userId = userId,
        title = title,
        description = description,
        categoryId = categoryId,
        priority = priority,
        status = status,
        location = location,
        createdAtMillis = createdAtMillis,
        updatedAtMillis = updatedAtMillis,
        evidenceUri = evidenceUri,
        pendingSync = pendingSync
    )

    companion object {
        fun fromDomain(report: Report): ReportEntity = ReportEntity(
            id = report.id.toIntOrNull() ?: 0,
            remoteId = report.remoteId,
            userId = report.userId,
            title = report.title,
            description = report.description,
            categoryId = report.categoryId,
            priority = report.priority,
            status = report.status,
            location = report.location,
            createdAtMillis = report.createdAtMillis,
            updatedAtMillis = report.updatedAtMillis,
            evidenceUri = report.evidenceUri,
            pendingSync = report.pendingSync
        )
    }
}
