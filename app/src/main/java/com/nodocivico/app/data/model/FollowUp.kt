package com.nodocivico.app.data.model

/**
 * Comentario o registro de seguimiento sobre un reporte.
 *
 * Cada FollowUp representa una observación añadida durante la gestión del
 * caso (ej: "Se contactó al técnico", "Se reabrió el ticket"). Esto da
 * trazabilidad al historial del reporte.
 *
 * En el Entregable 2 se vincula con Report mediante FK en Room.
 */
data class FollowUp(
    val id: String,
    val reportId: String,
    val authorName: String,
    val note: String,
    val createdAtMillis: Long
)
