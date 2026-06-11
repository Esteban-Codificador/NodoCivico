package com.nodocivico.app.data.model

data class Report(
    val id: String,
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
)
