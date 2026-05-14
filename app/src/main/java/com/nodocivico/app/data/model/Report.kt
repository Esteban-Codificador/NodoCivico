package com.nodocivico.app.data.model

/**
 * Reporte ciudadano: entidad principal del dominio.
 *
 * Representa una incidencia reportada por un usuario dentro del barrio,
 * conjunto residencial o sector comunitario. Es la entidad sobre la que
 * se realizan las operaciones CRUD del Entregable 2 y la sincronización
 * con la API REST del Entregable 3.
 *
 * Campos:
 *  - id: identificador local. En el Entregable 2 será la PK autogenerada de Room.
 *  - title: título corto que aparece en la lista (ej: "Luminaria dañada en la calle 12").
 *  - description: descripción libre de la incidencia.
 *  - categoryId: referencia a [Category]. Permite filtrar por tipo de problema.
 *  - priority: nivel de prioridad declarado por el usuario.
 *  - status: estado actual del seguimiento.
 *  - location: ubicación referencial en texto (sector, bloque, calle).
 *  - createdAtMillis: marca temporal de creación (epoch ms).
 *  - evidenceUri: ruta opcional a una imagen o archivo de evidencia.
 *  - pendingSync: indica si todavía no fue enviado a la API.
 *                 En el Entregable 1 se deja como flag preparada para el panel de sincronización.
 */
data class Report(
    val id: String,
    val title: String,
    val description: String,
    val categoryId: String,
    val priority: Priority,
    val status: ReportStatus,
    val location: String,
    val createdAtMillis: Long,
    val evidenceUri: String? = null,
    val pendingSync: Boolean = true
)
